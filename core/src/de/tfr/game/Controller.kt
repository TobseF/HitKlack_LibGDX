package de.tfr.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.Controller.Control.*
import de.tfr.game.lib.actor.Point
import java.util.*


/**
 * @author Tobse4Git@gmail.com
 */
class Controller(point: Point, gameRadius: Float, val viewport: Viewport) : InputAdapter(), Point by point {

    private val left: TouchArea
    private val right: TouchArea
    private val top: TouchArea
    private val bottom: TouchArea

    private val distance = 90f
    private val radius = 50f

    private val touchListeners: MutableCollection<ControlListener> = ArrayList()

    enum class Control {Left, Right, Top, Bottom, Esc, Action, Pause }

    private class Button(centerX: Float, centerY: Float, radius: Float) : Rectangle(centerX - radius, centerY - radius, radius * 2, radius * 2)
    class TouchArea(val control: Control, val rect: Rectangle)

    interface ControlListener {
        fun controlEvent(control: Control)
    }

    init {
        left = TouchArea(Left, Button(x - gameRadius - distance, y, radius))
        right = TouchArea(Right, Button(x + gameRadius + distance, y, radius))
        top = TouchArea(Top, Button(x, y + gameRadius + distance, radius))
        bottom = TouchArea(Bottom, Button(x, y - gameRadius - distance, radius))
        Gdx.input.inputProcessor = this
    }


    val touchAreas: List<TouchArea> by lazy {
        arrayListOf(left, right, top, bottom)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val worldCords = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        touchAreas.filter { it.rect.contains(worldCords.x, worldCords.y) }.forEach { notifyListener(it.control) }
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