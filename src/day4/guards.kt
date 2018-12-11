package day4

import java.io.File

fun main() {
    val input = File("src/day4/input.txt").readLines().sorted()

    val g1 = Guard(10).apply {
        sleepyMinutes += 5 until 25
        sleepyMinutes += 30 until 55
        sleepyMinutes += 24 until 29
    }
    val g2 = Guard(99).apply {
        sleepyMinutes += 40 until 50
        sleepyMinutes += 36 until 46
        sleepyMinutes += 45 until 55
    }
    for (g in listOf(g1, g2)) {
        println("$g: total minutes: ${g.totalMinutes()}, most asleep at: ${g.minuteMostAsleep()}")
    }
    println("-----------")


    val guards = mutableMapOf<Int, Guard>()

    var currentId = -1
    var currentFallAt = -1
    for (line in input) {
        val (time, action) = line.split("] ")
        val minutes = time.takeLast(2).toInt()
        when {
            action.any { it.isDigit() } -> {
                currentId = action.filter { it.isDigit() }.toInt()
                currentFallAt = -1
            }
            action == "falls asleep" -> {
                currentFallAt = minutes
            }
            action == "wakes up" -> {
                check(currentId > 0)
                check(currentFallAt >= 0)
                val guard = guards.computeIfAbsent(currentId, ::Guard)
                guard.sleepyMinutes += currentFallAt until minutes
                currentFallAt = -1
            }
        }
    }
    guards.values.forEach { g ->
        println("$g: total minutes: ${g.totalMinutes()}, most asleep at: ${g.minuteMostAsleep()}")
    }

    guards.values.maxBy { it.totalMinutes() }!!.let { sleepsMost ->
        println("$sleepsMost sleeps most (total ${sleepsMost.totalMinutes()}), most asleep at ${sleepsMost.minuteMostAsleep()}")
        println(sleepsMost.id * sleepsMost.minuteMostAsleep())
    }

    guards.values.maxBy { it.minuteMostAsleepTimes() }!!.let { sleepsMost ->
        println("$sleepsMost sleeps most (${sleepsMost.minuteMostAsleepTimes()} times), most asleep at ${sleepsMost.minuteMostAsleep()}")
        println(sleepsMost.id * sleepsMost.minuteMostAsleep())
    }

//    input.sorted().forEach(::println)
}

data class Guard(val id: Int) {
    val sleepyMinutes = mutableListOf<IntRange>()

    fun totalMinutes() = sleepyMinutes.sumBy { it.endInclusive - it.start + 1 }
    fun minuteMostAsleep() = mostAsleepAt().key
    fun minuteMostAsleepTimes() = mostAsleepAt().value

    private fun mostAsleepAt() = sleepyMinutes.asSequence().flatten().groupingBy { it }.eachCount().maxBy { it.value }!!
}