package de.tfr.game.util

/**
 * @author Tobse4Git@gmail.com
 */
class StopWatch(var start: ms = System.currentTimeMillis()) {

    typealias ms = Long

    fun getTime(): ms = System.currentTimeMillis() - start

}
