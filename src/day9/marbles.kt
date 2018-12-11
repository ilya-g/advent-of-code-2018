package day9

import java.util.*


data class Marble(val value: Int)

fun <T> MutableList<T>.extractLast(): T = get(lastIndex).also { removeAt(lastIndex) }

data class MarbleGame(val players: Int, val highestMarble: Int) {

    inner class State {
        val scores = LongArray(players)
        val availableMarbles = (1..highestMarble).mapTo(mutableListOf(), ::Marble).apply { reverse() }

        val circle = LinkedList<Marble>()

        fun runGame() {
            circle.add(Marble(0))

            var currentPlayer = 0
            var position = circle.listIterator(0)

            fun moveClockwise(steps: Int) {
                repeat(steps) {
                    if (!position.hasNext()) position = circle.listIterator(0)
                    position.next()
                }
            }

            fun extractAtCounterClockwise(steps: Int): Marble {
                var last: Marble? = null
                repeat(steps) {
                    if (!position.hasPrevious()) position = circle.listIterator(circle.size)
                    last = position.previous()
                }
                position.remove()
                return last!!
            }

            while (availableMarbles.isNotEmpty()) {
                val marble = availableMarbles.extractLast()
                if (marble.value % 23 == 0) {
                    scores[currentPlayer] = scores[currentPlayer] + (marble.value + extractAtCounterClockwise(7).value)
                } else {
                    moveClockwise(2)
                    position.add(marble)
                    position.previous()
                }
                currentPlayer++
                currentPlayer %= players
            }
        }

    }
}


fun main() {
    val game = MarbleGame(players = 455, highestMarble = 7122300)
//    val game = MarbleGame(players = 9, highestMarble = 25)

    val state = game.State()
    state.runGame()
    println(state.scores.max())
}

