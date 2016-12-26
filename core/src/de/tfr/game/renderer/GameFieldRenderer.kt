package de.tfr.game.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import de.tfr.game.lib.actor.Point
import de.tfr.game.model.*
import de.tfr.game.ui.BLACK
import de.tfr.game.ui.GRAY_DARK
import de.tfr.game.ui.GREEN_LIGHT
import de.tfr.game.ui.GREEN_LIGHT2


/**
 * @author Tobse4Git@gmail.com
 */
class GameFieldRenderer(point: Point, val camera: Camera) : Point by point {

    private val gap = 6
    private val blockWith = 18f
    private val radius = 8f
    private val renderer = ShapeRenderer()

    fun start() {
        renderer.projectionMatrix = camera.combined;
        renderer.begin(Filled);
    }

    fun render(field: GameField) {
        renderBackground(field)

        renderer.color = GREEN_LIGHT2
        renderer.circle(x, y, radius)

        field.forEach(this::renderRing)
    }

    private fun renderBackground(field: GameField) {
        renderer.color = GREEN_LIGHT
        val radius = getFieldSize(field)
        renderer.rect(x - radius, y - radius, radius * 2, radius * 2)
    }

    fun getFieldSize(field: GameField): Float = (blockWith / 2) + field.size * (gap + blockWith)

    fun end() {
        renderer.end();
    }

    private fun renderRing(ring: Ring) {
        ring.forEach { renderBlock(it, it.stone) }
    }

    fun renderTouchArea(touchAreas: List<Rectangle>) {
        renderer.color = Color.NAVY

        touchAreas.forEach {
            val center = it.getCenter(Vector2())
            renderer.circle(center.x, center.y, it.width / 2)
        }
    }

    fun renderStone(stone: Stone) {
        renderBlock(stone.block, stone)
    }

    private fun renderBlock(block: Block, stone: Stone?) {
        val distance = gap + blockWith + (block.row * (gap + blockWith))
        when (block.orientation) {
            Orientation.Left -> renderBlock(block, stone, x - distance, y)
            Orientation.Right -> renderBlock(block, stone, x + distance, y)
            Orientation.Up -> renderBlock(block, stone, x, y + distance)
            Orientation.Down -> renderBlock(block, stone, x, y - distance)
        }
    }

    private fun renderBlock(block: Block, stone: Stone?, x: Float, y: Float) {
        val length = ((block.row) * (blockWith * 2)) + ((2 * gap) * (block.row + 1))
        val side = length / 2
        val width = blockWith / 2
        when {
            stone == null -> renderer.color = GREEN_LIGHT2
            stone.state == Stone.State.Active -> renderer.color = BLACK
            stone.state == Stone.State.Set -> renderer.color = GRAY_DARK
        }

        when (block.orientation) {
            Orientation.Left -> renderer.rect(x - width, y - side, blockWith, length)
            Orientation.Right -> renderer.rect(x - width, y - side, blockWith, length)
            Orientation.Up -> renderer.rect(x - side, y - width, length, blockWith)
            Orientation.Down -> renderer.rect(x - side, y - width, length, blockWith)
        }

        fun renderTriangleLeftUp(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x + blockWith, y)
        fun renderTriangleLeftDown(x: Float, y: Float) = renderer.triangle(x, y, x, y - blockWith, x + blockWith, y)
        fun renderTriangleRightUp(x: Float, y: Float) = renderer.triangle(x, y, x + blockWith, y + blockWith, x + blockWith, y)
        fun renderTriangleRightDown(x: Float, y: Float) = renderer.triangle(x, y, x + blockWith, y - blockWith, x + blockWith, y)
        fun renderTriangleUpLeft(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x - blockWith, y + blockWith)
        fun renderTriangleUpRight(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x + blockWith, y + blockWith)
        fun renderTriangleDownLeft(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x - blockWith, y)
        fun renderTriangleDownRight(x: Float, y: Float) = renderer.triangle(x, y, x, y + blockWith, x + blockWith, y)

        when (block.orientation) {
            Orientation.Left -> {
                renderTriangleLeftUp(x - width, y + side)
                renderTriangleLeftDown(x - width, y - side)
            }
            Orientation.Right -> {
                renderTriangleRightUp(x - width, y + side)
                renderTriangleRightDown(x - width, y - side)
            }
            Orientation.Up -> {
                renderTriangleUpLeft(x - side, y - width)
                renderTriangleUpRight(x + side, y - width)
            }
            Orientation.Down -> {
                renderTriangleDownLeft(x - side, y - width)
                renderTriangleDownRight(x + side, y - width)
            }

        }
    }

}