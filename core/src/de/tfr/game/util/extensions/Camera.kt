package de.tfr.game.util.extensions

import com.badlogic.gdx.graphics.OrthographicCamera
import de.tfr.game.HitKlack

/**
 * @author Tobse4Git@gmail.com
 */

/** Constructs a new OrthographicCamera, using the given viewport width and height. For pixel perfect 2D rendering just supply
 * the screen size, for other unit scales (e.g. meters for box2d) proceed accordingly. The camera will show the region
 * [-viewportWidth/2, -(viewportHeight/2-1)] - [(viewportWidth/2-1), viewportHeight/2]
 * @param resolution with the viewport width and height
 */
fun OrthographicCamera(resolution: HitKlack.Resolution): OrthographicCamera {
    return com.badlogic.gdx.graphics.OrthographicCamera(resolution.width, resolution.width)
}