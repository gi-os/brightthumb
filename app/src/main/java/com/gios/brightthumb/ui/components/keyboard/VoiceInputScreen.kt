package com.gios.brightthumb.ui.components.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gios.brightthumb.R
import com.gios.brightthumb.voice.VoiceInputManager
import com.gios.brightthumb.voice.VoiceInputState

/**
 * Replaces the keyboard while voice input is active. Kept deliberately stark
 * (big shapes, no color) so it reads well on the LPIII's monochrome panel.
 */
@Composable
fun VoiceInputScreen(
    state: VoiceInputState,
    keyboardHeight: Dp,
    onDone: () -> Unit,
    onCancel: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(keyboardHeight)
                .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            when (state) {
                is VoiceInputState.Recording -> {
                    val seconds = VoiceInputManager.recordedSeconds
                    val remaining = VoiceInputManager.MAX_RECORD_SECONDS - seconds
                    Text(
                        text =
                            stringResource(R.string.voice_listening) +
                                "  %d:%02d".format(seconds / 60, seconds % 60) +
                                (if (remaining <= 5) "  ($remaining)" else ""),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    // Simple horizontal level meter.
                    val level = VoiceInputManager.audioLevel
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(0.8f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(level.coerceIn(0.02f, 1f))
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(MaterialTheme.colorScheme.onBackground),
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        VoiceRoundButton(
                            icon = Icons.Outlined.Close,
                            description = stringResource(R.string.voice_cancel),
                            emphasized = false,
                            onClick = onCancel,
                        )
                        VoiceRoundButton(
                            icon = Icons.Outlined.Check,
                            description = stringResource(R.string.voice_done),
                            emphasized = true,
                            onClick = onDone,
                        )
                    }
                }

                is VoiceInputState.Transcribing -> {
                    Icon(
                        imageVector = Icons.Outlined.HourglassEmpty,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text =
                            stringResource(
                                if (VoiceInputManager.modelReady) {
                                    R.string.voice_transcribing
                                } else {
                                    R.string.voice_loading_model
                                },
                            ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                is VoiceInputState.Error -> {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    VoiceRoundButton(
                        icon = Icons.Outlined.Close,
                        description = stringResource(R.string.voice_cancel),
                        emphasized = false,
                        onClick = onCancel,
                    )
                }

                is VoiceInputState.Idle -> {}
            }
        }
    }
}

@Composable
private fun VoiceRoundButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    emphasized: Boolean,
    onClick: () -> Unit,
) {
    val bg =
        if (emphasized) {
            MaterialTheme.colorScheme.onBackground
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    val fg =
        if (emphasized) {
            MaterialTheme.colorScheme.background
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    Box(
        modifier =
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(bg)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(32.dp),
            tint = fg,
        )
    }
}
