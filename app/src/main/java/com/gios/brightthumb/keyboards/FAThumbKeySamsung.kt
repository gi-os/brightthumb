@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val KB_FA_THUMBKEY_SAMSUNG: KeyboardDefinition =
    KeyboardDefinition(
        title = "فارسی thumb-key samsung",
        modes =
            KeyboardDefinitionModes(
                main = KB_FA_THUMBKEY_MAIN,
                shifted = KB_FA_THUMBKEY_MAIN,
                numeric = FARSI_NUMERIC_SAMSUNG_KEYBOARD,
            ),
    )
