package de.tfr.game.model


import java.util.*

/**
 * @author Tobse4Git@gmail.com
 */
class Ring(val index: Int) : Iterable<Block> {

    override fun iterator() = blocks.iterator()

    private var blocks: Array<Block> = Array(4, { i -> Block(index, Orientation.values()[i]) })

    operator fun get(orientation: Orientation) = blocks[orientation.ordinal]

    fun reset() = blocks.forEach { it.reset() }

    fun size() = blocks.filter { it.isTaken() }.count()

    fun isFull() = size() == 4

    fun freeSides(): List<Orientation> {
        return blocks.filter { it.isEmpty() }.map { it.orientation }.toList()
    }

    fun randomFreeSide(): Orientation? {
        val freeSides = freeSides()
        if (freeSides.isEmpty()) {
            return null;
        }
        return freeSides[Random().nextInt(freeSides.size)]
    }

    override fun toString(): String {
        return "Ring:$index {${blocks.map { it.orientation.char() + (if (it.isEmpty()) "[_]" else "[X]") }.joinToString(",")}}"
    }

}