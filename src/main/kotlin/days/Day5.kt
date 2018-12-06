package days

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import util.sameLetterDifferentCase

class Day5 : Day(5) {

    override fun partOne(): Int {
        val reactedPolymer = scanAndReact("", inputString)
        return reactedPolymer.length - 1
    }

    override fun partTwo(): Int {
        return shortestPolymer(inputString) - 1
    }

    companion object {

        fun shortestPolymer(polymer: String): Int {
            // map each distinct char in string to string without that char (all cases)
            // then map to reacted polymer and take the length, returning the lowest length
            val distinctUnits = polymer.toLowerCase().toCharArray().distinct()
            return runBlocking {
                distinctUnits
                    .map { polymer.replace(it.toString(), "", true) }
                    .map { async { scanAndReact(it) } }
                    .map { it.await().length }
                    .min() ?: 0
            }
        }

        tailrec fun scanAndReact(oldPolymer: String, polymer: String): String {
            return if (oldPolymer.length == polymer.length) {
                polymer
            } else {
                scanAndReact(polymer, scanAndReact(polymer))
            }
        }

        private fun scanAndReact(polymer: String): String {
            // map each char in string into a pair of index to char and remove indices that would break the lookaheads
            // then find first pair of units that can react (converting the string to a lazy sequence drastically improves performance)
            val firstReaction = polymer.asSequence()
                    .mapIndexed { index, char -> Pair(index, char) }
                    .filter { it.first < polymer.length - 1 }
                    .find { polymer[it.first].sameLetterDifferentCase(polymer[it.first + 1]) }

            return if (firstReaction != null) {
                polymer.replaceRange(firstReaction.first, firstReaction.first + 2, "")
            } else polymer
        }
    }

}
