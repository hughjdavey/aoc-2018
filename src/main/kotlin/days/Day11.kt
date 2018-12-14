package days

import util.cartesianProduct

class Day11 : Day(11) {

    override fun partOne(): Any {
        val grid = FuelCell.getFuelCellGrid()
        return highestPowerInGrid(grid, inputString.trim().toInt())
    }

    override fun partTwo(): Any {
        val serialNumber = inputString.trim().toInt()

        var max: Pair<Pair<Int, Int>, Int> = 0 to 0 to 0
        var maxSize = 0
        var i = 1
        //while (i <= 300) {        // todo improve performance such that we can run the whole thing
        while (i <= 12) {
            val new = highestPowerInGrid(FuelCell.getFuelCellGrid(), serialNumber, i)
            if (new.second > max.second) {
                max = new
                maxSize = i
            }
            i++
        }

        return "${max.first.first},${max.first.second},$maxSize"
    }

    data class FuelCell(val coord: Pair<Int, Int>) {
        private val rackId = coord.first + 10
        private val initialPowerLevel = rackId * coord.second
        private var cachedPowerLevel = Int.MIN_VALUE

        fun powerLevel(serialNumber: Int): Int {
            if (cachedPowerLevel == Int.MIN_VALUE) {
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
            return (0..grid.size).flatMap { y ->
                (0..grid[0].size).map { x ->
                    val index = x to y
                    val square = getSquare(grid, index, 3)
                    val power = square.map { it.powerLevel(serialNumber) }.sum()
                    x + 1 to y + 1 to power
                }
            }.maxBy { it.second }?.first ?: 0 to 0
        }

        fun highestPowerInGrid(grid: Array<Array<FuelCell>>, serialNumber: Int, gridSize: Int): Pair<Pair<Int, Int>, Int> {
            return (0..grid.size).flatMap { y ->
                (0..grid[0].size).map { x ->
                    val index = x to y
                    val square = getSquare(grid, index, gridSize)
                    val power = square.map { it.powerLevel(serialNumber) }.sum()
                    x + 1 to y + 1 to power
                }
            }.maxBy { it.second } ?: 0 to 0 to 0
        }

        fun <T> getSquare(grid: Array<Array<T>>, topLeft: Pair<Int, Int>, size: Int): List<T> {
            val indices = getIndices(topLeft, size)

            if (indices.any { it.second >= grid.size || it.first >= grid[0].size }) {
                return emptyList()
            }
            return indices.map { grid[it.second][it.first] }
        }

        fun getIndices(topLeft: Pair<Int, Int>, size: Int): List<Pair<Int, Int>> {
            val (x, y) = topLeft
            return cartesianProduct((x until x + size), (y until y + size))
        }
    }
}
