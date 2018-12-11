package day1

import java.io.File

fun main() {
    val input = File("src/day1/input.txt").useLines { it.map { l -> l.toInt() }.toList() }

    val result = input.sum().also(::println)

    val cycled = sequence { while(true) yieldAll(input) }
    println(cycled.take(20).toList())

    val freqs = cycled.runningFold(0, Int::plus)
    println(freqs.take(20).toList())

    val observed = mutableSetOf<Int>()
    val freqObservedTwice = freqs.first { !observed.add(it) }
    println(freqObservedTwice)
}

fun <T, R> Sequence<T>.runningFold(initial: R, operation: (acc: R, T) -> R): Sequence<R> = sequence {
    var acc = initial
    yield(acc)
    for (e in this@runningFold) {
        acc = operation(acc, e)
        yield(acc)
    }
}