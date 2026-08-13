@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val KB_EN_MESSAGEASE_SYMBOLS_TWO_HANDS_MAIN =
    KeyboardC(
        listOf(
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[0] +
                KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[0].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[1] +
                KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[1].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[2] +
                KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[2].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[3] +
                KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[3].subList(0, 1),
        ),
    )

val KB_EN_MESSAGEASE_SYMBOLS_TWO_HANDS_SHIFTED =
    KeyboardC(
        listOf(
            KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[0] +
                KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[0].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[1] +
                KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[1].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[2] +
                KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[2].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[3] +
                KB_EN_MESSAGEASE_SYMBOLS_SHIFTED.arr[3].subList(0, 1),
        ),
    )

val NUMERIC_KEYBOARD_MESSAGEASE_TWO_HANDS =
    KeyboardC(
        listOf(
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[0] +
                KB_EN_MESSAGEASE_NUMERIC.arr[0].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[1].subList(0, 3) +
                listOf(ABC_KEY_ITEM) +
                KB_EN_MESSAGEASE_NUMERIC.arr[1].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[2] +
                KB_EN_MESSAGEASE_NUMERIC.arr[2].subList(0, 3),
            KB_EN_MESSAGEASE_SYMBOLS_MAIN.arr[3] +
                KB_EN_MESSAGEASE_NUMERIC.arr[3].subList(0, 2),
        ),
    )

val KB_EN_MESSAGEASE_SYMBOLS_TWO_HANDS: KeyboardDefinition =
    KeyboardDefinition(
        title = "english messagease symbols two-hands",
        modes =
            KeyboardDefinitionModes(
                main = KB_EN_MESSAGEASE_SYMBOLS_TWO_HANDS_MAIN,
                shifted = KB_EN_MESSAGEASE_SYMBOLS_TWO_HANDS_SHIFTED,
                numeric = NUMERIC_KEYBOARD_MESSAGEASE_TWO_HANDS,
            ),
        settings =
            KeyboardDefinitionSettings(
                autoCapitalizers = arrayOf(::autoCapitalizeI, ::autoCapitalizeIApostrophe),
            ),
    )
