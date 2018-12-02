package days

import util.lazyAllPossiblePairs
import util.toInt

class Day2 : Day(2) {

    override fun partOne(): Int {
        val counts = inputList.map { toMatchSummary(it) }.fold(Pair(0, 0)) { totals, summary ->
            Pair(totals.first + summary.first.toInt(), totals.second + summary.second.toInt())
        }
        return counts.first * counts.second
    }

    override fun partTwo(): String {
        val boxIdLength = inputList.first().length

        // validate an assumption that all boxIds are the same length
        if (!inputList.all { it.length == boxIdLength }) throw IllegalArgumentException("")

        return lazyAllPossiblePairs(inputList, inputList)
            .map { commonLetters(it.first, it.second) }
            .find { it.length == boxIdLength - 1 }!!    // if the common letters string is one char shorter than the id length then the ids differ by exactly one letter
    }

    companion object {

        /**
         * Take a boxId (string of letters) and return a summary of its relevant internal matches
         *
         * Pair#first is true if boxId contains exactly 2 of a letter
         * Pair#second is true if boxId contains exactly 3 of a letter
         */
        fun toMatchSummary(boxId: String): Pair<Boolean, Boolean> {
            val charCounts = boxId.associate { char -> Pair(char, boxId.count { it == char }) }.values
            return Pair(charCounts.any { it == 2 }, charCounts.any { it == 3 })
        }

        /**
         * Take two boxIds and return the letters they have in common (same letter at same index)
         */
        fun commonLetters(id1: String, id2: String): String {
            return id1.mapIndexed { index, char ->
                if (char == id2[index]) char else null
            }.filterNotNull().joinToString("")
        }
    }
}
