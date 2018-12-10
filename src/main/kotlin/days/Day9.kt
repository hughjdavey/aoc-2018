package days

import util.infiniteList
import java.util.LinkedList

class Day9 : Day(9) {

    private val players = inputString.substring(0..inputString.indexOf(" players")).trim().toInt()
    private val lastMarbleScore = inputString.substring(inputString.indexOf("worth ") + 6..inputString.indexOf(" points")).trim().toInt()

    override fun partOne(): Any {
        return winningScore(players, lastMarbleScore)
    }

    override fun partTwo(): Any {
        return winningScore(players, lastMarbleScore * 100)
    }

    companion object {

        fun winningScore(players: Int, lastMarble: Int): Long {
            val playersLoop = infiniteList((1L .. players).map { Player(it) }).withIndex()

            val marbleCircle2 = MarbleCircle()
            val gameHistory2 = playersLoop.takeWhile { (index, player) ->
                player.lastMarble = index + 1L
                player.score += marbleCircle2.placeMarble(player.lastMarble).toInt()
                index != lastMarble
            }
            return gameHistory2.maxBy { it.value.score }?.value?.score ?: 0
        }
    }

    data class Player(val number: Long, var score: Long = 0, var lastMarble: Long = 0)

    class MarbleCircle {

        private val marbles = LinkedList(listOf(0L))

        fun placeMarble(marble: Long): Long {
            if (marble % 23 == 0L) {
                for (i in 0 until 6) {
                    marbles.addLast(marbles.removeFirst())
                }
                return marbles.removeFirst() + marble
            }
            else {
                for (i in 0 until 2) {
                    marbles.addFirst(marbles.removeLast())
                }
                marbles.addLast(marble)
            }
            return 0
        }
    }
}
