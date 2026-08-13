@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val KB_EN_MESSAGEASE_LEFT_SYMBOLS: KeyboardDefinition =
    KeyboardDefinition(
        title = "english messagease left-handed symbols",
        modes =
            KeyboardDefinitionModes(
                main = lastColKeysToFirst(KB_EN_MESSAGEASE_SYMBOLS_MAIN),
                shifted = lastColKeysToFirst(KB_EN_MESSAGEASE_SYMBOLS_SHIFTED),
                numeric = lastColKeysToFirst(KB_EN_MESSAGEASE_NUMERIC),
            ),
        settings =
            KeyboardDefinitionSettings(
                autoCapitalizers = arrayOf(::autoCapitalizeI, ::autoCapitalizeIApostrophe),
            ),
    )
