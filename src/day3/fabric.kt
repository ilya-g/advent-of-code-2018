package day3

import java.io.File

fun main() {
    val input = File("src/day3/input.txt").readLines()
    val claims = input.map(::parseClaim)


    val fabric = Fabric(1000, 1000)
    claims.forEach(fabric::accountClaim)

    println(fabric.countOverlappingInches())
    println(fabric.nonOverlappedClaims)
}

data class Claim(val id: Int, val left: Int, val top: Int, val width: Int, val height: Int) {
    init {
        require(id > 0)
    }
}

val number = Regex("\\d+") // something as persisted scope of a function would be nice
fun parseClaim(claimString: String): Claim {
    val (id, l, t, w, h) = number.findAll(claimString).map { it.value.toInt() }.toList()
    return Claim(id, l, t, w, h)
}

class Fabric(val width: Int, val height: Int) {
    private val inches = IntArray(width * height)
    val nonOverlappedClaims = mutableSetOf<Int>()

    fun accountClaim(claim: Claim) {
        val l = claim.left.also { require(it in 0 until width) }
        val t = claim.top.also { require(it in 0 until height) }
        val r = (l + claim.width - 1).also { require(it in 0 until width) }
        val b = (t + claim.height - 1).also { require(it in 0 until height) }
        nonOverlappedClaims.add(claim.id)
        for (x in l..r)
            for (y in t..b) {
                val index = y * width + x
                inches[index] = when (val prevClaim = inches[index]) {
                    0 -> claim.id
                    else -> {
                        nonOverlappedClaims.remove(claim.id)
                        if (prevClaim > 0) nonOverlappedClaims.remove(prevClaim)
                        -1
                    }
                }
            }
    }

    fun countOverlappingInches() = inches.count { it < 0 }
}