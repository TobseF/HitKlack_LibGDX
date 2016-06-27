package de.tfr.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.GameField
import de.tfr.game.ui.DEVICE

/**
 * @author Tobse4Git@gmail.com
 */
class HitKlack : ApplicationAdapter() {

    data class Resolution(var width: Float, var height: Float) {
        fun getCenter() = Point2D(width / 2, height / 2)
    }

    internal lateinit var camera: OrthographicCamera
    private lateinit var renderer: Renderer
    private lateinit var controller: Controller
    private lateinit var viewport: Viewport

    private var game: BoxGame
    private val gameField = GameField(10)
    private val resolution = Resolution(800f, 800f)

    init {
        game = BoxGame(gameField)
    }

    override fun create() {
        camera = OrthographicCamera(resolution.height, resolution.height);
        camera.setToOrtho(false); //true to invert y axis
        viewport = FitViewport(resolution.height, resolution.height, camera)
        renderer = Renderer(resolution.getCenter(), camera)
        controller = Controller(resolution.getCenter(), renderer.getFieldSize(gameField), viewport)
        controller.addTouchListener(game)
    }

    override fun render() {
        clear()
        camera.update();
        renderField()
        game.update(graphics.deltaTime)
    }

    private fun renderField() {
        with(renderer) {
            start()
            render(game.field)
            renderTouchArea(controller.touchAreas.map { it.rect })
            game.getStones().forEach { renderStone(it) }
            end()
        }
    }

    private fun clear() = clear(DEVICE)

    private fun clear(color: Color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true);
    }
}