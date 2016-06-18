package de.tfr.game.model

/**
 * @author Tobse4Git@gmail.com
 */
class Stone(private val initBlock: Block) {

    enum class State {Active, Set }

    var state = State.Active

    var block: Block = initBlock
        set(value) {
            field = value
        }

    fun freeze() {
        state = State.Set
    }

    override fun toString(): String {
        return "Stone[$state]"
    }
}