package de.tfr.game.lib.actor

/**
 * @author Tobse4Git@gmail.com
 */
class Box2D(point: Point, override var width: Float, override var height: Float) : Point2D(point.x, point.y), Box {

}