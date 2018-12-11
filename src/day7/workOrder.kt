package day7

import java.io.File
import java.util.*

data class Dep(val before: Char, val after: Char)

fun main() {
    val input = File("src/day7/input.txt").readLines()

    val stepNameRegex = Regex("\\b[A-Z]\\b")
    val deps = input.map { stepNameRegex.findAll(it).map { it.value }.toList().let { (b, a) -> Dep(b[0], a[0]) } }

    run part1@ {
        val allSteps = deps.flatMapTo(hashSetOf()) { listOf(it.before, it.after) }
        val remainingDeps = deps.toMutableList()
        val queue = sortedSetOf<Char>()

        while (true) {
            allSteps.filterTo(queue) { s -> remainingDeps.none { it.after == s } }
            val first = queue.firstOrNull() ?: break
            queue.remove(first)
            allSteps.remove(first)
            print(first)

            remainingDeps.removeAll { it.before == first }
        }
        println()
    }

    run part2@ {
        data class WorkItem(val name: Char, val readyAt: Int)
        val allSteps = deps.flatMapTo(hashSetOf()) { listOf(it.before, it.after) }
        val remainingDeps = deps.toMutableList()
        val maxInProgress = 5
        var currentTime = 0

        val workQueue = sortedSetOf<Char>()
        val readyQueue = PriorityQueue<WorkItem>(compareBy { it.readyAt })

        while (true) {
            allSteps.filterTo(workQueue) { s -> remainingDeps.none { it.after == s } }
            allSteps.removeAll(workQueue)
            val tasks = workQueue.take(maxInProgress - readyQueue.size).also { workQueue.removeAll(it) }
            tasks.mapTo(readyQueue) { WorkItem(it, currentTime + (60 + (it - 'A' + 1))) }

            currentTime = readyQueue.peek()?.readyAt ?: break
            val readyTasks = readyQueue.takeWhile { it.readyAt == currentTime }.also { readyQueue.removeAll(it) }.map { it.name }

            readyTasks.forEach(::print)
            remainingDeps.removeAll { it.before in readyTasks }
        }
        println()
        println(currentTime)
    }

}