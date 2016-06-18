package de.tfr.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled
import com.badlogic.gdx.math.Rectangle
import de.tfr.game.lib.actor.Point
import de.tfr.game.model.*
import de.tfr.game.ui.BLACK
import de.tfr.game.ui.GRAY_DARK
import de.tfr.game.ui.GREEN_LIGHT
import de.tfr.game.ui.GREEN_LIGHT2


/**
 * @author Tobse4Git@gmail.com
 */
class Renderer(var point: Point) : Point by point {

    private val gap = 4
    private val tick = 16f
    private val radius = 8f
    private var renderer = ShapeRenderer()

    fun start() {
        renderer.begin(Filled);
    }

    fun render(field: GameField) {
        renderBackground(field)

        renderer.color = GREEN_LIGHT2
        renderer.circle(x, y, radius)

        field.forEach { ring -> renderRing(ring) }
    }

    private fun renderBackground(field: GameField) {
        renderer.color = GREEN_LIGHT
        val radius = getFieldSize(field)
        renderer.rect(x - radius, y - radius, radius * 2, radius * 2)
    }

    fun getFieldSize(field: GameField): Float {
        return gap + tick + (field.size * (gap + tick))
    }

    fun end() {
        renderer.end();
    }

    private fun renderRing(ring: Ring) {
        ring.forEach { renderBlock(it, it.stone) }
    }

    fun renderTouchArea(touchAreas: List<Rectangle>) {
        renderer.color = Color.CYAN
        touchAreas.forEach { renderer.rect(it.x, it.y, it.width, it.height) }
    }

    fun renderStone(stone: Stone) {
        renderBlock(stone.block, stone)
    }

    private fun renderBlock(block: Block, stone: Stone?) {
        val distance = gap + tick + (block.row * (gap + tick))
        when (block.orientation) {
            Orientation.Left -> renderBlock(block, stone, x - distance, y)
            Orientation.Right -> renderBlock(block, stone, x + distance, y)
            Orientation.Up -> renderBlock(block, stone, x, y + distance)
            Orientation.Down -> renderBlock(block, stone, x, y - distance)
        }
    }

    private fun renderBlock(block: Block, stone: Stone?, x: Float, y: Float) {
        renderer.rect(x, y, 1f, 1f)
        val length = ((block.row) * (tick * 2)) + ((2 * gap) * (block.row + 1))
        val side = length / 2
        val width = tick / 2
        when {
            stone == null -> renderer.color = GREEN_LIGHT2
            stone.state == Stone.State.Active -> renderer.color = BLACK
            stone.state == Stone.State.Set -> renderer.color = GRAY_DARK
        }

        when (block.orientation) {
            Orientation.Left -> renderer.rect(x - width, y - side, tick, length)
            Orientation.Right -> renderer.rect(x - width, y - side, tick, length)
            Orientation.Up -> renderer.rect(x - side, y - width, length, tick)
            Orientation.Down -> renderer.rect(x - side, y - width, length, tick)
        }

        when (block.orientation) {
            Orientation.Left -> {
                renderTriangleLeftUp(x - width, y + side, tick)
                renderTriangleLeftDown(x - width, y - side, tick)
            }
            Orientation.Right -> {
                renderTriangleReightUp(x - width, y + side, tick)
                renderTriangleReightDown(x - width, y - side, tick)
            }

            Orientation.Up -> {
                renderTriangleUpLeft(x - side, y - width, tick)
                renderTriangleUpRight(x + side, y - width, tick)
            }
            Orientation.Down -> {
                renderTriangleDownLeft(x - side, y - width, tick)
                renderTriangleDownRight(x + side, y - width, tick)
            }

        }
    }

    private fun renderTriangleLeftUp(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x, y + side, x + side, y)
    }

    private fun renderTriangleLeftDown(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x, y - side, x + side, y)
    }

    private fun renderTriangleReightUp(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x + side, y + side, x + side, y)
    }

    private fun renderTriangleReightDown(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x + side, y - side, x + side, y)
    }

    private fun renderTriangleUpLeft(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x, y + side, x - side, y + side)
    }

    private fun renderTriangleUpRight(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x, y + side, x + side, y + side)
    }

    private fun renderTriangleDownLeft(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x, y + side, x - side, y)
    }

    private fun renderTriangleDownRight(x: Float, y: Float, side: Float) {
        renderer.triangle(x, y, x, y + side, x + side, y)
    }


}