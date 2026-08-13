@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val KB_MATH_THUMBKEY_MAIN =
    KeyboardC(
        listOf(
            listOf(
                KeyItemC(
                    center = KeyC("∀", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topLeft = KeyC("⊊"),
                    left = KeyC("⊆"),
                    bottomLeft = KeyC("⊂"),
                ),
                KeyItemC(
                    center = KeyC("∫", size = LARGE),
                    swipeType = EIGHT_WAY,
                    left = KeyC("+"),
                    topLeft = KeyC("±"),
                    right = KeyC("!"),
                    bottomLeft = KeyC("∋"),
                    bottomRight = KeyC("∈"),
                    bottom = KeyC("∂"),
                ),
                KeyItemC(
                    center = KeyC("∃", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topRight = KeyC("⊋"),
                    right = KeyC("⊇"),
                    bottomRight = KeyC("⊃"),
                    bottom = KeyC("="),
                    bottomLeft = KeyC("∣"),
                    left = KeyC("≡"),
                ),
                EMOJI_KEY_ITEM,
            ),
            listOf(
                KeyItemC(
                    center = KeyC("∅", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topRight = KeyC("∩"),
                    right = KeyC("∪"),
                    bottomRight = KeyC("⊍"),
                    topLeft = KeyC("{"),
                    left = KeyC("("),
                    bottom = KeyC("𝒫"),
                ),
                KeyItemC(
                    center = KeyC("∎", size = LARGE),
                    bottomRight = KeyC("⇒"),
                    bottom = KeyC("⇔"),
                    bottomLeft = KeyC("⇐"),
                ),
                KeyItemC(
                    center = KeyC("¬", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topLeft = KeyC("∧"),
                    left = KeyC("∨"),
                    bottomLeft = KeyC("⩒"),
                    top =
                        KeyC(
                            display = KeyDisplay.IconDisplay(Icons.Outlined.ArrowDropUp),
                            action = ToggleShiftMode(true),
                            swipeReturnAction = ToggleCurrentWordCapitalization(true),
                            color = MUTED,
                        ),
                    topRight = KeyC("}"),
                    right = KeyC(")"),
                    bottom =
                        KeyC(
                            ToggleShiftMode(false),
                            swipeReturnAction = ToggleCurrentWordCapitalization(false),
                        ),
                ),
                NUMERIC_KEY_ITEM,
            ),
            listOf(
                KeyItemC(
                    center = KeyC("→", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topLeft = KeyC("~"),
                    left = KeyC("≤"),
                    bottomLeft = KeyC("<"),
                    bottom = KeyC("÷"),
                    bottomRight = KeyC(":"),
                    right = KeyC("↦"),
                    top = KeyC("∘"),
                    topRight = KeyC("⋅"),
                ),
                KeyItemC(
                    center = KeyC("∑", size = LARGE),
                    top = KeyC("⨯"),
                    topRight = KeyC("'", color = MUTED),
                    left = KeyC(","),
                    bottomRight = KeyC("-", color = MUTED),
                    bottom = KeyC(".", color = MUTED),
                    bottomLeft = KeyC("*", color = MUTED),
                    right = KeyC("∏", color = MUTED),
                    topLeft = KeyC("∞", color = MUTED),
                ),
                KeyItemC(
                    center = KeyC("ℕ", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topRight = KeyC("°"),
                    right = KeyC("≥"),
                    bottomRight = KeyC(">"),
                    top = KeyC("ℂ"),
                    topLeft = KeyC("ℝ"),
                    left = KeyC("ℚ"),
                    bottomLeft = KeyC("ℙ"),
                    bottom = KeyC("ℤ"),
                ),
                BACKSPACE_KEY_ITEM,
            ),
            listOf(
                SPACEBAR_KEY_ITEM,
                RETURN_KEY_ITEM,
            ),
        ),
    )

val KB_MATH_THUMBKEY_SLASH =
    KeyboardC(
        listOf(
            listOf(
                KeyItemC(
                    center = KeyC("", size = LARGE),
                    swipeType = EIGHT_WAY,
                    left = KeyC("⊈"),
                    bottomLeft = KeyC("⊄"),
                ),
                KeyItemC(
                    center = KeyC("∮", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topLeft = KeyC("∓"),
                    bottomLeft = KeyC("∌"),
                    bottomRight = KeyC("∉"),
                ),
                KeyItemC(
                    center = KeyC("∄", size = LARGE),
                    swipeType = EIGHT_WAY,
                    right = KeyC("⊉"),
                    bottomRight = KeyC("⊅"),
                    bottom = KeyC("≠"),
                    bottomLeft = KeyC("∤"),
                ),
                EMOJI_KEY_ITEM,
            ),
            listOf(
                KeyItemC(
                    center = KeyC("∅", size = LARGE),
                    swipeType = EIGHT_WAY,
                ),
                KeyItemC(
                    center = KeyC("↯", size = LARGE),
                    bottomRight = KeyC("⇏"),
                    bottom = KeyC("⇎"),
                    bottomLeft = KeyC("⇍"),
                ),
                KeyItemC(
                    center = KeyC("¬", size = LARGE),
                    swipeType = EIGHT_WAY,
                    bottom =
                        KeyC(
                            display = KeyDisplay.IconDisplay(Icons.Outlined.ArrowDropDown),
                            action = ToggleShiftMode(false),
                            swipeReturnAction = ToggleCurrentWordCapitalization(false),
                            color = MUTED,
                        ),
                ),
                NUMERIC_KEY_ITEM,
            ),
            listOf(
                KeyItemC(
                    center = KeyC("↛", size = LARGE),
                    swipeType = EIGHT_WAY,
                    topLeft = KeyC("≁"),
                    left = KeyC("≰"),
                    bottomLeft = KeyC("≮"),
                ),
                KeyItemC(
                    center = KeyC("", size = LARGE),
                ),
                KeyItemC(
                    center = KeyC("", size = LARGE),
                    swipeType = EIGHT_WAY,
                    left = KeyC("≱"),
                    bottomLeft = KeyC("≯"),
                ),
                BACKSPACE_KEY_ITEM,
            ),
            listOf(
                SPACEBAR_KEY_ITEM,
                RETURN_KEY_ITEM,
            ),
        ),
    )

val KB_MATH_THUMBKEY: KeyboardDefinition =
    KeyboardDefinition(
        title = "math thumb-key",
        modes =
            KeyboardDefinitionModes(
                main = KB_MATH_THUMBKEY_MAIN,
                shifted = KB_MATH_THUMBKEY_SLASH,
                numeric = NUMERIC_KEYBOARD,
            ),
    )
