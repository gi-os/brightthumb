package com.gios.brightthumb

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.gios.brightthumb.utils.TAG
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A crash log that outlives the process.
 *
 * Most of BrightThumb does not run in an app you are looking at. The IME is a
 * service: it wakes for a clipboard change, a cursor update, a settings write,
 * and it dies in the background where no dialog is shown and no logcat is
 * attached. Before this, a background crash left nothing behind at all -- the
 * only symptom was a keyboard that "keeps crashing" with nowhere to look.
 *
 * So the uncaught-exception handler writes the stack trace to a file first, then
 * hands the throwable to the platform handler so the process still dies normally.
 * Settings -> About surfaces the last one and copies it out, which is the only
 * way to read it on a phone with no cable.
 *
 * One entry, not a ring buffer: the crash you want is the last one, and a
 * keyboard should not accumulate files.
 */
object CrashLog {
    private const val FILE_NAME = "last-crash.txt"
    private const val MAX_CHARS = 16_000

    fun install(context: Context) {
        val appContext = context.applicationContext
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Wrapped: a handler that throws replaces the real crash with its own
            // and loses the trace we came here to keep.
            try {
                write(appContext, thread, throwable)
            } catch (e: Throwable) {
                Log.e(TAG, "could not record crash", e)
            }
            previous?.uncaughtException(thread, throwable)
        }
    }

    private fun write(
        context: Context,
        thread: Thread,
        throwable: Throwable,
    ) {
        val stack =
            StringWriter().also { sw ->
                PrintWriter(sw).use { throwable.printStackTrace(it) }
            }.toString()

        val stamp =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        val text =
            buildString {
                appendLine("${throwable.javaClass.simpleName}: ${throwable.message}")
                appendLine("BrightThumb ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                appendLine("$stamp on thread ${thread.name}")
                appendLine()
                append(stack)
            }.take(MAX_CHARS)

        File(context.filesDir, FILE_NAME).writeText(text)
    }

    fun read(context: Context): String? =
        try {
            File(context.filesDir, FILE_NAME)
                .takeIf { it.exists() && it.length() > 0 }
                ?.readText()
        } catch (e: Throwable) {
            Log.e(TAG, "could not read crash log", e)
            null
        }

    fun clear(context: Context) {
        try {
            File(context.filesDir, FILE_NAME).delete()
        } catch (e: Throwable) {
            Log.e(TAG, "could not clear crash log", e)
        }
    }

    fun copyToClipboard(
        context: Context,
        text: String,
    ) {
        try {
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("BrightThumb crash", text))
        } catch (e: Throwable) {
            Log.e(TAG, "could not copy crash log", e)
        }
    }
}
