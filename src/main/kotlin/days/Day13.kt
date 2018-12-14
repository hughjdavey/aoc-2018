package days

import util.Tracks
import util.infiniteList

class Day13 : Day(13) {

    override fun partOne(): Any {
        val initialState = Day13.initCarts(inputList)

        var iteration = 1
        while (initialState.flatten().none { it.second != null && it.second!!.state == 'X' }) {
            Day13.doTick(initialState, iteration++)
        }
        return initialState.flatten().find { it.second != null && it.second!!.state == 'X' }?.second?.location ?: -1
    }

    override fun partTwo(): Any {
        val initialState = Day13.initCarts(inputList)

        var iteration = 1
        while (initialState.flatten().count { it.second != null } > 1) {
            Day13.doTick(initialState, iteration++, true)
        }
        return initialState.flatten().find { it.second != null }?.second?.location ?: -1
    }

    data class Cart(var state: Char, var location: Pair<Int, Int>, val intersectionBehaviour: Iterator<Char> = infiniteList(listOf('l', 's', 'r')).iterator()) {

        private var moves = 0

        fun canMove(iteration: Int) = moves < iteration

        // todo this function is messy - refactor
        // todo perhaps some kind of direction abstraction to replace all the charAt stuff
        fun move(tracks: Tracks, cleanup: Boolean = false) {
            val newLocation = when (this.state) {
                '>' -> location.copy(first = location.first + 1)
                '<' -> location.copy(first = location.first - 1)
                'v' -> location.copy(second = location.second + 1)
                '^' -> location.copy(second = location.second - 1)
                else -> 0 to 0
            }

            val charAt = tracks[newLocation.second][newLocation.first].first
            val newState =
                if (tracks[newLocation.second][newLocation.first].second != null) {
                    'X'
                }
            else if (isIntersection(charAt)) {
                val direction = intersectionBehaviour.next()
                when (direction) {
                    'l' -> if (state == '>') '^' else if (state == '<') 'v' else if (state == 'v') '>' else '<'
                    's' -> state
                    'r' -> if (state == '>') 'v' else if (state == '<') '^' else if (state == 'v') '<' else '>'
                    else -> state
                }
            }
            else {
                when (charAt) {
                    'X' -> if (cleanup) '.' else 'X'
                    '-', '|' -> state
                    '\\' -> if (state == '>') 'v' else if (state == '<') '^' else if (state == 'v') '>' else '<'
                    '/' -> if (state == '>') '^' else if (state == '<') 'v' else if (state == 'v') '<' else '>'
                    else -> state
                }
            }

            tracks[location.second][location.first] = tracks[location.second][location.first].first to null
            tracks[newLocation.second][newLocation.first] = tracks[newLocation.second][newLocation.first].first to this

            if (cleanup && newState == 'X') {
                tracks[newLocation.second][newLocation.first] = tracks[newLocation.second][newLocation.first].first to null
            }

            state = newState
            location = newLocation
            moves++
        }

        companion object {

            fun isCart(char: Char) = char == '>' || char == '<' || char == '^' || char == 'v'

            fun isIntersection(char: Char) = char == '+'
        }
    }

    companion object {

        fun initCarts(tracks: List<String>): Tracks {
            val grid = Array(tracks.size) { Array(tracks.sortedBy { it.length }.last().length) { '.' to null as Cart? } }
            for (y in 0 .. tracks.lastIndex) {
                for (x in 0 .. tracks[y].lastIndex) {
                    grid[y][x] = if (Cart.isCart(tracks[y][x])) {
                        val missing = if (tracks[y][x] == '>' || tracks[y][x] == '<') '-' else '|'
                        missing to Cart(tracks[y][x], x to y)
                    } else {
                        tracks[y][x] to null
                    }
                }
            }
            return grid
        }

        fun doTick(tracks: Tracks, iteration: Int, partTwo: Boolean = false): Tracks {
            for (y in 0 .. tracks.lastIndex) {
                for (x in 0 .. tracks[y].lastIndex) {
                    val maybeCart = tracks[y][x].second
                    if (maybeCart != null && maybeCart.canMove(iteration)) {
                        maybeCart.move(tracks, partTwo)
                    }
                }
            }
            return tracks
        }

        fun prettyPrint(tracks: Tracks) {
            for (y in 0 .. tracks.lastIndex) {
                for (x in 0 .. tracks[y].lastIndex) {
                    if (tracks[y][x].second != null) print(tracks[y][x].second!!.state) else print(tracks[y][x].first)
                }
                println()
            }
        }
    }
}
