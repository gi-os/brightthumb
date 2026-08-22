package com.gios.brightthumb.voice

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.gios.brightthumb.IMEService
import com.gios.brightthumb.MicPermissionActivity
import com.gios.brightthumb.utils.TAG
import com.k2fsa.sherpa.onnx.FeatureConfig
import com.k2fsa.sherpa.onnx.OfflineModelConfig
import com.k2fsa.sherpa.onnx.OfflineRecognizer
import com.k2fsa.sherpa.onnx.OfflineRecognizerConfig
import com.k2fsa.sherpa.onnx.OfflineTransducerModelConfig
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import kotlin.math.min
import kotlin.math.sqrt

sealed class VoiceInputState {
    data object Idle : VoiceInputState()

    data object Recording : VoiceInputState()

    data object Transcribing : VoiceInputState()

    data class Error(
        val message: String,
    ) : VoiceInputState()
}

/**
 * Fully offline voice typing: records from the mic and transcribes with NVIDIA's
 * Parakeet TDT 110M (int8) through sherpa-onnx. Nothing ever leaves the device,
 * which keeps BrightThumb's no-network promise intact.
 *
 * ## Why not whisper tiny.en
 *
 * It shipped with whisper tiny.en, and the honest report from using it was that
 * it felt a decade out of date. It measures that way too: on the eight-domain
 * Open ASR average, tiny.en is around 12.8% WER against Parakeet 110M's 7.5%,
 * and the gap is widest exactly where dictation lives — conversational speech
 * rather than read audiobooks, which is why clean test clips flattered it.
 *
 * Parakeet is also *faster*, which is the unusual part. Whisper decodes a full
 * 30-second window whatever you gave it, so a two-second "yes, on my way" cost
 * the same as half a minute of talking. A transducer decodes the audio it has.
 * Measured on the same clips at two threads, it runs about two and a half times
 * quicker, and that ratio grows on short utterances — the ones a keyboard
 * actually sees.
 *
 * It punctuates and capitalises natively, so no separate punctuation model.
 *
 * The cost is roughly 30MB more in the APK. Worth it: the previous model's
 * output needed correcting often enough that dictation stopped being faster
 * than typing, which for a keyboard is the whole argument.
 *
 * A single dictation is still capped at [MAX_RECORD_SECONDS] — a transducer has
 * no window limit, but an unbounded recording is a memory question rather than
 * a model one, and half a minute is already far past what anyone dictates into
 * a phone in one breath.
 */
object VoiceInputManager {
    const val SAMPLE_RATE = 16000
    const val MAX_RECORD_SECONDS = 28

    private const val ASSET_DIR = "parakeet-110m-en"

    var state by mutableStateOf<VoiceInputState>(VoiceInputState.Idle)
        private set

    /** 0..1-ish RMS of the most recent audio chunk, for the level meter. */
    var audioLevel by mutableFloatStateOf(0f)
        private set

    var recordedSeconds by mutableIntStateOf(0)
        private set

    /** True once the model has finished loading (first use takes a few seconds). */
    var modelReady by mutableStateOf(false)
        private set

    private val mainHandler = Handler(Looper.getMainLooper())

    // One executor for model load + decode, so they serialize naturally.
    private val decodeExecutor = Executors.newSingleThreadExecutor()

    @Volatile
    private var recognizer: OfflineRecognizer? = null

    @Volatile
    private var stopRequested = false

    @Volatile
    private var cancelRequested = false

    private var recordingThread: Thread? = null
    private var imeRef: WeakReference<IMEService>? = null

    fun toggle(ime: IMEService) {
        when (state) {
            is VoiceInputState.Idle, is VoiceInputState.Error -> start(ime)
            is VoiceInputState.Recording -> stopRequested = true
            // Ignore taps while a transcription is in flight.
            is VoiceInputState.Transcribing -> {}
        }
    }

    /** Discard the current recording (or dismiss an error) without committing text. */
    fun cancel() {
        when (state) {
            is VoiceInputState.Recording -> {
                cancelRequested = true
                stopRequested = true
            }

            is VoiceInputState.Error -> state = VoiceInputState.Idle
            else -> {}
        }
    }

    /** Called when the keyboard window goes away mid-recording. */
    fun abort() {
        if (state is VoiceInputState.Recording) {
            cancelRequested = true
            stopRequested = true
        }
    }

