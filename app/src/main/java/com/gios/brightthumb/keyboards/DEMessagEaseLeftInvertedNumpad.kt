@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.KeyboardDefinition
import com.gios.brightthumb.utils.KeyboardDefinitionModes
import com.gios.brightthumb.utils.lastColKeysToFirst

val KB_DE_MESSAGEASE_LEFT_INVERTED_NUMPAD: KeyboardDefinition =
    KeyboardDefinition(
        title = "deutsch messagease left-handed, inverted numpad",
        modes =
            KeyboardDefinitionModes(
                main = lastColKeysToFirst(KB_DE_MESSAGEASE_MAIN),
                shifted = lastColKeysToFirst(KB_DE_MESSAGEASE_SHIFTED),
                numeric = lastColKeysToFirst(KB_DE_MESSAGEASE_INVERTED_NUMPAD_NUMERIC),
            ),
    )
