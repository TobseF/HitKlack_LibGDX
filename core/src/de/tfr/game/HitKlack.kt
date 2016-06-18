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

class HitKlack : ApplicationAdapter() {

    data class Resolution(var width: Float, var height: Float)

    internal lateinit var camera: OrthographicCamera
    private lateinit var renderer: Renderer
    private lateinit var controller: Controller
    private lateinit var viewport: Viewport

    private var game: BoxGame
    private val gameField = GameField(10)
    private val resolution = Resolution(1920f, 1080f)

    init {
        game = BoxGame(gameField)
    }

    override fun create() {
        camera = OrthographicCamera(resolution.width, resolution.height);
        camera.setToOrtho(false); //true to invert y axis
        viewport = FitViewport(resolution.width, resolution.height, camera)
        val center = Point2D((resolution.width / 2) - 500, (resolution.height / 2) - 150)
        renderer = Renderer(center)
        controller = Controller(center, renderer.getFieldSize(gameField), camera)
        controller.addTouchListener(game)
    }

    override fun render() {
        clear()
        camera.update();
        renderField()
        game.update(graphics.deltaTime)
    }

    private fun renderField() {
        renderer.start()
        renderer.render(game.field)
        renderer.renderTouchArea(controller.touchAreas.map { it.rect })
        game.getStones().forEach { renderer.renderStone(it) }
        renderer.end()
    }

    private fun clear() {
        clear(DEVICE)
    }

    private fun clear(color: Color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height);
        camera.position.set(0f, 0f, 0f)
    }
}