package days

import days.Day4.Record.RecordType.GUARD
import days.Day4.Record.RecordType.SLEEP
import days.Day4.Record.RecordType.WAKE
import java.time.LocalDateTime

class Day4 : Day(4) {

    private val sortedRecords: List<Record> = inputList.map { Record(it) }.sortedBy { it.dateTime }
    private val guards: Set<Guard> = sortedRecords
        .filter { it.type == GUARD }
        .fold(setOf()) { guards, record -> guards.plus(Guard(record)) }

    private lateinit var currentGuard: Guard

    // add all minutes asleep for all guards. do in init as both parts use it
    init {
        sortedRecords.forEach { record ->
                when {
                    record.type == GUARD -> currentGuard = guards.find { it.id == Guard(record).id }!!
                    record.type == SLEEP -> currentGuard.minutesAsleep.add(MinuteAsleep(record.dateTime))
                    record.type == WAKE -> {
                        val minutesSinceLastWentToSleep = record.dateTime.minute - currentGuard.minutesAsleep.last().minute.toLong()
                        (minutesSinceLastWentToSleep - 1 downTo 1).forEach {
                            currentGuard.minutesAsleep.add(MinuteAsleep(record.dateTime.minusMinutes(it)))
                        }
                    }
                }
            }
    }

    override fun partOne(): Any {
        val guardMostMinsAsleep = guards.sortedBy { it.minutesAsleep.size }.last()
        val minuteMostAsleep = guardMostMinsAsleep.mostFrequentMinute()?.key
        return guardMostMinsAsleep.id * (minuteMostAsleep?:0)
    }

    override fun partTwo(): Any {
        val guardMostFreqAsleep = guards.sortedBy { it.mostFrequentMinute()?.value }.last()
        val minuteMostAsleep = guardMostFreqAsleep.mostFrequentMinute()?.key
        return guardMostFreqAsleep.id * (minuteMostAsleep?:0)
    }

    class Record(recordString: String) {
        val dateTime = LocalDateTime.parse(recordString.substring(recordString.indexOf('[') + 1, recordString.indexOf(']')).replace(' ', 'T'))
        val recordContent = recordString.substring(recordString.indexOf(']') + 2)
        val type = if (recordContent.startsWith("Guard")) GUARD else if (recordContent == "falls asleep") SLEEP else WAKE

        enum class RecordType {
            GUARD, SLEEP, WAKE
        }
    }

    class Guard(record: Record) {
        val id = record.recordContent.substring(record.recordContent.indexOf('#') + 1, record.recordContent.indexOf(" begins")).toInt()
        val minutesAsleep: MutableSet<MinuteAsleep> = mutableSetOf()

        fun mostFrequentMinute(): Map.Entry<Int, Int>? {
            return minutesAsleep.groupingBy { it.minute }.eachCount().maxBy { it.value }
        }
    }

    data class MinuteAsleep(val dateTime: LocalDateTime) {
        val minute = dateTime.minute
    }
}
