package day6

import java.io.File
import kotlin.math.abs
import kotlin.random.Random

data class Site(val id: Char, val x: Int, val y: Int)
fun Site.distanceTo(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y)

fun indexName(index: Int): Char {
    val diffZA = 'Z' - 'A' + 1
    return if (index < diffZA) 'A' + index else 'a' + (index - diffZA)
}

fun main() {
    val input = File("src/day6/input.txt").readLines().shuffled(Random)

    val sites = input.mapIndexed { index, l -> l.split(", ").let { (x, y) -> Site(indexName(index), x.toInt(), y.toInt()) } }
//    sites.forEach(::println)

    val maxX = sites.maxBy { it.x }!!.x + 1
    val maxY = sites.maxBy { it.y }!!.y + 1

    val field = List(maxY + 1) { MutableList<Site?>(maxX + 1) { null } }

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            var nearestDistance = maxY + 1
            var nearestSite: Site? = null
            for (site in sites) {
                val distance = site.distanceTo(x,  y)
                if (distance < nearestDistance) {
                    nearestSite = site
                    nearestDistance = distance
                } else if (distance == nearestDistance) {
                    nearestSite = null
                }
            }
            field[y][x] = nearestSite
        }
    }

    val infiniteSites = sequence {
        (0..maxX).forEach { x -> yield(field[0][x]); yield(field[maxY][x]) }
        (0..maxY).forEach { y -> yield(field[y][0]); yield(field[y][maxX]) }
    }.filterNotNull().toSet()

    field.forEach { row ->
        println(row.joinToString("") { (it?.id ?: '.').toString() })
    }
    println("---")
    println("Infinite area sites: ${infiniteSites.map { it.id }}")

    val areas = field.asSequence().flatten().filterNotNull().filter { it !in infiniteSites }.groupingBy { it }.eachCount()
    areas.maxBy { it.value }.let(::println)

    val maxTotalDistance = 10000
    val safeArea = (0..maxY).sumBy { y ->
        (0..maxX).count { x ->
            sites.sumBy { it.distanceTo(x, y) } < maxTotalDistance
        }
    }
    println(safeArea)
}