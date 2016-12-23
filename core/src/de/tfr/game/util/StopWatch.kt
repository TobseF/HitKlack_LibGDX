package de.tfr.game.util

/**
 * @author Tobse4Git@gmail.com
 */
class StopWatch(var start: ms = System.currentTimeMillis()) {

    fun getTime(): ms = System.currentTimeMillis() - start
}
