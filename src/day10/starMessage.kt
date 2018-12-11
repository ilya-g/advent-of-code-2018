package day10

import java.io.File

data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
}

data class Point(val position: Vector, val velocity: Vector)

fun List<Point>.boundingBox(): Pair<Vector, Vector> {
    val seq = asSequence()
    val xs = seq.map { it.position.x }
    val ys = seq.map { it.position.y }
    return Vector(xs.min()!!, ys.min()!!) to Vector(xs.max()!!, ys.max()!!)

}
fun List<Point>.boundingBoxSize(): Int = boundingBox().let { (min, max) -> (max.x - min.x + 1) + (max.y - min.y + 1) }

fun drawPoints(points: List<Point>): String {
    val (topLeft, rightBottom) = points.boundingBox()
    val dx = rightBottom.x - topLeft.x + 1
    val dy = rightBottom.y - topLeft.y + 1

    val field = Array(dy) { CharArray(dx) { '.' } }
    for (point in points) {
        field[point.position.y - topLeft.y][point.position.x - topLeft.x] = '#'
    }

    return field.joinToString("\n") { it.joinToString("") }
}

fun main() {
    val input = File("src/day10/input.txt").readLines()

    val numberRegex = Regex("-?\\d+")
    var points = input.map {
        numberRegex.findAll(it).map(MatchResult::value).toList().let { (x, y, vx, vy) ->
            Point(Vector(x.toInt(), y.toInt()), Vector(vx.toInt(), vy.toInt()))
        }
    }

    var prevBoundingBox = points.boundingBoxSize().also(::println)
    run loop@{
        var time = 0
        while(true) {
            time++
            val newPoints = points.map { it.copy(position = it.position + it.velocity) }
            val boundingBox = newPoints.boundingBoxSize()
            println("at $time: $boundingBox")
            if (boundingBox < prevBoundingBox) {
                prevBoundingBox = boundingBox
                points = newPoints
            } else {
                return@loop points
            }
        }
    }

    println(drawPoints(points))
}

