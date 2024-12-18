package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val day = "day18"

    validateInput( "$day-part1" , 11 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1" ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 31 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2" ) {
        part2(readInput("$day/input"))
    }
}
