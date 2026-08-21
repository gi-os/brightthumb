package com.gios.brightthumb

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Invisible one-shot activity that asks for RECORD_AUDIO on behalf of the IME
 * (a keyboard can't show a permission dialog itself). Finishes immediately
 * after the user answers.
 */
class MicPermissionActivity : ComponentActivity() {
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast
                    .makeText(this, getString(R.string.voice_mic_granted), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast
                    .makeText(this, getString(R.string.voice_mic_denied), Toast.LENGTH_LONG)
                    .show()
                if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    // "Don't ask again" — the only path left is app settings.
                    startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        },
                    )
                }
            }
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission.launch(Manifest.permission.RECORD_AUDIO)
    }
}
