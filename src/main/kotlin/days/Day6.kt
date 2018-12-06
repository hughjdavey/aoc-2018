package days

class Day6 : Day(6) {

    private val coords = toNamedCoords(inputList)

    override fun partOne(): Int {
        println("// Day 6 Part 1 takes about 6 seconds...")
        val allGridCoords = (0..maxX(coords.values)).flatMap { x -> (0..maxY(coords.values)).map { y -> Pair(x, y) } }
        return allGridCoords.asSequence().map { toClosestName(it) to it }.groupingBy { it.first }
            .aggregate { _, accumulator: Set<Pair<Int, Int>>?, element, _ -> accumulator?.plus(element.second) ?: setOf(element.second) }
            .filter { it.value.none { c -> isInfinite(c) } }
            .map { it.value.size }.max() ?: 0
    }

    private fun isInfinite(gridCoord: Pair<Int, Int>) : Boolean {
        return gridCoord.first == 0 || gridCoord.first == maxX(coords.values) ||
                gridCoord.second == 0 || gridCoord.second == maxY(coords.values)
    }

    private fun toClosestName(gridCoord: Pair<Int, Int>): String {
        val baz = coords.map { namedCoord ->
            namedCoord.key to manhattanDistance(namedCoord.value, gridCoord)
        }.sortedBy { it.second }

        return if (baz.count { it.second == baz.first().second } > 1) "." else baz.first().first
    }

    override fun partTwo(): Int {
        val allGridCoords = (0..maxX(coords.values)).flatMap { x -> (0..maxY(coords.values)).map { y -> Pair(x, y) } }
        return allGridCoords.count { coord ->
            val distancesFromCoords = coords.map { entry -> Pair(entry.key, manhattanDistance(entry.value, coord)) }.sortedBy { it.second }
            distancesFromCoords.map { it.second }.sum() < 10000
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
