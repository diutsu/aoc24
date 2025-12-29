package com.diutsu.aoc25

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
import java.lang.Long.min
import kotlin.math.max

fun main() {

    fun part1(input: List<String>): Int {
        val ranges = input.takeWhile { it.isNotEmpty() }.map {
            val (a,b) = it.split('-')
            LongRange(a.toLong(), b.toLong())
        }

        val inValues= input.drop(ranges.size+1).map { it.toLong() }
            .filter { v -> ranges.any { it.contains(v)} }

        return inValues.size
    }

    fun mergeRanges(ranges: List<LongRange>): MutableSet<LongRange> {
        val mergedRanges = emptySet<LongRange>().toMutableSet()
        for (range in ranges) {
            val overlap = mergedRanges.filter {
                it.contains(range.first) || it.contains(range.last)
                        || range.contains(it.first) || range.contains(it.last)
            }.toSet()
            mergedRanges += if (overlap.isEmpty()) {
                range
            } else {
                val totalFirst = min(overlap.minBy { it.first }.first, range.first)
                val totalLast = max(overlap.maxBy { it.last }.last, range.last)
                mergedRanges -= overlap
                LongRange(totalFirst, totalLast)
            }
        }
        return mergedRanges
    }

    fun part2(input: List<String>): Long {

        val ranges = input.takeWhile { it.isNotEmpty() }.map {
            val (a,b) = it.split('-')
            LongRange(a.toLong(), b.toLong())
        }

        val mergedRanges = mergeRanges(ranges).toSet()

        return mergedRanges.sumOf { it.last - it.first + 1}
    }

    val day = "day05"

    validateInput( "$day-part1" , 3 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1" , 690) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 14 ) {
        part2(readInput("$day/example"))
    }
    validateInput( "$day-part2" , 14 ) {
        part2(readInput("$day/example2"))
    }
    runDay( "$day-part2", 344323629240733) {
        part2(readInput("$day/input"))
    }
}
