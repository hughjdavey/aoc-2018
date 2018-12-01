package days

import util.InputReader

class Day1 : Day {

    override fun partOne(): Int {
        val frequencies = InputReader.getInputAsList(1)
        return frequencies.fold(0) { sum, freq ->
            sum + freq.toInt()
        }
    }

    override fun partTwo(): Int {
        val infiniteFrequencies = InfiniteList(InputReader.getInputAsList(1))
        val reachedFrequencies = mutableSetOf<Int>()
        val repeatedFreq: Int
        var currentFreq = 0

        while (true) {
            currentFreq += infiniteFrequencies.next().toInt()
            if (!reachedFrequencies.contains(currentFreq)) {
                reachedFrequencies.add(currentFreq)
            }
            else {
                repeatedFreq = currentFreq
                break
            }
        }

        return repeatedFreq
    }

    class InfiniteList<T>(private val base: List<T>) {

        private var index: Int = -1

        fun next(): T {
            if (index + 1 < base.size) {
                index += 1
            }
            else {
                index = 0
            }
            return base[index]
        }
    }
}
