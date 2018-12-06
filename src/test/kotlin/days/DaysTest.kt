package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection.empty
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Test
import util.lazyAllPossiblePairs
import java.time.LocalDateTime

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

    @Test
    fun testDayFourRecordParsing() {
        val record1 = Day4.Record("[1518-06-29 00:00] Guard #1153 begins shift")
        assertThat(record1.dateTime, `is`(LocalDateTime.of(1518, 6, 29, 0, 0)))
        assertThat(record1.recordContent, `is`("Guard #1153 begins shift"))
        assertThat(record1.type, `is`(Day4.Record.RecordType.GUARD))

        val record2 = Day4.Record("[1518-05-29 00:56] wakes up")
        assertThat(record2.dateTime, `is`(LocalDateTime.of(1518, 5, 29, 0, 56)))
        assertThat(record2.recordContent, `is`("wakes up"))
        assertThat(record2.type, `is`(Day4.Record.RecordType.WAKE))

        val record3 = Day4.Record("[1518-05-13 00:07] falls asleep")
        assertThat(record3.dateTime, `is`(LocalDateTime.of(1518, 5, 13, 0, 7)))
        assertThat(record3.recordContent, `is`("falls asleep"))
        assertThat(record3.type, `is`(Day4.Record.RecordType.SLEEP))
    }

    @Test
    fun testDayFourGuardParsing() {
        val guard10 = Day4.Guard(Day4.Record("[1518-11-01 00:00] Guard #10 begins shift"))
        assertThat(guard10.id, `is`(10))

        val guard1153 = Day4.Guard(Day4.Record("[1518-06-29 00:00] Guard #1153 begins shift"))
        assertThat(guard1153.id, `is`(1153))
    }

    @Test
    fun testDayFourMostFrequentMinute() {
        // guard 10's minutes asleep from day 4 part 1 example
        val minutes = listOf(
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:05")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:06")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:07")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:08")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:09")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:10")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:11")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:12")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:13")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:14")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:15")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:16")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:17")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:18")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:19")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:20")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:21")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:22")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:23")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:24")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:30")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:31")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:32")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:33")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:34")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:35")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:36")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:37")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:38")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:39")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:40")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:41")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:42")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:43")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:44")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:45")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:46")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:47")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:48")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:49")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:50")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:51")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:52")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:53")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-01T00:54")),
            Day4.MinuteAsleep(LocalDateTime.parse("1518-11-03T00:24")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-03T00:25")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-03T00:26")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-03T00:27")), Day4.MinuteAsleep(LocalDateTime.parse("1518-11-03T00:28"))
        )

        // slightly awkward as we need to create a fake guard and hack its minutesAsleep to test
        val guard = Day4.Guard(Day4.Record("[1518-11-01 00:00] Guard #10 begins shift"))
        guard.minutesAsleep.clear()
        guard.minutesAsleep.addAll(minutes)

        val mostFrequentMinute = guard.mostFrequentMinute()
        assertThat(mostFrequentMinute!!, notNullValue())
        assertThat(mostFrequentMinute.key, `is`(24))          // minute 24
        assertThat(mostFrequentMinute.value, `is`(2))         // 2 days
    }

    @Test
    fun testDayFiveScanAndReact() {
        // use test input from day 5
        val reactedPolymer = Day5.scanAndReact("", "dabAcCaCBAcCcaDA")
        assertThat(reactedPolymer, `is`("dabCBAcaDA"))     // unchanged from last pass
        assertThat(reactedPolymer.length, `is`(10))
    }

    @Test
    fun testDayFiveShortestPolymer() {
        // use test input from day 5
        val shortestPolymer = Day5.shortestPolymer("dabAcCaCBAcCcaDA")
        assertThat(shortestPolymer, `is`(4))
    }

    @Test
    fun testDaySixToCoords() {
        // use test input from day 6
        val coords = Day6.toNamedCoords(listOf("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9"))
        assertThat(coords.toList(), containsInAnyOrder(
            Pair("A", Pair(1, 1)), Pair("B", Pair(1, 6)), Pair("C", Pair(8, 3)),
            Pair("D", Pair(3, 4)), Pair("E", Pair(5, 5)), Pair("F", Pair(8, 9))
        ))

        assertThat(Day6.maxX(coords.values), `is`(8))
        assertThat(Day6.maxY(coords.values), `is`(9))

        // test coord naming
        val fiftyFiveCoords = Day6.toNamedCoords(IntArray(55).map { "$it, $it" })
        assertThat(fiftyFiveCoords.keys, contains(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "AA", "BB", "CC", "DD", "EE", "FF", "GG", "HH", "II", "JJ", "KK", "LL", "MM", "NN", "OO", "PP", "QQ", "RR", "SS", "TT", "UU", "VV", "WW", "XX", "YY", "ZZ",
            "AAA", "BBB", "CCC"
        ))
    }

    @Test
    fun testDay6ManhattanDistance() {
        // use test input from day 6
        val coords = Day6.toNamedCoords(listOf("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9"))
        assertThat(Day6.manhattanDistance(coords["A"]!!, coords["D"]!!), `is`(5))
        assertThat(Day6.manhattanDistance(coords["C"]!!, coords["E"]!!), `is`(5))
        assertThat(Day6.manhattanDistance(coords["A"]!!, coords["F"]!!), `is`(15))
    }
}
