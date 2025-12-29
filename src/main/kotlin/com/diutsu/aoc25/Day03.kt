package com.diutsu.aoc25

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun accumulate(bank: IntArray, batteries: Int): Long {
        var idx = 0
        var acc = 0L
        var allowedEnd = bank.size - (batteries - 1) // initial allowedEnd
        (batteries downTo 1).forEach { _ ->
            var best = Int.MIN_VALUE
            var bestIdx = idx

            var i = idx
            while (i < allowedEnd) {
                val v = bank[i]
                if (v > best) {
                    best = v
                    bestIdx = i
                }
                i++
            }

            idx = bestIdx + 1
            acc = acc * 10 + best
            allowedEnd++ // next step allows one more element
        }

        return acc
    }


    fun part1(input: List<String>): Long {
        return input
            .map { it.toCharArray().map { c -> c.digitToInt() }.toIntArray() }
            .sumOf { accumulate(it, 2) }
    }


    fun part2(input: List<String>): Long {
        return input
            .map { it.toCharArray().map { c -> c.digitToInt() }.toIntArray() }
            .sumOf { accumulate(it, 12) }


    }

    val day = "day03"

    validateInput( "$day-part1" , 357 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1" , 17179) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 3121910778619 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2", 170025781683941 ) {
        part2(readInput("$day/input"))
    }
}
