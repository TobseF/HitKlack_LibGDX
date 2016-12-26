package de.tfr.game.util

import com.badlogic.gdx.graphics.Color

/**
 * @author Tobse4Git@gmail.com
 */

fun rgbColor(r: Int, g: Int, b: Int): Color {
    return Color(r / 255f, g / 255f, b / 255f, 1f)
}
