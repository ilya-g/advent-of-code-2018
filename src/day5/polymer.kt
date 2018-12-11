package day5

import java.io.File
import java.util.*

fun main() {
    val input = File("src/day5/input.txt").readText()

    val polymer = input.asSequence()

    println(react2(polymer))

    val elements = polymer.distinctBy { it.toLowerCase() }

    elements.map { e -> e to react2(polymer.filterNot { it.equals(e, ignoreCase = true) }) }
        .onEach { (e, s) -> println("Filtered '${e.toLowerCase()}': reacted size $s") }
        .minBy { (_, s) -> s }!!
        .let(::println)
}


fun react(polymer: Sequence<Char>): Int {
    val elements = polymer.toCollection(LinkedList())
    val index = elements.listIterator()
    while (index.hasNext()) {
        val current = index.next()
        if (!index.hasNext()) break
        val next = index.next()

        if (current.equals(next, ignoreCase = true) && current != next) {
            index.remove()
            index.previous()
            index.remove()
        }
        if (index.hasPrevious()) index.previous()
    }
    return elements.size
}

fun react2(polymer: Sequence<Char>): Int {
    val result = mutableListOf<Char>()
    polymer.forEach { e ->
        if (result.isNotEmpty() && result.last().let { p -> p.equals(e, ignoreCase = true) && p != e })
            result.removeAt(result.lastIndex)
        else
            result.add(e)
    }
    return result.size
}