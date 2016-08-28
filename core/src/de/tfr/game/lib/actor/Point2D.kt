package de.tfr.game.lib.actor

/**
 * @author Tobse4Git@gmail.com
 */
open class Point2D(override var x: Float, override var y: Float) : Point {
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())
}
