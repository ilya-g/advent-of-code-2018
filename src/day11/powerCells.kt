package day11

const val gridSerial = 9995

data class Cell(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
}
data class BlockTotalPower(val topLeft: Cell, val size: Int, val totalPower: Int)

class Grid(val serial: Int) {
    val gridSize = 300

    fun cellPower(x: Int, y: Int) = ((x + 10) * y + serial) * (x + 10) / 100 % 10 - 5
    fun blockPower(x: Int, y: Int, size: Int) = (0 until size).sumBy { dy -> (0 until size).sumBy { dx -> cellPower(x + dx, y + dy) }}


    fun maxBlockPower(blockSize: Int): BlockTotalPower {
        val blockIndices = 1..gridSize - blockSize + 1
        val blockTopLeftCells = blockIndices.asSequence().flatMap { y -> blockIndices.asSequence().map { x -> Cell(x, y) } }
        val blockPowers = blockTopLeftCells.map { it to blockPower(it.x, it.y, blockSize) }
        return blockPowers.maxBy { it.second }!!.let { (cell, power) -> BlockTotalPower(cell, blockSize, power) }
    }


    private val blockPowerMap: Array<Array<IntArray>?> = arrayOfNulls(gridSize + 1)

    private fun fillBlockPowerMap(blockSize: Int): Array<IntArray> {
        val mapSize = gridSize - blockSize + 1
        val map: Array<IntArray>
        if (blockSize == 1) {
            map = Array(mapSize) { yi -> IntArray(mapSize) { xi -> cellPower(xi + 1, yi + 1) }}
        } else {
            val prevMap = blockPowerMap[blockSize - 1] ?: fillBlockPowerMap(blockSize - 1)
            map = Array(mapSize) { yi ->
                IntArray(mapSize) { xi ->
                    prevMap[yi][xi] +
                            (1..blockSize).sumBy { dx -> cellPower(xi + dx, yi + blockSize) } +
                            (1 until blockSize).sumBy { dy -> cellPower(xi + blockSize, yi + dy) }
                }
            }
        }
        blockPowerMap[blockSize] = map
        return map
    }

    fun maxBlockPowerCached(blockSize: Int): BlockTotalPower {
        val powerMap = blockPowerMap[blockSize] ?: fillBlockPowerMap(blockSize)
        val blockIndices = 1..gridSize - blockSize + 1
        val blockTopLeftCells = blockIndices.asSequence().flatMap { y -> blockIndices.asSequence().map { x -> Cell(x, y) } }
        val blockPowers = blockTopLeftCells.map { it to powerMap[it.y - 1][it.x - 1] }
        return blockPowers.maxBy { it.second }!!.let { (cell, power) -> BlockTotalPower(cell, blockSize, power) }
    }
}


fun main() {
    val grid = Grid(gridSerial)

    println(grid.maxBlockPowerCached(3))

    (1..300).asSequence()
        .map { size -> grid.maxBlockPowerCached(size) }
        .onEach { with(it) { println("size $size: max block at $topLeft with power $totalPower") } }
        .maxBy { it.totalPower }!!
        .run { println("${topLeft.x},${topLeft.y},$size - $totalPower") }
}
