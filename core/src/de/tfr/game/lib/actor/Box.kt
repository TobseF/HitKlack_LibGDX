package de.tfr.game.lib.actor

/**
 * @author Tobse4Git@gmail.com
 */
interface Box : Point {
    var width: Float
    var height: Float

    fun center() = Point2D(width / 2, height / 2)

}