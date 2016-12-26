package de.tfr.game.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import de.tfr.game.Controller
import de.tfr.game.Controller.Control

/**
 * @author Tobse4Git@gmail.com
 */
class ControllerRenderer(val camera: Camera) {

    private val buttons: SpriteSheet
    private val red: ButtonTexture
    private val blue: ButtonTexture
    private val yellow: ButtonTexture
    private val green: ButtonTexture
    private val batch: SpriteBatch = SpriteBatch()
    private val width = 120

    private class ButtonTexture(val normal: TextureRegion, val pressed: TextureRegion)

    init {
        val texture = Texture(Gdx.files.internal("buttons.png"))
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        buttons = SpriteSheet(texture, width, width, 2, 4)
        green = ButtonTexture(buttons[0], buttons[1])
        blue = ButtonTexture(buttons[2], buttons[3])
        yellow = ButtonTexture(buttons[4], buttons[5])
        red = ButtonTexture(buttons[6], buttons[7])
    }

    fun render(controller: Controller) {
        batch.projectionMatrix = camera.combined
        batch.begin()

        val radius = width / 2F
        fun draw(textureRegion: TextureRegion, touchArea: Controller.TouchArea) {
            val pos = touchArea.rect.getCenter(Vector2()).sub(radius, radius)
            batch.draw(textureRegion, pos.x, pos.y)
        }

        fun button(button: ButtonTexture, control: Control): TextureRegion {
            return if (controller.isPressed(control)) button.pressed else button.normal
        }


        draw(button(red, Control.Left), controller.left)
        draw(button(blue, Control.Right), controller.right)
        draw(button(yellow, Control.Bottom), controller.bottom)
        draw(button(green, Control.Top), controller.top)

        batch.end()
    }

    class SpriteSheet(val texture: Texture, val width: Int, val height: Int, val horizontalCount: Int, val verticalCount: Int) {
        private val regions: Array<TextureRegion> = Array(numTiles(), this::getTile)

        fun numTiles() = horizontalCount * verticalCount

        operator fun get(index: Int) = regions[index]

        fun getTile(index: Int): TextureRegion {
            val y = if (index > 0) index / horizontalCount else 0
            val x = index - (y * horizontalCount)
            return getTile(x, y)
        }

        private fun getTile(x: Int, y: Int) = TextureRegion(texture, x * width, y * height, width, height)
    }
}