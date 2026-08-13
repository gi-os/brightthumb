@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val KB_EN_MESSAGEASE_COMPOSE_LEFT_FLIPPED_NUMPAD: KeyboardDefinition =
    KeyboardDefinition(
        title = "english messagease compose left-handed with a flipped numpad layout",
        modes =
            KeyboardDefinitionModes(
                main = lastColKeysToFirst(KB_EN_MESSAGEASE_COMPOSED_MAIN),
                shifted = lastColKeysToFirst(KB_EN_MESSAGEASE_COMPOSED_SHIFTED),
                numeric = lastColKeysToFirst(NUMERIC_KEYBOARD_FLIPPED),
            ),
        settings =
            KeyboardDefinitionSettings(
                autoCapitalizers = arrayOf(::autoCapitalizeI, ::autoCapitalizeIApostrophe),
            ),
    )
