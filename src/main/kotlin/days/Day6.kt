package days

class Day6 : Day(6) {

    private val coords = toNamedCoords(inputList)

    // todo improve this!
    override fun partOne(): Int {
        val allGridCoords = (0..maxX(coords.values)).flatMap { x -> (0..maxY(coords.values)).map { y -> Pair(x, y) } }

        val grid = Array(maxY(coords.values) + 1) { Array(maxX(coords.values) + 1) { "?" } }
        coords.forEach { coord -> grid[coord.value.second][coord.value.first] = coord.key }

        allGridCoords.forEach { coord ->
            val distancesFromCoords = coords.map { entry -> Pair(entry.key, manhattanDistance(entry.value, coord)) }.sortedBy { it.second }
            val closestCoord = distancesFromCoords.first()
            if (distancesFromCoords.filter { it.second == closestCoord.second }.size > 1) {
                grid[coord.second][coord.first] = "."
            }
            else {
                grid[coord.second][coord.first] = closestCoord.first.toLowerCase()
            }
        }

        val infiniteCoords = grid.first().plus(grid.last()).plus(grid.map { it.first() }).plus(grid.map { it.last() }).toSet()

        val largestAreaCoord = grid.flatten()
            .filter { name -> name.all { it.isLowerCase() } }
            .filter { name -> !infiniteCoords.contains(name) }
            .groupingBy { it }
            .eachCount()
            .values.sorted()

        return largestAreaCoord.max() ?: 0
    }

    override fun partTwo(): Int {
        val allGridCoords = (0..maxX(coords.values)).flatMap { x -> (0..maxY(coords.values)).map { y -> Pair(x, y) } }
        return allGridCoords.count { coord ->
            val distancesFromCoords = coords.map { entry -> Pair(entry.key, manhattanDistance(entry.value, coord)) }.sortedBy { it.second }
            distancesFromCoords.map { it.second }.sum() < 10000
        }
    }

    private fun printGrid(grid: Array<Array<String>>) {
        for (i in 0 until grid.size) {
            println()
            for (j in 0 until grid[0].size) {
                print(grid[i][j] + " ")
            }
        }
    }

    companion object {

        fun toNamedCoords(input: List<String>): Map<String, Pair<Int, Int>> {
            return input.map { it.split(',') }
                .map { it.map { s -> s.trim() } }
                .mapIndexed { index, splits -> getCoordName(index) to Pair(splits[0].toInt(), splits[1].toInt()) }
                .toMap()
        }

        fun manhattanDistance(c1: Pair<Int, Int>, c2: Pair<Int, Int>): Int {
            return diff(c1.first, c2.first) + diff(c1.second, c2.second)
        }

        fun maxX(coords: Collection<Pair<Int, Int>>) = coords.sortedBy { it.first }.last().first

        fun maxY(coords: Collection<Pair<Int, Int>>) = coords.sortedBy { it.second }.last().second

        private fun diff(x: Int, y: Int): Int {
            return Math.max(x, y) - Math.min(x, y)
        }

        private fun getCoordName(index: Int): String {
            val repeatCount = (index / 26) + 1
            return ('A' + (index % 26)).toString().repeat(repeatCount)
        }
    }
}
