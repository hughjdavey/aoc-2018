package days

class Day11 : Day(11) {

    override fun partOne(): Any {
        val grid = FuelCell.getFuelCellGrid()
        return highestPowerInGrid(grid, inputString.trim().toInt())
    }

    override fun partTwo(): Any {
        return -1
    }

    data class FuelCell(val coord: Pair<Int, Int>) {
        private val rackId = coord.first + 10
        private val initialPowerLevel = rackId * coord.second

        fun powerLevel(serialNumber: Int): Int {
            val increasedPowerLevel = ((initialPowerLevel + serialNumber) * rackId).toString()
            val hundreds = if (increasedPowerLevel.length >= 3) increasedPowerLevel[increasedPowerLevel.length - 3] else '0'
            return hundreds.toString().toInt() - 5
        }

        companion object {

             fun getFuelCellGrid(): Array<Array<FuelCell>> {
                 val grid = Array(300) { Array(300) { FuelCell(0 to 0) } }
                 for (y in 0..299) {
                     for (x in 0..299) {
                         grid[y][x] = FuelCell(x + 1 to y + 1)
                     }
                 }
                 return grid
             }
        }
    }

    companion object {

        fun highestPowerInGrid(grid: Array<Array<FuelCell>>, serialNumber: Int): Pair<Int, Int> {
            return (0..grid.size).flatMap { y -> (0..grid[0].size).map { x ->
               val index = x to y
               val square = get3x3Square(grid, index.first to index.second)
               val power = square.map { it.powerLevel(serialNumber) }.sum()
               x + 1 to y + 1 to power
            } }.maxBy { it.second }?.first ?: 0 to 0
        }

        fun <T> get3x3Square(grid: Array<Array<T>>, topLeft: Pair<Int, Int>): List<T> {
            val (x, y) = topLeft
            val indices = listOf(x to y, x + 1 to y, x + 2 to y, x to y + 1, x + 1 to y + 1, x + 2 to y + 1, x to y + 2, x + 1 to y + 2, x + 2 to y + 2)

            if (indices.any { it.second >= grid.size || it.first >= grid[0].size }) {
                return emptyList()
            }
            return indices.map { grid[it.second][it.first] }
        }
    }
}
