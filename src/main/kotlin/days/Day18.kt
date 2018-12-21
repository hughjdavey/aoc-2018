package days

class Day18 : Day(18) {

    override fun partOne(): Any {
        var parsed = Day18.parseArea(inputList)
        (1..10).forEach { parsed = Day18.doMinute(parsed) }
        return parsed.flatten().count { it.isTrees() } * parsed.flatten().count { it.isYard() }
    }

    override fun partTwo(): Any {
        println("// Day 18 Part 2 takes about 6 seconds...")
        var parsed = Day18.parseArea(inputList)
        val seen = mutableListOf(parsed)
        (1..1000000000).forEach { minute ->
            parsed = Day18.doMinute(parsed)
            val seenBefore = seen.find { prettyPrint(it) == prettyPrint(parsed) }

            if (seenBefore != null) {
                val patternLength = seen.size - seen.indexOfFirst { prettyPrint(it) == prettyPrint(seenBefore) }
                val remainingMinutes = 1000000000 - minute

                // now we have a pattern figure how many more iterations needed to reach the place in the pattern where we'd also be after 1000000000
                val howManyMore = remainingMinutes % patternLength
                for (i in 0 until howManyMore) {
                    parsed = Day18.doMinute(parsed)
                }
                return parsed.flatten().count { it.isTrees() } * parsed.flatten().count { it.isYard() }
            }
            else {
                seen.add(parsed)
            }
        }
        return parsed.flatten().count { it.isTrees() } * parsed.flatten().count { it.isYard() }
    }

    data class Acre(var type: Char, val index: Pair<Int, Int>) {
        fun isOpen() = type == OPEN
        fun isTrees() = type == TREES
        fun isYard() = type == YARD

        var next = type

        companion object {
            const val OPEN = '.'
            const val TREES = '|'
            const val YARD = '#'
        }
    }

    companion object {

        fun doMinute(area: Array<Array<Acre>>): Array<Array<Acre>> {
            val newArea = Array(area.size) { Array(area.first().size) { Acre('?', 0 to 0) } }

            area.flatten().forEach { acre ->
                val adjacent = getAdjacent(acre, area)
                val newType = when {
                    acre.isOpen() -> if (adjacent.count { it.isTrees() } >= 3) Acre.TREES else acre.type
                    acre.isTrees() -> if (adjacent.count { it.isYard() } >= 3) Acre.YARD else acre.type
                    acre.isYard() -> if (adjacent.count { it.isYard() } >= 1 && adjacent.count { it.isTrees() } >= 1) Acre.YARD else Acre.OPEN
                    else -> acre.type
                }
                newArea[acre.index.first][acre.index.second] = acre.copy(type = newType)
            }
            return newArea
        }

        fun parseArea(input: List<String>): Array<Array<Acre>> {
            val area = Array(input.size) { Array(input.first().length) { Acre('?', 0 to 0) } }
            for (y in 0 .. input.lastIndex) {
                for (x in 0 .. input[y].lastIndex) {
                    area[x][y] = Acre(input[y][x], x to y)
                }
            }
            return area
        }

        fun prettyPrint(area: Array<Array<Acre>>): String {
            val s = StringBuilder()
            for (y in 0 .. area.lastIndex) {
                for (x in 0 .. area[y].lastIndex) {
                    s.append(area[x][y].type)
                }
                s.append('\n')
            }
            return s.toString()
        }

        fun getAdjacent(acre: Acre, area: Array<Array<Acre>>): List<Acre> {
            return adjacentIndices(acre.index).mapNotNull {
                try { area[it.first][it.second] } catch (e: ArrayIndexOutOfBoundsException) { null }
            }
        }

        private fun adjacentIndices(index: Pair<Int, Int>): List<Pair<Int, Int>> {
            val (x, y) = index
            return listOf(x - 1 to y - 1, x to y - 1, x + 1 to y - 1,
                          x - 1 to y, /* x to y */ x + 1 to y,
                          x - 1 to y + 1, x to y + 1, x + 1 to y + 1)
        }
    }
}
