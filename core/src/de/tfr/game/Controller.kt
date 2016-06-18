package de.tfr.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import de.tfr.game.Controller.Control.*
import de.tfr.game.lib.actor.Point
import java.util.*


/**
 * @author Tobse4Git@gmail.com
 */
class Controller(point: Point, gameRadius: Float, val camera: Camera) : InputAdapter(), Point by point {

    private val left: TouchArea
    private val right: TouchArea
    private val top: TouchArea
    private val bottom: TouchArea

    private val size = 150f

    private val touchListeners: MutableCollection<ControlListener> = ArrayList()

    enum class Control {Left, Right, Top, Bottom, Esc, Action, Pause }

    class TouchArea(val control: Control, val rect: Rectangle)

    interface ControlListener {
        fun controlEvent(control: Control)
    }

    init {
        left = TouchArea(Left, Rectangle(x - gameRadius - size, y - gameRadius, size, gameRadius * 2))
        right = TouchArea(Right, Rectangle(x + gameRadius, y - gameRadius, size, gameRadius * 2))
        top = TouchArea(Top, Rectangle(x - gameRadius, y + gameRadius, gameRadius * 2, size))
        bottom = TouchArea(Bottom, Rectangle(x - gameRadius, y - gameRadius - size, gameRadius * 2, size))
        Gdx.input.inputProcessor = this
    }

    val touchAreas: List<TouchArea> by lazy {
        arrayListOf(left, right, top, bottom)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val projection = Vector3(screenX.toFloat(), screenY.toFloat(), 0f)
        val coordinates = camera.unproject(projection)
        touchAreas.filter { it.rect.contains(coordinates.x, coordinates.y) }.forEach { notifyListener(it.control) }
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.RIGHT ->
                notifyListener(Right)
            Input.Keys.UP ->
                notifyListener(Top)
            Input.Keys.DOWN ->
                notifyListener(Bottom)
            Input.Keys.LEFT ->
                notifyListener(Left)
            Input.Keys.SPACE -> {
                notifyListener(Action)
            }
            Input.Keys.P -> {
                notifyListener(Pause)
            }
        }
        return true
    }

    fun addTouchListener(touchListener: ControlListener) {
        touchListeners.add(touchListener)
    }

    private fun notifyListener(control: Control) {
        touchListeners.forEach { it.controlEvent(control) }
    }
}