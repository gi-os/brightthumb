package com.gios.brightthumb.utils

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.gios.brightthumb.db.ClipboardRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ThumbKeyClipboardManager(
    private val context: Context,
    private val clipboardRepository: ClipboardRepository,
) {
    private val systemClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    // SupervisorJob keeps sibling jobs alive; it does NOT stop an uncaught
    // exception from reaching the thread's default handler and killing the
    // process. A keyboard writing to Room in the background must not be able to
    // die that way, so the scope carries a handler of its own.
    private val scope =
        CoroutineScope(
            SupervisorJob() +
                Dispatchers.IO +
                CoroutineExceptionHandler { _, e ->
                    Log.e(TAG, "clipboard work failed", e)
                },
        )
    private var isListening = false
    private var lastClipText: String? = null

// Used to manage data with a non-text MEME type, when private clipboard is enabled
// When copying data with a MIME type different than a text, e.g. a picture, it is not added to the history, as it’s not a text. With standard paste it’s not an issue as the paste will still paste it.
// However if we paste from the internal clipboard, it will paste the latest string in the history, and not the picture that was only in the system clipboard.
    private var wasLastCopyOperationDoneViaSystem: Boolean = true

    private fun addToClipboardRepo(text: String) {
        if (text.isBlank() || text == lastClipText) return@addToClipboardRepo
        lastClipText = text
        Log.d(TAG, "Adding clipboard item: $text")
        scope.launch {
            clipboardRepository.addItem(text)
        }
    }

    /**
     * Fires on every system copy, anywhere on the phone, whether or not the
     * keyboard is up. Everything in here therefore runs with no window, no focus
     * and no user watching, and anything it throws lands on the main thread's
     * uncaught handler and takes the whole IME process with it.
     *
     * Two things throw here in practice:
     *
     * - Reading the clip at all. Since Android 10 the clipboard is only readable
     *   by the app with focus or the current IME, and a denied read comes back as
     *   null on most builds but as a SecurityException on some.
     * - [android.content.ClipData.Item.coerceToText] on a non-text clip. It
     *   resolves content: URIs through the ContentResolver, on this thread,
     *   against a provider this process usually has no grant for -- so copying an
     *   image in another app could kill the keyboard.
     *
     * Non-text clips are skipped outright (they were never stored anyway, per the
     * note above) and the rest is wrapped.
     */
    private val clipboardListener =
        ClipboardManager.OnPrimaryClipChangedListener {
            try {
                val description = systemClipboardManager.primaryClipDescription
                val isText =
                    description == null ||
                        description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                        description.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)
                if (!isText) {
                    // Still a system copy, and the private-clipboard paste path has to
                    // know that, or it pastes the last string in the history instead of
                    // the image that was actually copied.
                    wasLastCopyOperationDoneViaSystem = true
                    return@OnPrimaryClipChangedListener
                }

                val clip = systemClipboardManager.primaryClip
                if (clip == null || clip.itemCount == 0) return@OnPrimaryClipChangedListener
                val text = clip.getItemAt(0).coerceToText(context).toString()
                addToClipboardRepo(text)
                wasLastCopyOperationDoneViaSystem = true
            } catch (e: Throwable) {
                // Includes SecurityException from a background clipboard read.
                Log.e(TAG, "could not read the clipboard", e)
            }
        }

    fun startListening() {
        if (!isListening) {
            try {
                systemClipboardManager.addPrimaryClipChangedListener(clipboardListener)
                isListening = true
            } catch (e: Throwable) {
                Log.e(TAG, "could not listen to the clipboard", e)
            }
        }
    }

    fun stopListening() {
        if (isListening) {
            try {
                systemClipboardManager.removePrimaryClipChangedListener(clipboardListener)
            } catch (e: Throwable) {
                Log.e(TAG, "could not stop listening to the clipboard", e)
            }
            isListening = false
        }
    }

    fun clearExpired() {
        scope.launch {
            clipboardRepository.clearExpired()
        }
    }

    fun addPrivateClip(text: String) {
        addToClipboardRepo(text)
        wasLastCopyOperationDoneViaSystem = false
    }

    fun wasLastCopyOperationDoneViaSystem(): Boolean = wasLastCopyOperationDoneViaSystem

    fun getLastClip(): String? = lastClipText
}
