package de.tfr.game.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import de.tfr.game.Display
import de.tfr.game.ui.GRAY_DARK
import de.tfr.game.ui.GREEN_LIGHT
import de.tfr.game.ui.GREEN_LIGHT2

/**
 * @author Tobse4Git@gmail.com
 */
class DisplayRenderer(val display: Display, val camera: Camera, val batch: SpriteBatch) {
    lateinit var font: BitmapFont

    lateinit var glyphLayout: GlyphLayout
    private var renderer = ShapeRenderer()
    var time = System.currentTimeMillis()

    fun init() {
        font = BitmapFont(Gdx.files.internal("fonts/segment7.fnt"))
        glyphLayout = GlyphLayout(font, "00 00")
    }

    fun begin() {

    }

    fun render() {

        renderer.projectionMatrix = camera.combined;
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.color = GREEN_LIGHT

        renderer.rect(display.x - display.width / 2, display.y - 510, display.width, display.height)
        renderer.end()

        batch.projectionMatrix = camera.combined;
        batch.begin();
        glyphLayout.setText(font, "88:88")
        font.color = GRAY_DARK
        font.draw(batch, glyphLayout, display.x - (glyphLayout.width / 2), display.y - 370 - glyphLayout.height / 2)
        glyphLayout.setText(font, display.getText())
        font.color = GREEN_LIGHT2
        font.draw(batch, glyphLayout, display.x - (glyphLayout.width / 2), display.y - 370 - glyphLayout.height / 2)
        batch.end()

        end()
        /*

         renderer.color = GREEN_LIGHT2
          renderer.setAutoShapeType(true)
          renderer.begin()
          renderer.set(ShapeRenderer.ShapeType.Filled)
          renderer.rect(display.x, display.y, display.width, display.height)
          renderer.end()
          batch.end()
          */
    }

    private fun end() {


    }


}
