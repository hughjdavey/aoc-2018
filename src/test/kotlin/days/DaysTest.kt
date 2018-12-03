package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection.empty
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.junit.Test
import util.lazyAllPossiblePairs

/**
 * Tests for the days package
 */
class DaysTest {

    @Test
    fun testDayOneFreqSeenTracker() {
        assertThat(Day1.FreqSeenTracker.seenFreqs, empty())

        val a = Day1.FreqSeenTracker()
        assertThat(a.currentFreq, `is`(0))
        assertThat(a.seenBefore, `is`(false))
        assertThat(Day1.FreqSeenTracker.seenFreqs, empty())

        val b = Day1.FreqSeenTracker(1)
        assertThat(b.currentFreq, `is`(1))
        assertThat(b.seenBefore, `is`(false))
        assertThat(Day1.FreqSeenTracker.seenFreqs, contains(1))

        val c = Day1.FreqSeenTracker(1)
        assertThat(c.currentFreq, `is`(1))
        assertThat(c.seenBefore, `is`(true))
        val d = Day1.FreqSeenTracker(2)
        assertThat(d.currentFreq, `is`(2))
        assertThat(d.seenBefore, `is`(false))
        val e = Day1.FreqSeenTracker(3)
        assertThat(e.currentFreq, `is`(3))
        assertThat(e.seenBefore, `is`(false))
        assertThat(Day1.FreqSeenTracker.seenFreqs, contains(1, 2, 3))
    }

    @Test
    fun testDayTwoPartOneToMatchSummary() {
        // use the example box ids from day 2 part 1 description
        val boxIds = listOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")
        assertThat(boxIds.map { Day2.toMatchSummary(it) }, contains(
            Pair(false, false),
            Pair(true, true),
            Pair(true, false),
            Pair(false, true),
            Pair(true, false),
            Pair(true, false),
            Pair(false, true)
        ))
    }

    @Test
    fun testDayTwoPartTwoCommonLetters() {
        // use the example box ids from day 2 part 2 description
        assertThat(Day2.commonLetters("fghij", "fguij"), `is`("fgij"))

        val boxIds = listOf("abc", "def", "adc")
        val commonLettersForAllPairs = lazyAllPossiblePairs(boxIds, boxIds).map { Day2.commonLetters(it.first, it.second) }.take(9).toList()
        assertThat(commonLettersForAllPairs, contains(
            "abc", "", "ac", "", "def", "", "ac", "", "adc"
        ))
    }

    @Test
    fun testDayThreeClaimParsing() {
        val claim1 = Day3.Claim("#123 @ 3,2: 5x4")
        assertThat(claim1.number, `is`(123))
        assertThat(claim1.fromLeft, `is`(3))
        assertThat(claim1.fromTop, `is`(2))
        assertThat(claim1.width, `is`(5))
        assertThat(claim1.height, `is`(4))

        val claim2 = Day3.Claim("#1391 @ 256,801: 23x10")
        assertThat(claim2.number, `is`(1391))
        assertThat(claim2.fromLeft, `is`(256))
        assertThat(claim2.fromTop, `is`(801))
        assertThat(claim2.width, `is`(23))
        assertThat(claim2.height, `is`(10))
    }

    @Test
    fun testDayThreeClaimMaxXAndY() {
        val claim1 = Day3.Claim("#123 @ 3,2: 5x4")
        assertThat(claim1.maxX(), `is`(8))
        assertThat(claim1.maxY(), `is`(6))

        val claim2 = Day3.Claim("#1391 @ 256,801: 23x10")
        assertThat(claim2.maxX(), `is`(279))
        assertThat(claim2.maxY(), `is`(811))
    }

    @Test
    fun testDayThreeClaimGetAllCoords() {
        // use the example claim and its diagram from day 3 part 1 description
        /*
            expected =
            ...........
            ...........
            ...#####...
            ...#####...
            ...#####...
            ...#####...
            ...........
            ...........
            ...........
         */
        val allCoords = Day3.Claim("#123 @ 3,2: 5x4").getAllCoords()
        assertThat(allCoords, containsInAnyOrder(
            Pair(4, 3), Pair(5, 3), Pair(6, 3), Pair(7, 3), Pair(8, 3),
            Pair(4, 4), Pair(5, 4), Pair(6, 4), Pair(7, 4), Pair(8, 4),
            Pair(4, 5), Pair(5, 5), Pair(6, 5), Pair(7, 5), Pair(8, 5),
            Pair(4, 6), Pair(5, 6), Pair(6, 6), Pair(7, 6), Pair(8, 6)
        ))
    }
}
