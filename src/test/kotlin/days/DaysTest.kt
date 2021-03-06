package days

import days.Day18.Companion.getAdjacent
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.hamcrest.collection.IsEmptyCollection.empty
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.hamcrest.core.StringContains.containsString
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
    fun testDaySixManhattanDistance() {
        // use test input from day 6
        val coords = Day6.toNamedCoords(listOf("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9"))
        assertThat(Day6.manhattanDistance(coords["A"]!!, coords["D"]!!), `is`(5))
        assertThat(Day6.manhattanDistance(coords["C"]!!, coords["E"]!!), `is`(5))
        assertThat(Day6.manhattanDistance(coords["A"]!!, coords["F"]!!), `is`(15))
    }

    @Test
    fun testDaySevenStepParsing() {
        // use test input from day 7
        val input = listOf(
            "Step C must be finished before step A can begin.",
            "Step C must be finished before step F can begin.",
            "Step A must be finished before step B can begin.",
            "Step A must be finished before step D can begin.",
            "Step B must be finished before step E can begin.",
            "Step D must be finished before step E can begin.",
            "Step F must be finished before step E can begin.")

        val steps = Day7.parseSteps(input)
        assertThat(steps, hasSize(6))
        assertThat(steps.find { it.name == 'A' }!!.dependencies, contains('C'))
        assertThat(steps.find { it.name == 'B' }!!.dependencies, contains('A'))
        assertThat(steps.find { it.name == 'C' }!!.dependencies, empty())
        assertThat(steps.find { it.name == 'D' }!!.dependencies, contains('A'))
        assertThat(steps.find { it.name == 'E' }!!.dependencies, contains('B', 'D', 'F'))
        assertThat(steps.find { it.name == 'F' }!!.dependencies, contains('C'))
    }

    @Test
    fun testDayEightTreeParsing() {
        // use test input from day 8
        val root = Day8.parseTree("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")

        // A
        assertThat(root.header, `is`(2 to 3))
        assertThat(root.childNodes.size, `is`(2))
        assertThat(root.metadata, containsInAnyOrder(1, 1, 2))

        // B
        assertThat(root.childNodes[0].header, `is`(0 to 3))
        assertThat(root.childNodes[0].childNodes.size, `is`(0))
        assertThat(root.childNodes[0].metadata, containsInAnyOrder(10, 11, 12))

        // C
        assertThat(root.childNodes[1].header, `is`(1 to 1))
        assertThat(root.childNodes[1].childNodes.size, `is`(1))
        assertThat(root.childNodes[1].metadata, containsInAnyOrder(2))

        // D
        assertThat(root.childNodes[1].childNodes[0].header, `is`(0 to 1))
        assertThat(root.childNodes[1].childNodes[0].childNodes.size, `is`(0))
        assertThat(root.childNodes[1].childNodes[0].metadata, containsInAnyOrder(99))
    }

    @Test
    fun testDayEightMetadataSum() {
        // use test input from day 8
        val root = Day8.parseTree("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")
        assertThat(Day8.metadataSum(root), `is`(138))
    }

    @Test
    fun testDayEightNodeValue() {
        // use test input from day 8
        val root = Day8.parseTree("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")
        assertThat(Day8.nodeValue(root), `is`(66))
    }

    @Test
    fun testDayNineWinningScores() {
        assertThat(Day9.winningScore(9, 25), `is`(32L))
        assertThat(Day9.winningScore(10, 1618), `is`(8317L))
        assertThat(Day9.winningScore(13, 7999), `is`(146373L))
        assertThat(Day9.winningScore(17, 1104), `is`(2764L))
        assertThat(Day9.winningScore(21, 6111), `is`(54718L))
        assertThat(Day9.winningScore(30, 5807), `is`(37305L))
    }

    @Test
    fun testDayTenPointParsing() {
        val points = Day10.parsePoints(day10TestPoints)
        assertThat(points, hasSize(31))

        // only test selected as there are so many
        assertThat(points[0], `is`(Day10.LightPoint(9 to 1, 0 to 2)))
        assertThat(points[10], `is`(Day10.LightPoint(-2 to 3, 1 to 0)))
        assertThat(points[20], `is`(Day10.LightPoint(0 to 5, 0 to -1)))
        assertThat(points[30], `is`(Day10.LightPoint(-3 to 6, 2 to -1)))
    }

    @Test
    fun testDay10CorrectMessage() {
        var points = Day10.parsePoints(day10TestPoints)
        assertThat(points, hasSize(31))

        // pass 3 seconds
        (0 until 3).forEach { points = points.map { Day10.moveOneSecond(it) } }

        val gridAfterThreeSeconds = Day10.drawOnGrid(points)
        assertThat(gridAfterThreeSeconds, `is`(
            "\n" +
            "# . . . # . . # # # \n" +
            "# . . . # . . . # . \n" +
            "# . . . # . . . # . \n" +
            "# # # # # . . . # . \n" +
            "# . . . # . . . # . \n" +
            "# . . . # . . . # . \n" +
            "# . . . # . . . # . \n" +
            "# . . . # . . # # # "
        ))
    }

    @Test
    fun testDay11FuelCellPowerLevel() {
        // examples from day 11 part 1
        assertThat(Day11.FuelCell(3 to 5).powerLevel(8), `is`(4))
        assertThat(Day11.FuelCell(122 to 79).powerLevel(57), `is`(-5))
        assertThat(Day11.FuelCell(217 to 196).powerLevel(39), `is`(0))
        assertThat(Day11.FuelCell(101 to 153).powerLevel(71), `is`(4))
    }

    @Test
    fun testDay11Get3x3Square() {
        val grid: Array<Array<Int>> = arrayOf(
            arrayOf(0,  1,  2,  3,  4),
            arrayOf(5,  6,  7,  8,  9),
            arrayOf(10, 11, 12, 13, 14),
            arrayOf(15, 16, 17, 18, 19),
            arrayOf(20, 21, 22, 23, 24)
        )

        val topLeft3x3 = Day11.getSquare(grid, 0 to 0, 3)
        assertThat(topLeft3x3, containsInAnyOrder(0, 1, 2, 5, 6, 7, 10, 11, 12))

        val middle3x3 = Day11.getSquare(grid, 2 to 2, 3)
        assertThat(middle3x3, containsInAnyOrder(12, 13, 14, 17, 18, 19, 22, 23, 24))

        val invalid3x3 = Day11.getSquare(grid, 3 to 3, 3)
        assertThat(invalid3x3, empty())
    }

    @Test
    fun testDay11Part1HighestPower() {
        assertThat(Day11.highestPowerInGrid(Day11.FuelCell.getFuelCellGrid(), 18), `is`(33 to 45))
        assertThat(Day11.highestPowerInGrid(Day11.FuelCell.getFuelCellGrid(), 42), `is`(21 to 61))
    }

