package days

class Day6 : Day(6) {

    private val coords = toNamedCoords(inputList)

    // todo improve this!
    override fun partOne(): Int {
        val allGridCoords = (0..maxX(coords.values)).flatMap { x -> (0..maxY(coords.values)).map { y -> Pair(x, y) } }

        val map = mutableMapOf<String, Int>()
        val inf = mutableSetOf("")

        allGridCoords.forEach { coord ->
            val distancesFromCoords = coords.map { entry -> Pair(entry.key, manhattanDistance(entry.value, coord)) }.sortedBy { it.second }
            val closestCoord = distancesFromCoords.first()
            if (distancesFromCoords.filter { it.second == closestCoord.second }.size == 1) {
                map[closestCoord.first] = (map[closestCoord.first] ?: 0) + 1

                if (coord.first == 0 || coord.first == maxX(coords.values) || coord.second == 0 || coord.second == maxY(coords.values)) {
                    inf.add(closestCoord.first)
                }
            }
        }

        return map.entries
            .filter { !inf.contains(it.key) }
            .sortedBy { it.value }
            .last().value
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
