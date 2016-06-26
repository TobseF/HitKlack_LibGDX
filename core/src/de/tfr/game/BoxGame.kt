package de.tfr.game

import de.tfr.game.model.GameField
import de.tfr.game.model.Orientation
import de.tfr.game.model.Ring
import de.tfr.game.model.Stone
import de.tfr.game.util.Timer

/**
 * @author Tobse4Git@gmail.com
 */
class BoxGame(val field: GameField) : Controller.ControlListener {

    private var active: Stone
    private var activeRing: Ring? = null
    private val timer: Timer
    private val fallingSpeed = 0.3f
    private val firstPause = 0.7f

    init {
        active = Stone(field[field.size - 1][Orientation.Left])
        timer = Timer(firstPause, { doStep() })
    }

    private fun doStep() {
        timer.actionTime = fallingSpeed
        move(active)
    }

    override fun controlEvent(control: Controller.Control) {
        when (control) {
            mapOrientation(active.block.orientation) -> setStone()
            Controller.Control.Action -> setStone(active)
            Controller.Control.Esc -> reset()
            Controller.Control.Pause -> timer.togglePause()
        }
    }

    fun mapOrientation(orientation: Orientation): Controller.Control =
            when (orientation) {
                Orientation.Left -> Controller.Control.Left
                Orientation.Right -> Controller.Control.Right
                Orientation.Up -> Controller.Control.Top
                Orientation.Down -> Controller.Control.Bottom
            }


    fun getStones() = listOf(active)

    fun update(deltaTime: Float) {
        timer.update(deltaTime)
    }

    private fun reset() {
        field.reset()
        timer.reset()
        activeRing = null
        respawnStone()
    }

    private fun respawnStone() {
        val field = field[field.size - 1][randomFreeOrientation()]
        active = Stone(field)
        timer.reset()
        timer.actionTime = firstPause
    }

    private fun randomFreeOrientation(): Orientation {
        return activeRing?.randomFreeSide() ?: Orientation.random()
    }

    private fun move(stone: Stone) {
        if (stone.isInLastRow()) {
            misstep()
        } else {
            val next = field[active.block.row - 1][active.block.orientation]
            active.block = next
        }
    }

    private fun Stone.isInLastRow() = this.block.row == 0

    private fun setStone() {
        if (active.block.isEmpty()) {
            setStone(active)
        }
    }

    private fun setStone(stone: Stone) {
        active.block.stone = active
        stone.freeze()
        if (activeRing != null) {
            if (activeRing!!.isFull()) {
                activeRing = null
            } else if (activeRing?.index != active.block.row) {
                misstep()
            }
        } else {
            activeRing = field[active.block.row]
        }
        respawnStone()
    }

    private fun misstep() {
        activeRing?.reset()
        active.block.reset()
        activeRing = null
        resetLastFullRing()
        respawnStone()
    }

    private fun firstFull(): Ring? = field.find(Ring::isFull)

    private fun resetLastFullRing() {
        firstFull()?.reset()
    }

}
