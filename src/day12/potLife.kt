package day12

import java.io.File

val IntRange.size: Int get() = endInclusive - start + 1

data class State(val start: Int, val data: List<Char>) {
    val lifeRange = data.indexOf('#') + start .. data.lastIndexOf('#') + start
    operator fun get(index: Int) = data.getOrElse(index - start) { '.' }
    override fun toString(): String = "State($start, ${data.joinToString("")})"
    fun stringStartingAt(position: Int) = buildString { (position..lifeRange.endInclusive).forEach { append(this@State[it]) } }
    fun sumAlivePositions() = lifeRange.sumBy { pos -> if (this[pos] == '#') pos else 0 }
    fun translationEqualTo(other: State) =
        this.lifeRange.size == other.lifeRange.size &&
        this.lifeRange.all { thisPos -> this[thisPos] == other[thisPos - this.lifeRange.start + other.lifeRange.start] }

}

data class Rule(val pattern: String, val result: Char)

fun Rule.matchIn(state: State, position: Int): Boolean =
    pattern.indices.all { pattern[it] == state[position + it - 2] }

fun State.next(rules: List<Rule>): State {
    val newStart = lifeRange.start - 2
    val newSize = lifeRange.size + 2 + 2
    val newData = List(newSize) { index -> rules.firstOrNull { rule -> rule.matchIn(this, index + newStart) }?.result ?: '.' }
    return State(newStart, newData)
}

fun main() {
    val input = File("src/day12/input.txt").readLines()

    val initial = input[0].substringAfter(": ")
    val rules = input.drop(2).map { it.split(" => ").let { (pattern, result) -> Rule(pattern, result.single()) } }.filter { it.result == '#' }

    var state = State(0, initial.toList())
    println(state.stringStartingAt(-5))

    for(iteration in 1..20) {
        state = state.next(rules)
        println("${iteration.toString().padEnd(5)}${state.stringStartingAt(-5)}")
    }

    println(state.sumAlivePositions())

    var iteration = 20
    do {
        iteration++
        val prev = state
        state = state.next(rules)
        println("${iteration.toString().padEnd(5)}${state.sumAlivePositions().toString().padEnd(6)}${state.stringStartingAt(-5)}")
    } while (!state.translationEqualTo(prev))

    val currentSum = state.sumAlivePositions()
    val nextSum = state.next(rules).sumAlivePositions()
    val totalIterations = 50_000_000_000L
    val totalAliveSum = currentSum + (totalIterations - iteration) * (nextSum - currentSum)
    println(totalAliveSum)
}