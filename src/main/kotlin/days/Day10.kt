package days

import util.scan

class Day10 : Day(10) {

    // move these here instead of in each part to save calculation time
    private val seq = (0..100000).asSequence().scan(0 to parsePoints(inputList)) { (_, points), i ->
        i + 1 to points.map { moveOneSecond(it) }
    }
    private val minDistPoints = seq.minBy { getMaxDistanceBetweenPoints(it.second) }


    override fun partOne(): Any {
        return drawOnGrid(minDistPoints!!.second)
    }

    override fun partTwo(): Any {
        return minDistPoints!!.first
    }

    data class LightPoint(private val initialPosition: Pair<Int, Int>, val velocity: Pair<Int, Int>) {
        var position = initialPosition
    }

    companion object {

        fun getMaxDistanceBetweenPoints(points: List<LightPoint>): Int {
            val positions = points.map { it.position }
            return Math.max(
                positions.map { it.first }.max()!! - positions.map { it.first }.min()!!,
                positions.map { it.second }.max()!! - positions.map { it.second }.min()!!
            )

        }

        fun drawOnGrid(points: List<LightPoint>): String {
            val positions = points.map { it.position }
            val minX = positions.minBy { it.first }!!.first
            val maxX = positions.maxBy { it.first }!!.first
            val minY = positions.minBy { it.second }!!.second
            val maxY = positions.maxBy { it.second }!!.second

            val xExtent = maxX - minX
            val yExtent = maxY - minY

            val grid = Array(yExtent + 1) { Array(xExtent + 1) { '.' } }
            points.forEach { grid[it.position.second - minY][it.position.first - minX] = '#' }

            val s = StringBuilder()
            for (y in 0..yExtent) {
                s.append('\n')
                for (x in 0..xExtent) {
                    s.append(grid[y][x]).append(" ")
                }
            }
            return s.toString()
        }

        fun moveOneSecond(point: LightPoint): LightPoint {
            return LightPoint(
                point.position.first + point.velocity.first to point.position.second + point.velocity.second,
                point.velocity
            )
        }

        fun parsePoints(input: List<String>): List<LightPoint> {
            return input.map {
                val position = parsePair(it, it.indexOf('<') + 1, it.indexOf('>'))
                val velocity = parsePair(it, it.lastIndexOf('<') + 1, it.lastIndexOf('>'))
                LightPoint(position, velocity)
            }
        }

        private fun parsePair(string: String, start: Int, end: Int): Pair<Int, Int> {
            return string.substring(start, end).split(',').map { it.trim().toInt() }.zipWithNext().first()
        }
    }
}
