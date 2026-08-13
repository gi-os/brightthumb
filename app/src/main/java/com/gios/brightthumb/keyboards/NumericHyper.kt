@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.gios.brightthumb.keyboards

import com.gios.brightthumb.utils.*
import com.gios.brightthumb.utils.ColorVariant.*
import com.gios.brightthumb.utils.FontSizeVariant.*
import com.gios.brightthumb.utils.KeyAction.*
import com.gios.brightthumb.utils.SwipeNWay.*

val HYPER_NUMERIC_KEYBOARD =
    KeyboardC(
        listOf(
            listOf(
                RETURN_KEY_ITEM,
                KeyItemC(
                    center = KeyC("1", size = LARGE),
                    top = KeyC(":(", size = SMALL),
                    left = KeyC("["),
                    bottomRight = KeyC("<"),
                    right = KeyC("\\"),
                ),
                KeyItemC(
                    center = KeyC("2", size = LARGE),
                    bottom = KeyC("@"),
                ),
                KeyItemC(
                    center = KeyC("3", size = LARGE),
                    top = KeyC(":)", size = SMALL),
                    left = KeyC("|"),
                    right = KeyC("]"),
                    bottomLeft = KeyC(">"),
                ),
                KeyItemC(
                    center = KeyC(".", size = LARGE),
                    bottom = KeyC(","),
                    left = KeyC("!"),
                ),
                BACKSPACE_KEY_ITEM,
            ),
            listOf(
                SPACEBAR_SKINNY_KEY_ITEM,
                KeyItemC(
                    center = KeyC("4", size = LARGE),
                    top = KeyC("\""),
                    bottom = KeyC(":"),
                    left = KeyC("("),
                    right = KeyC("_"),
                ),
                KeyItemC(
                    center = KeyC("5", size = LARGE),
                    left = KeyC("€"),
                    right = KeyC("£"),
                    top = KeyC("$"),
                    bottom = KeyC("&"),
                ),
                KeyItemC(
                    center = KeyC("6", size = LARGE),
                    top = KeyC("'"),
                    bottom = KeyC(";"),
                    left = KeyC("^"),
                    right = KeyC(")"),
                ),
                KeyItemC(
                    center = KeyC("0", size = LARGE),
                    left = KeyC("%"),
                ),
                SPACEBAR_SKINNY_KEY_ITEM,
            ),
            listOf(
                ABC_KEY_ITEM,
                KeyItemC(
                    center = KeyC("7", size = LARGE),
                    topRight = KeyC("?"),
                    top = KeyC("~"),
                    left = KeyC("{"),
                ),
                KeyItemC(
                    center = KeyC("8", size = LARGE),
                    topLeft = KeyC("`"),
                    topRight = KeyC("´"),
                ),
                KeyItemC(
                    center = KeyC("9", size = LARGE),
                    right = KeyC("}"),
                    top = KeyC("°"),
                    topLeft = KeyC("#"),
                ),
                KeyItemC(
                    center = KeyC("="),
                    top = KeyC("+"),
                    bottom = KeyC("-"),
                    left = KeyC("*"),
                    right = KeyC("/"),
                ),
                EMOJI_KEY_ITEM,
            ),
        ),
    )
