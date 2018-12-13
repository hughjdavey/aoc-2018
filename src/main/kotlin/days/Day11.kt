package days

import util.cartesianProduct

class Day11 : Day(11) {

    override fun partOne(): Any {
        val grid = FuelCell.getFuelCellGrid()
        return highestPowerInGrid(grid, inputString.trim().toInt())
    }

    override fun partTwo(): Any {
        val grid = FuelCell.getFuelCellGrid()
        return highestPowerInGrid2(grid, inputString.trim().toInt())
    }

    data class FuelCell(val coord: Pair<Int, Int>) {
        private val rackId = coord.first + 10
        private val initialPowerLevel = rackId * coord.second
        private var cachedPowerLevel = Int.MIN_VALUE

        fun powerLevel(serialNumber: Int): Int {
            if (cachedPowerLevel == Int.MAX_VALUE) {
                val increasedPowerLevel = ((initialPowerLevel + serialNumber) * rackId).toString()
                val hundreds = if (increasedPowerLevel.length >= 3) increasedPowerLevel[increasedPowerLevel.length - 3] else '0'
                cachedPowerLevel = hundreds.toString().toInt() - 5
            }
            return cachedPowerLevel
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
               val square = getSquare(grid, index, 3)
               val power = square.map { it.powerLevel(serialNumber) }.sum()
               x + 1 to y + 1 to power
            } }
                .maxBy { it.second }?.first ?: 0 to 0
        }

        fun highestPowerInGrid2(grid: Array<Array<FuelCell>>, serialNumber: Int): Triple<Int, Int, Int> {
            val squareSizes = 1..300

            (0..grid.size).flatMap { y -> (0..grid[0].size).map { x ->
                val index = x to y
                println(index)
                val xysizeTOpower = mutableListOf<Pair<String, Int>>()
                var square = getSquare(grid, index, 1).toMutableList()
                var squareSum = square.map { it.powerLevel(serialNumber) }.sum()
                for (i in 2 .. 300) {
                    //println("iteration $i")
                    //squareSum += getBorder(grid, index, i).map { it.powerLevel(serialNumber) }.sum()
                    squareSum += getBorder(grid, index, i).size
                    xysizeTOpower.add("$x$y$i" to squareSum)
                    //square.addAll(getBorder(grid, index, i))
                }
            }}

            //val foo = (0..grid.size).flatMap { y -> (0..grid[0].size).map { x ->
            //    val index = x to y
            //    val highestTotalPowerToSize = squareSizes.map { size ->
            //        val square = getSquare(grid, index, size)
            //        println("square is $square")
            //        val power = square.map { it.powerLevel(serialNumber) }.sum()
            //        power to size
            //    }.maxBy { it.first }
            //    index to highestTotalPowerToSize
            //} }
            //    .filterNot { it.second == null }
            //    .maxBy { it.second!!.first }!!

            //return Triple(foo.first.first, foo.first.second, foo.second!!.second)
            return Triple(1, 2, 3)
        }

        fun <T> getSquare(grid: Array<Array<T>>, topLeft: Pair<Int, Int>, size: Int): List<T> {
            val indices = getIndices(topLeft, size)

            if (indices.any { it.second >= grid.size || it.first >= grid[0].size }) {
                return emptyList()
            }
            return indices.map { grid[it.second][it.first] }
        }

        fun <T> getBorder(grid: Array<Array<T>>, squareIndices: List<Pair<Int, Int>>): List<T> {
            val size = Math.sqrt(squareIndices.size.toDouble())

            val indices = getIndices(squareIndices.minBy { it.first + it.second }!!, size.toInt() + 1)
            return indices.filterNot { squareIndices.contains(it) }.map { grid[it.second][it.first] }

        }

        fun <T> getBorder(grid: Array<Array<T>>, originalIndex: Pair<Int, Int>, originalSize: Int): List<T> {
            //val size = Math.sqrt(squareIndices.size.toDouble())

            val originalIndices = getIndices(originalIndex, originalSize)
            val indices = getIndices(originalIndex, originalSize + 1)
            return indices.filterNot { originalIndices.contains(it) }.map { grid[it.second][it.first] }

        }

        fun getIndices(topLeft: Pair<Int, Int>, size: Int): List<Pair<Int, Int>> {
            val (x, y) = topLeft
            return cartesianProduct((x until x + size), (y until y + size))
        }
    }
}
