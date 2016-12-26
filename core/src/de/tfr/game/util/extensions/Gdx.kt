package de.tfr.game.util.extensions

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20

/**
 * @author Tobse4Git@gmail.com
 */
fun GL20.glClearColor(color: Color) {
    this.glClearColor(color.r, color.g, color.b, color.a)
}