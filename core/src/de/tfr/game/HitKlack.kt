package de.tfr.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.tfr.game.lib.actor.Box2D
import de.tfr.game.lib.actor.Point2D
import de.tfr.game.model.GameField
import de.tfr.game.renderer.ControllerRenderer
import de.tfr.game.renderer.DisplayRenderer
import de.tfr.game.renderer.GameFieldRenderer
import de.tfr.game.renderer.LogoRenderer
import de.tfr.game.ui.DEVICE

/**
 * @author Tobse4Git@gmail.com
 */
class HitKlack : ApplicationAdapter() {

    data class Resolution(var width: Float, var height: Float) {
        fun getCenter() = Point2D(width / 2, height / 2)
    }

    private lateinit var camera: OrthographicCamera
    private lateinit var renderer: GameFieldRenderer
    private lateinit var controller: Controller
    private lateinit var viewport: Viewport
    private lateinit var display: Display
    private lateinit var displayRenderer: DisplayRenderer
    private lateinit var controllerRenderer: ControllerRenderer
    private lateinit var game: BoxGame
    private lateinit var logo: LogoRenderer

    private val gameField = GameField(10)
    private val resolution = Resolution(800f, 1400f)
    lateinit var batch: SpriteBatch
    private lateinit var stage: Stage
    private lateinit var shapeRenderer: ShapeRenderer

    override fun create() {
        shapeRenderer = ShapeRenderer()
        batch = SpriteBatch()
        game = BoxGame(gameField)
        camera = OrthographicCamera(resolution.width, resolution.height);
        camera.setToOrtho(false); //true to invert y axis

        viewport = FitViewport(resolution.width, resolution.height, camera)
        val center = resolution.getCenter()
        renderer = GameFieldRenderer(center, camera)
        val gameFieldSize = renderer.getFieldSize(gameField)
        controller = Controller(center, gameFieldSize, viewport)
        controller.addTouchListener(game)
        display = Display(Box2D(center, 280f, 90f))
        displayRenderer = DisplayRenderer(display, camera, SpriteBatch())

        displayRenderer.init()
        controllerRenderer = ControllerRenderer(camera)
        logo = LogoRenderer(center, camera, SpriteBatch(), gameFieldSize)
    }

    override fun render() {
        clear()
        camera.update();
        controllerRenderer.render(controller)
        renderField()
        game.update(graphics.deltaTime)
        displayRenderer.render()
        logo.render()
    }

    private fun renderField() {
        with(renderer) {
            start()
            render(game.field)
            game.getStones().forEach(renderer::renderStone)
            end()
        }
    }

    private fun clear() = clear(DEVICE)

    private fun clear(color: Color) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //   renderGameBackground()
    }

    private fun renderGameBackground() {
        shapeRenderer.setAutoShapeType(true)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.rect(0f, 0f, resolution.width, resolution.height)
        shapeRenderer.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true);
    }
}