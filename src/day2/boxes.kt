package day2

import java.io.File

fun main() {
    val input = File("src/day2/input.txt").readLines()

    val lineLetterFreq = input.map { l -> l.groupingBy { it }.eachCount() }


    val countTriples = lineLetterFreq.count { it.containsValue(3) }
    val countDoubles = lineLetterFreq.count { it.containsValue(2) }

    println(countDoubles * countTriples)

    println(diffs("abcd", "abcd"))

    for (idx1 in input.indices) {
        for (idx2 in idx1 + 1 until input.size) {
            val line1 = input[idx1]
            val line2 = input[idx2]
            if (diffs(line1, line2) == 1) {
                println(commonChars(line1, line2))
            }
        }
    }
}

fun diffs(line1: String, line2: String): Int = line1.zip(line2) { c1, c2 -> c1 != c2 }.count { it }

fun commonChars(line1: String, line2: String): String = buildString {
    // note: need something 'x' to 'zip' as 'forEach' is to 'map'
    line1.zip(line2) { c1, c2 -> if (c1 == c2) append(c1) else Unit }
}

