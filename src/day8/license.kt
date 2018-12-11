package day8

import java.io.File

fun main() {
    val input = File("src/day8/input.txt").readText()

    val data = input.splitToSequence(' ').map { it.toInt() }

    println(sumMetadata(data.iterator()))
    println(nodeValue(data.iterator()))
}


fun sumMetadata(source: Iterator<Int>): Int {
    val childCount = source.next()
    val metadataCount = source.next()

    return (1..childCount).sumBy { sumMetadata(source) } + (1..metadataCount).sumBy { source.next() }
}

fun nodeValue(source: Iterator<Int>): Int {
    val childCount = source.next()
    val metadataCount = source.next()

    return if (childCount > 0) {
        val children = List(childCount) { nodeValue(source) }
        (1..metadataCount).sumBy { children.getOrNull(source.next() - 1) ?: 0 }
    } else {
        (1..metadataCount).sumBy { source.next() }
    }
}