package de.tfr.game.util

/**
 * @author Tobse4Git@gmail.com
 */
class Timer(var actionTime: Float, val timerAction: () -> Unit) : Time {

    override var time = 0F
    private var pause = false

    fun update(deltaTime: Float) {
        time += deltaTime
        if (!pause && time >= actionTime) {
            time = 0F
            timerAction.invoke()
        }
    }

    fun togglePause() {
        pause = !pause
    }

    fun reset() {
        time = 0F
    }

}