//    @Test
//    fun testDay11Part2HighestPower() {
//        val grid = Day11.FuelCell.getFuelCellGrid()
//        assertThat(Day11.highestPowerInGrid2(grid, 18), `is`(Triple(90, 269, 16)))
//        assertThat(Day11.highestPowerInGrid2(grid, 42), `is`(Triple(232, 251, 12)))
//    }

    @Test
    fun testDay12ParseInitialState() {
        val initialState1 = Day12.parseInitialState("initial state: #..##....")
        assertThat(initialState1.length, `is`(9))
        assertThat(initialState1.mapIndexed { index, c -> if (c == '#') index else null }.filterNotNull(), contains(0, 3, 4))

        val initialState2 = Day12.parseInitialState("initial state: #..#.#..##......###...###")
        assertThat(initialState2.length, `is`(25))
        assertThat(initialState2.mapIndexed { index, c -> if (c == '#') index else null }.filterNotNull(), contains(0, 3, 5, 8, 9, 16, 17, 18, 22, 23, 24))
    }

    @Test
    fun testDay12Rules() {
        val notes = Day12.parseRules(listOf("..#.. => .", "##.## => .", ".##.# => #"))
        assertThat(notes, hasSize(3))
        assertThat(notes, contains(
            Day12.Rule("..#..", '.'),
            Day12.Rule("##.##", '.'),
            Day12.Rule(".##.#", '#')
        ))
    }

    @Test
    fun testDay12PadState() {
        assertThat(Day12.padState(""), `is`(".........." to 5))
        assertThat(Day12.padState("#..##...."), `is`(".....#..##....." to 5))
        assertThat(Day12.padState(".#..##.."), `is`(".....#..##....." to 4))
        assertThat(Day12.padState("...#..##"), `is`(".....#..##....." to 2))
    }

    @Test
    fun testDay12DoGeneration() {
        val initialState = "#..#.#..##......###...###"
        val rules = Day12.parseRules(listOf(
            "...## => #", "..#.. => #", ".#... => #", ".#.#. => #", ".#.## => #", ".##.. => #", ".#### => #",
            "#.#.# => #", "#.### => #", "##.#. => #", "##.## => #", "###.. => #", "###.# => #", "####. => #"
        ))

        val endState = Day12.doGeneration(initialState to 0, rules)
        assertThat(endState.first, containsString("#...#....#.....#..#..#..#"))
        assertThat(endState.second, `is`(5))
    }

    @Test
    fun testDay12DoGenerations() {
        val initialState = "#..#.#..##......###...###..........."
        val rules = Day12.parseRules(listOf(
            "...## => #", "..#.. => #", ".#... => #", ".#.#. => #", ".#.## => #", ".##.. => #", ".#### => #",
            "#.#.# => #", "#.### => #", "##.#. => #", "##.## => #", "###.. => #", "###.# => #", "####. => #"
        ))

        assertThat(Day12.doGenerations(initialState, rules, 1).first, containsString("...#...#....#.....#..#..#..#..........."))
        assertThat(Day12.doGenerations(initialState, rules, 2).first, containsString("...##..##...##....#..#..#..##.........."))
        assertThat(Day12.doGenerations(initialState, rules, 3).first, containsString("..#.#...#..#.#....#..#..#...#.........."))
        assertThat(Day12.doGenerations(initialState, rules, 4).first, containsString("...#.#..#...#.#...#..#..##..##........."))
        assertThat(Day12.doGenerations(initialState, rules, 5).first, containsString("....#...##...#.#..#..#...#...#........."))
        assertThat(Day12.doGenerations(initialState, rules, 6).first, containsString("....##.#.#....#...#..##..##..##........"))
        assertThat(Day12.doGenerations(initialState, rules, 7).first, containsString("...#..###.#...##..#...#...#...#........"))
        assertThat(Day12.doGenerations(initialState, rules, 8).first, containsString("...#....##.#.#.#..##..##..##..##......."))
        assertThat(Day12.doGenerations(initialState, rules, 9).first, containsString("...##..#..#####....#...#...#...#......."))
        assertThat(Day12.doGenerations(initialState, rules, 10).first, containsString("..#.#..#...#.##....##..##..##..##......"))
        assertThat(Day12.doGenerations(initialState, rules, 11).first, containsString("...#...##...#.#...#.#...#...#...#......"))
        assertThat(Day12.doGenerations(initialState, rules, 12).first, containsString("...##.#.#....#.#...#.#..##..##..##....."))
        assertThat(Day12.doGenerations(initialState, rules, 13).first, containsString("..#..###.#....#.#...#....#...#...#....."))
        assertThat(Day12.doGenerations(initialState, rules, 14).first, containsString("..#....##.#....#.#..##...##..##..##...."))
        assertThat(Day12.doGenerations(initialState, rules, 15).first, containsString("..##..#..#.#....#....#..#.#...#...#...."))
        assertThat(Day12.doGenerations(initialState, rules, 16).first, containsString(".#.#..#...#.#...##...#...#.#..##..##..."))
        assertThat(Day12.doGenerations(initialState, rules, 17).first, containsString("..#...##...#.#.#.#...##...#....#...#..."))
        assertThat(Day12.doGenerations(initialState, rules, 18).first, containsString("..##.#.#....#####.#.#.#...##...##..##.."))
        assertThat(Day12.doGenerations(initialState, rules, 19).first, containsString(".#..###.#..#.#.#######.#.#.#..#.#...#.."))

        // extra tests for 20 :)
        val twentyGenerations = Day12.doGenerations(initialState, rules, 20)
        assertThat(twentyGenerations.first, containsString(".#....##....#####...#######....#.#..##."))
        assertThat(twentyGenerations.second, `is`(7))
        assertThat(Day12.totalPlantContainingPots(twentyGenerations), `is`(325L))
    }

    @Test
    fun testDay13CartsIntersectionBehaviour() {
        val cart = Day13.Cart('>', 0 to 0)
        assertThat(cart.intersectionBehaviour.next(), `is`('l'))
        assertThat(cart.intersectionBehaviour.next(), `is`('s'))
        assertThat(cart.intersectionBehaviour.next(), `is`('r'))
        assertThat(cart.intersectionBehaviour.next(), `is`('l'))
        assertThat(cart.intersectionBehaviour.next(), `is`('s'))
        assertThat(cart.intersectionBehaviour.next(), `is`('r'))
        assertThat(cart.intersectionBehaviour.next(), `is`('l'))
    }

    @Test
    fun testDay13SimpleCollision() {
        val initialState = Day13.initCarts(listOf(
            "|",
            "v",
            "|",
            "|",
            "|",
            "^",
            "|"
        ))

        Day13.doTick(initialState, 1)
        Day13.doTick(initialState, 2)
        assertThat(initialState.flatten().find { it.second != null && it.second!!.state == 'X' }!!.second!!.location, `is`(0 to 3))
    }

    @Test
    fun testDay13ComplexCollision() {
        val initialState = Day13.initCarts(listOf(
            """/->-\        """,
            """|   |  /----\""",
            """| /-+--+-\  |""",
            """| | |  | v  |""",
            """\-+-/  \-+--/""",
            """  \------/"""
        ))

        var iteration = 1
        while (initialState.flatten().none { it.second != null && it.second!!.state == 'X' }) {
            Day13.doTick(initialState, iteration++)
        }
        assertThat(initialState.flatten().find { it.second != null && it.second!!.state == 'X' }!!.second!!.location, `is`(7 to 3))
    }

    @Test
    fun testDay13CollisionRemoval() {
        val initialState = Day13.initCarts(listOf(
            """/>-<\  """,
            """|   |  """,
            """| /<+-\""",
            """| | | v""",
            """\>+</ |""",
            """  |   ^""",
            """  \<->/"""
        ))

        Day13.doTick(initialState, 1, true)
        Day13.doTick(initialState, 2, true)
        Day13.doTick(initialState, 3, true)

        assertThat(initialState.flatten().filter { it.second != null }, hasSize(1))
        assertThat(initialState.flatten().find { it.second != null }!!.second!!.location, `is`(6 to 4))
    }

    @Test
    fun testDay14ElfChooseNext() {
        val scoreboard = listOf(3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1)
        val elf = Day14.Elf(4)

        elf.chooseNext(scoreboard)
        assertThat(elf.indexOfCurrentRecipe, `is`(6))
        assertThat(scoreboard[elf.indexOfCurrentRecipe], `is`(1))

        elf.chooseNext(scoreboard)
        assertThat(elf.indexOfCurrentRecipe, `is`(8))
        assertThat(scoreboard[elf.indexOfCurrentRecipe], `is`(4))

        elf.chooseNext(scoreboard)
        assertThat(elf.indexOfCurrentRecipe, `is`(2))
        assertThat(scoreboard[elf.indexOfCurrentRecipe], `is`(1))
    }

    @Test
    fun testDay14PrettyPrint() {
        val elves = Day14.Elf(8) to Day14.Elf(4)
        val scoreboard = listOf(3, 7, 1, 0, 1, 0, 1, 2, 4, 5, 1)
        assertThat(Day14.scoreboardToString(elves, scoreboard), `is`("3  7  1  0  [1]  0  1  2  (4)  5  1"))
    }

    @Test
    fun testDay14Scoreboard() {
        val expectedScoreboards = listOf(
            "(3)  [7]",
            "(3)  [7]  1  0",
            "3  7  1  [0]  (1)  0",
            "3  7  1  0  [1]  0  (1)",
            "(3)  7  1  0  1  0  [1]  2",
            "3  7  1  0  (1)  0  1  2  [4]",
            "3  7  1  [0]  1  0  (1)  2  4  5",
            "3  7  1  0  [1]  0  1  2  (4)  5  1",
            "3  (7)  1  0  1  0  [1]  2  4  5  1  5",
            "3  7  1  0  1  0  1  2  [4]  (5)  1  5  8",
            "3  (7)  1  0  1  0  1  2  4  5  1  5  8  [9]",
            "3  7  1  0  1  0  1  [2]  4  (5)  1  5  8  9  1  6",
            "3  7  1  0  1  0  1  2  4  5  [1]  5  8  9  1  (6)  7",
            "3  7  1  0  (1)  0  1  2  4  5  1  5  [8]  9  1  6  7  7",
            "3  7  [1]  0  1  0  (1)  2  4  5  1  5  8  9  1  6  7  7  9",
            "3  7  1  0  [1]  0  1  2  (4)  5  1  5  8  9  1  6  7  7  9  2"
        )
        val elves = Day14.Elf(0) to Day14.Elf(1)
        var scoreboard = listOf(3, 7)

        for (i in 0 until 15) {
            assertThat(Day14.scoreboardToString(elves, scoreboard), `is`(expectedScoreboards[i]))
            scoreboard = Day14.foo(elves, scoreboard.toMutableList())
        }
    }

    @Test
    fun testDay14ScoresAfter() {
        assertThat(Day14.recipeScoresAfter(9), `is`("5158916779"))
        assertThat(Day14.recipeScoresAfter(5), `is`("0124515891"))
        assertThat(Day14.recipeScoresAfter(18), `is`("9251071085"))
        assertThat(Day14.recipeScoresAfter(2018), `is`("5941429882"))
    }

    @Test
    fun testDay14ScoresBefore() {
        assertThat(Day14.recipeScoresBefore("51589"), `is`(9))
        assertThat(Day14.recipeScoresBefore("01245"), `is`(5))
        assertThat(Day14.recipeScoresBefore("92510"), `is`(18))
        assertThat(Day14.recipeScoresBefore("59414"), `is`(2018))
    }

//    @Test
//    fun testDay15Arena() {
//        val arena = Arena.getArena(listOf(
//            "#######",
//            "#.G.E.#",
//            "#E.G.E#",
//            "#.G.E.#",
//            "#######"
//        ))
//
//        assertThat(arena.toString(), `is`("#######\n#.G.E.#\n#E.G.E#\n#.G.E.#\n#######\n"))
//        assertThat(arena.livingCombatants(), `is`(7))
//
//        assertThat(arena.getSurroundingTiles(0 to 0), containsInAnyOrder(
//            ArenaTile(0 to 1, '#'), ArenaTile(1 to 0, '#'), null, null
//        ))
//
//        assertThat(arena.getSurroundingTiles(3 to 2), containsInAnyOrder(
//            ArenaTile(3 to 1, '.'), ArenaTile(3 to 3, '.'), ArenaTile(2 to 2, '.'), ArenaTile(4 to 2, '.')
//        ))
//
//        assertThat(arena.getSurroundingTiles(4 to 4), containsInAnyOrder(
//            ArenaTile(3 to 4, '#'), ArenaTile(5 to 4, '#'), ArenaTile(4 to 3, 'E'), null
//        ))
//    }
//
//    @Test
//    fun testDay15Dijkstra() {
//        val arena = Arena.getArena(listOf(
//            "#######",
//            "#.G.E.#",
//            "#E...E#",
//            "#.G.E.#",
//            "#######"
//        ))
//
//        val elf = arena.get(4, 1)
//        val goblin = arena.get(2, 3)
//        val surrounding = arena.getSurroundingTiles(goblin.location)
//
//        val shortestPaths = surrounding.filterNotNull().mapNotNull { arena.dijkstra(elf, it) }
//
//        shortestPaths.map { it.path.map { it.location.toString() + " => " }.joinToString("") }.forEach { println(it) }
//
//        assertThat(shortestPaths, hasSize(4))
//        //assertThat(shortestPaths.map { it.path.map { it.location } }, containsInAnyOrder(
//        //    listOf(4 to 1, 4 to 2, 3 to 2, 2 to 2),
//        //    listOf(4 to 1, 4 to 2, 3 to 2, 2 to 2)
//        //))
//    }
//
//    @Test
//    fun testDay15Foo() {
//        val arena = Arena.getArena(listOf(
//            "#######",
//            "#.G.E.#",
//            "#E.G.E#",
//            "#.G.E.#",
//            "#######"
//        ))
//
//        println(arena)
//        Day15.doRound(arena)
//        println(arena)
//    }

    @Test
    fun testDay16Parsing() {
        val input = listOf(
            "Before: [0, 1, 2, 1]",
            "14 1 3 3",
            "After:  [0, 1, 2, 1]",
            "",
            "Before: [3, 2, 2, 3]",
            "13 2 1 3",
            "After:  [3, 2, 2, 1]",
            "",
            "Before: [2, 2, 2, 2]",
            "13 2 1 0",
            "After:  [1, 2, 2, 2]",
            ""
        )

        val samples = Day16.parseSamples(input)
        System.err.println(samples)
        assertThat(samples, containsInAnyOrder(
            Day16.Sample(intArrayOf(0, 1, 2, 1), intArrayOf(0, 1, 2, 1), 14, 1, 3, 3),
            Day16.Sample(intArrayOf(3, 2, 2, 3), intArrayOf(3, 2, 2, 1), 13, 2, 1, 3),
            Day16.Sample(intArrayOf(2, 2, 2, 2), intArrayOf(1, 2, 2, 2), 13, 2, 1, 0)
        ))
    }

    @Test
    fun testDay16Possibilities() {
        val sample = Day16.parseSamples(listOf(
            "Before: [3, 2, 1, 1]",
            "9 2 1 2",
            "After:  [3, 2, 2, 1]",
            ""
        )).first()
        val possibleInstructions = Day16.getPossibilities(sample)
        assertThat(possibleInstructions, containsInAnyOrder("mulr", "addi", "seti"))
    }

    @Test
    fun testDay18Areas() {
        val input = listOf(
            ".#.#...|#.",
            ".....#|##|",
            ".|..|...#.",
            "..|#.....#",
            "#.#|||#|#|",
            "...#.||...",
            ".|....|...",
            "||...#|.#|",
            "|.||||..|.",
            "...#.|..|."
        )

        val parsed = Day18.parseArea(input)
        assertThat(parsed[0][0], `is`(Day18.Acre('.', 0 to 0)))
        assertThat(parsed[1][0], `is`(Day18.Acre('#', 1 to 0)))
        assertThat(parsed[6][7], `is`(Day18.Acre('|', 6 to 7)))

        val middleAdjacent = getAdjacent(Day18.Acre('|', 6 to 7), parsed)
        assertThat(middleAdjacent, hasSize(8))
        assertThat(middleAdjacent, containsInAnyOrder(
            Day18.Acre('.', 5 to 6), Day18.Acre('|', 6 to 6), Day18.Acre('.', 7 to 6),
            Day18.Acre('#', 5 to 7), Day18.Acre('.', 7 to 7),
            Day18.Acre('|', 5 to 8), Day18.Acre('.', 6 to 8), Day18.Acre('.', 7 to 8)
        ))

        val edgeAdjacent = getAdjacent(Day18.Acre('.', 0 to 0), parsed)
        assertThat(edgeAdjacent, hasSize(3))
        assertThat(edgeAdjacent, containsInAnyOrder(
            Day18.Acre('#', 1 to 0), Day18.Acre('.', 1 to 1), Day18.Acre('.', 0 to 1)
        ))
    }

    @Test
    fun testDay18AreaMinutes() {
        val input = listOf(
            ".#.#...|#.",
            ".....#|##|",
            ".|..|...#.",
            "..|#.....#",
            "#.#|||#|#|",
            "...#.||...",
            ".|....|...",
            "||...#|.#|",
            "|.||||..|.",
            "...#.|..|."
        )

        var parsed = Day18.parseArea(input)
        (1..10).forEach { parsed = Day18.doMinute(parsed) }
        assertThat(parsed.flatten().count { it.isTrees() }, `is`(37))
        assertThat(parsed.flatten().count { it.isYard() }, `is`(31))
    }

    @Test
    fun testDay19Parsing() {
        val input = listOf(
            "#ip 0",
            "seti 5 0 1",
            "seti 6 0 2",
            "addi 0 1 0",
            "addr 1 2 3",
            "setr 1 0 0",
            "seti 8 0 4",
            "seti 9 0 5"
        )

        val instructions = input.drop(1).map { Day19.parseInstruction(it) }
        assertThat(instructions, contains(
            Day19.Instruction("seti", 5, 0, 1),
            Day19.Instruction("seti", 6, 0, 2),
            Day19.Instruction("addi", 0, 1, 0),
            Day19.Instruction("addr", 1, 2, 3),
            Day19.Instruction("setr", 1, 0, 0),
            Day19.Instruction("seti", 8, 0, 4),
            Day19.Instruction("seti", 9, 0, 5)
        ))
    }

    @Test
    fun testDay19() {
        val input = listOf(
            "#ip 0",
            "seti 5 0 1",
            "seti 6 0 2",
            "addi 0 1 0",
            "addr 1 2 3",
            "setr 1 0 0",
            "seti 8 0 4",
            "seti 9 0 5"
        )

        val out = Day19.runProgram(input)
        assertThat(out.second.toList(), contains(7, 5, 6, 0, 0, 9))
    }

    companion object {

        private val day10TestPoints = listOf(
            "position=< 9,  1> velocity=< 0,  2>", "position=< 7,  0> velocity=<-1,  0>", "position=< 3, -2> velocity=<-1,  1>", "position=< 6, 10> velocity=<-2, -1>", "position=< 2, -4> velocity=< 2,  2>", "position=<-6, 10> velocity=< 2, -2>",
            "position=< 1,  8> velocity=< 1, -1>", "position=< 1,  7> velocity=< 1,  0>", "position=<-3, 11> velocity=< 1, -2>", "position=< 7,  6> velocity=<-1, -1>", "position=<-2,  3> velocity=< 1,  0>", "position=<-4,  3> velocity=< 2,  0>",
            "position=<10, -3> velocity=<-1,  1>", "position=< 5, 11> velocity=< 1, -2>", "position=< 4,  7> velocity=< 0, -1>", "position=< 8, -2> velocity=< 0,  1>", "position=<15,  0> velocity=<-2,  0>","position=< 1,  6> velocity=< 1,  0>",
            "position=< 8,  9> velocity=< 0, -1>", "position=< 3,  3> velocity=<-1,  1>", "position=< 0,  5> velocity=< 0, -1>", "position=<-2,  2> velocity=< 2,  0>", "position=< 5, -2> velocity=< 1,  2>", "position=< 1,  4> velocity=< 2,  1>",
            "position=<-2,  7> velocity=< 2, -2>", "position=< 3,  6> velocity=<-1, -1>", "position=< 5,  0> velocity=< 1,  0>", "position=<-6,  0> velocity=< 2,  0>", "position=< 5,  9> velocity=< 1, -2>", "position=<14,  7> velocity=<-2,  0>",
            "position=<-3,  6> velocity=< 2, -1>"
        )
    }
}
