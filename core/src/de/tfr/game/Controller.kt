package de.tfr.game

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.Controller.Control.*
import de.tfr.game.lib.actor.Point
import java.util.*


/**
 * @author Tobse4Git@gmail.com
 */
class Controller(point: Point, gameRadius: Float, val viewport: Viewport) : InputProcessor by InputAdapter(), Point by point {

    private val left: TouchArea
    private val right: TouchArea
    private val top: TouchArea
    private val bottom: TouchArea

    private val distance = 90f
    private val radius = 62f
    private val vibrateTime = 26

    private val touchListeners: MutableCollection<ControlListener> = ArrayList()

    enum class Control {Left, Right, Top, Bottom, Esc, Action, Pause }
    private class Button(centerX: Float, centerY: Float, radius: Float) : Rectangle(centerX - radius, centerY - radius, radius * 2, radius * 2)
    class TouchArea(val control: Control, val rect: Rectangle)

    interface ControlListener {
        fun controlEvent(control: Control)
    }

    class TouchPoint(inputIndex: Int) : Vector2(input.getX(inputIndex).toFloat(), input.getY(inputIndex).toFloat())

    fun isPressed(control: Control): Boolean {
        val touchPointers = getTouchPointers()
        fun touches(touchArea: TouchArea) = touchPointers.any(touchArea.rect::contains)
        when (control) {
            Left -> return input.isKeyPressed(Keys.LEFT) || touches(left)
            Right -> return input.isKeyPressed(Keys.RIGHT) || touches(right)
            Top -> return input.isKeyPressed(Keys.UP) || touches(top)
            Bottom -> return input.isKeyPressed(Keys.DOWN) || touches(bottom)
        }
        return false
    }

    private fun getTouchPointers() = (0..6).filter(input::isTouched).map { viewport.unproject(TouchPoint(it)) }.filter { !it.isZero }

    init {
        left = TouchArea(Left, Button(x - gameRadius - distance, y, radius))
        right = TouchArea(Right, Button(x + gameRadius + distance, y, radius))
        top = TouchArea(Top, Button(x, y + gameRadius + distance, radius))
        bottom = TouchArea(Bottom, Button(x, y - gameRadius - distance, radius))
        input.inputProcessor = this
        input.isCatchBackKey = true;
    }

    val touchAreas: List<TouchArea> by lazy {
        arrayListOf(left, right, top, bottom)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val worldCords = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        touchAreas.filter { it.rect.contains(worldCords.x, worldCords.y) }.forEach {
            doHapticFeedback()
            notifyListener(it.control)
        }
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        fun toControl(keycode: Int) =
                when (keycode) {
                    Keys.RIGHT -> Right
                    Keys.UP -> Top
                    Keys.DOWN -> Bottom
                    Keys.LEFT -> Left
                    Keys.SPACE -> Action
                    Keys.P -> Pause
                    Keys.ESCAPE, Keys.BACK -> Esc
                    else -> null
                }
        toControl(keycode)?.let(this::notifyListener)
        doHapticFeedback()
        return true
    }

    private fun doHapticFeedback() = input.vibrate(vibrateTime)

    fun addTouchListener(touchListener: ControlListener) = touchListeners.add(touchListener)

    private fun notifyListener(control: Control) = touchListeners.forEach { it.controlEvent(control) }

}