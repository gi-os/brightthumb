@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val KB_EN_QBASED_LEFT: KeyboardDefinition =
    KeyboardDefinition(
        title = "english qbased left-handed",
        modes =
            KeyboardDefinitionModes(
                main = lastColKeysToFirst(KB_EN_QBASED_MAIN),
                shifted = lastColKeysToFirst(KB_EN_QBASED_SHIFTED),
                numeric = lastColKeysToFirst(NUMERIC_KEYBOARD),
            ),
        settings =
            KeyboardDefinitionSettings(
                autoCapitalizers = arrayOf(::autoCapitalizeI, ::autoCapitalizeIApostrophe),
            ),
    )
