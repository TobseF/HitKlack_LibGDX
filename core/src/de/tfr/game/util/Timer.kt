package de.tfr.game.util

/**
 * Created by tobse on 19.06.2016.
 */

class Timer(var actionTime: Float, val timerAction: () -> Unit) {
    var time: Float = 0f
    private var pause = false

    fun update(deltaTime: Float) {
        time += deltaTime
        if (!pause && time >= actionTime) {
            time = 0f
            timerAction.invoke()
        }
    }

    fun togglePause() {
        pause = !pause
    }

    fun reset() {
        time = 0f
    }

}