    private fun start(ime: IMEService) {
        if (ContextCompat.checkSelfPermission(ime, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            val intent =
                Intent(ime, MicPermissionActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            ime.startActivity(intent)
            return
        }

        imeRef = WeakReference(ime)
        stopRequested = false
        cancelRequested = false
        audioLevel = 0f
        recordedSeconds = 0
        state = VoiceInputState.Recording

        // Kick off the (one-time) model load in parallel with recording, so the
        // first dictation doesn't stare at a spinner before it can even listen.
        ensureRecognizerAsync(ime.applicationContext)

        recordingThread =
            Thread({ recordLoop() }, "brightthumb-voice-record").also { it.start() }
    }

    @SuppressLint("MissingPermission") // checked in start()
    private fun recordLoop() {
        val minBuf =
            AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_FLOAT,
            )
        if (minBuf <= 0) {
            postError("Mic unavailable")
            return
        }

        val record =
            try {
                AudioRecord(
                    MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_FLOAT,
                    maxOf(minBuf * 2, SAMPLE_RATE), // >= 250ms of headroom
                )
            } catch (e: Exception) {
                Log.e(TAG, "AudioRecord init failed", e)
                postError("Mic unavailable")
                return
            }

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            record.release()
            postError("Mic unavailable")
            return
        }

        val maxSamples = SAMPLE_RATE * MAX_RECORD_SECONDS
        val samples = FloatArray(maxSamples)
        var sampleCount = 0
        val chunk = FloatArray(SAMPLE_RATE / 10) // 100 ms

        try {
            record.startRecording()
            while (!stopRequested && sampleCount < maxSamples) {
                val toRead = min(chunk.size, maxSamples - sampleCount)
                val n = record.read(chunk, 0, toRead, AudioRecord.READ_BLOCKING)
                if (n <= 0) break
                System.arraycopy(chunk, 0, samples, sampleCount, n)
                sampleCount += n

                var sum = 0.0
                for (i in 0 until n) sum += chunk[i] * chunk[i]
                val rms = sqrt(sum / n).toFloat()

                mainHandler.post {
                    audioLevel = min(1f, rms * 8f)
                    recordedSeconds = sampleCount / SAMPLE_RATE
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "recording failed", e)
            record.release()
            postError("Recording failed")
            return
        }

        try {
            record.stop()
        } catch (_: Exception) {
        }
        record.release()

        if (cancelRequested || sampleCount < SAMPLE_RATE / 4) {
            // Cancelled, or under 250ms of audio — nothing worth transcribing.
            mainHandler.post {
                audioLevel = 0f
                state = VoiceInputState.Idle
            }
            return
        }

        mainHandler.post {
            audioLevel = 0f
            state = VoiceInputState.Transcribing
        }
        val audio = samples.copyOf(sampleCount)
        decodeExecutor.submit { transcribe(audio) }
    }

    private fun transcribe(audio: FloatArray) {
        val rec = recognizer
        if (rec == null) {
            // ensureRecognizerAsync failed and already surfaced an error.
            if (state is VoiceInputState.Transcribing) {
                postError("Speech model failed to load")
            }
            return
        }
        try {
            val stream = rec.createStream()
            stream.acceptWaveform(audio, SAMPLE_RATE)
            rec.decode(stream)
            val text = rec.getResult(stream).text.trim()
            stream.release()

            mainHandler.post {
                commitText(text)
                state = VoiceInputState.Idle
            }
        } catch (e: Exception) {
            Log.e(TAG, "transcription failed", e)
            postError("Transcription failed")
        }
    }

    private fun commitText(text: String) {
        if (text.isEmpty()) return
        val ime = imeRef?.get() ?: return
        val ic = ime.currentInputConnection ?: return

        // Insert a space when dictating right after existing text.
        val before = ic.getTextBeforeCursor(1, 0)
        val prefix =
            if (!before.isNullOrEmpty() && !before.last().isWhitespace()) " " else ""

        ime.ignoreNextCursorMove()
        ic.commitText(prefix + text, 1)
    }

    private fun ensureRecognizerAsync(appContext: Context) {
        if (recognizer != null) return
        decodeExecutor.submit {
            if (recognizer != null) return@submit
            try {
                val config =
                    OfflineRecognizerConfig(
                        featConfig = FeatureConfig(sampleRate = SAMPLE_RATE, featureDim = 80),
                        modelConfig =
                            OfflineModelConfig(
                                transducer =
                                    OfflineTransducerModelConfig(
                                        encoder = "$ASSET_DIR/encoder.int8.onnx",
                                        decoder = "$ASSET_DIR/decoder.int8.onnx",
                                        joiner = "$ASSET_DIR/joiner.int8.onnx",
                                    ),
                                tokens = "$ASSET_DIR/tokens.txt",
                                // Stated rather than left to the metadata sniffer: NeMo models
                                // need their own per-feature normalisation, and getting that
                                // silently wrong degrades accuracy instead of failing.
                                modelType = "nemo_transducer",
                                numThreads = 2,
                                debug = false,
                            ),
                        decodingMethod = "greedy_search",
                    )
                recognizer = OfflineRecognizer(appContext.assets, config)
                mainHandler.post { modelReady = true }
                Log.d(TAG, "whisper tiny.en loaded")
            } catch (e: Throwable) {
                Log.e(TAG, "failed to load whisper model", e)
                postError("Speech model failed to load")
            }
        }
    }

    private fun postError(message: String) {
        mainHandler.post {
            audioLevel = 0f
            state = VoiceInputState.Error(message)
        }
    }
}
