package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun games(input: List<String>, extra: Long):Long = input.chunked(4).sumOf {
        val (a1, a2) = it[0].split(": ")[1].split(", ").map { it.drop(2).toInt() }
        val (b1, b2) = it[1].split(": ")[1].split(", ").map { it.drop(2).toInt() }
        val (p1, p2) = it[2].split(": ")[1].split(", ").map { it.drop(2).toInt() + extra}
        // Solve:
        // a1 * x + b1 * y = p1
        // a2 * x + b2 * y = p2
        val x = (b1 * p2 - b2 * p1) / (a2 * b1 - a1 * b2)
        val y = (a2 * p1 - a1 * p2) / (a2 * b1 - a1 * b2)
        // validate if solution is in problems space:
        if (a2 * x + b2 * y == p2 && a1 * x + b1 * y == p1) {
            x * 3 + y
        } else {
            0
        }
    }

    fun part1(input: List<String>): Long {
       return games(input, 0)
    }

    fun part2(input: List<String>): Long {
       return games(input,10000000000000)
    }

    val day = "day13"

    validateInput( "$day-part1" , 480) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1",40069 ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 875318608908 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2",71493195288102 ) {
        part2(readInput("$day/input"))
    }
}
