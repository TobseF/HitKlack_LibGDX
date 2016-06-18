package de.tfr.game.model

import java.util.*

/**
 * @author Tobse4Git@gmail.com
 */
enum class Orientation { Left, Right, Up, Down;

    fun char() = this.name.first()

    companion object {
        private val random = Random()

        fun random() = values()[random.nextInt(values().size)]
    }
}