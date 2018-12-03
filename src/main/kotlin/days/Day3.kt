package days

class Day3 : Day(3) {

    private val claims = inputList.map { Claim(it) }
    private val maxX = claims.map { it.maxX() }.sorted().last()
    private val maxY = claims.map { it.maxY() }.sorted().last()
    private val grid = Array(maxX + 1) { Array<MutableSet<Int>>(maxY + 1) { mutableSetOf() } }

    init {
        // fill our 2d array of sets so it represents the piece of fabric with all claims marked. do in init as both parts use it
        claims.forEach { claim -> claim.getAllCoords().forEach { grid[it.first][it.second].add(claim.number) } }
    }

    override fun partOne(): Int {
        return grid.flatMap { it.asList() }.filter { squareInch -> squareInch.size > 1 }.count()
    }

    override fun partTwo(): Int {
        val overlappingClaims = grid.flatMap { it.asList() }.filter { squareInch -> squareInch.size > 1 }.flatten()
        return claims.map { it.number }.first { claimId -> !overlappingClaims.contains(claimId) }
    }

    class Claim(private val claimString: String) {

        val number = claimString.substring(idx('#') + 1, idx('@') - 1).toInt()
        val fromLeft = claimString.substring(idx('@') + 2, idx(',')).toInt()
        val fromTop = claimString.substring(idx(',') + 1, idx(':')).toInt()
        val width = claimString.substring(idx(':') + 2, idx('x')).toInt()
        val height = claimString.substring(idx('x') + 1).toInt()

        private fun idx(char: Char) = claimString.indexOf(char)

        /**
         * Get the highest x coordinate this claim reaches
         */
        fun maxX() = this.fromLeft + this.width

        /**
         * Get the highest y coordinate this claim reaches
         */
        fun maxY() = this.fromTop + this.height

        /**
         * Get all coordinates that this claim encompasses
         */
        fun getAllCoords(): List<Pair<Int, Int>> {
            return (fromLeft + 1..fromLeft + width).flatMap { x ->      // fromLeft + 1 to fromLeft + width == x coords from top left to top right
                (fromTop + 1..fromTop + height).map { y ->              // fromTop + 1 to fromTop + height == y coords from top left to bottom right
                    Pair(x, y)
                }
            }
        }
    }
}
