package com.diutsu.aoc25

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
import kotlin.math.abs

fun main() {

    fun readRotations(input: List<String>): List<Int> {
        val rotations = input.map {
            val dir = if (it.first() == 'L') -1 else 1
            val value = it.substring(1).toInt()
            dir * value
        }
        return rotations
    }


    fun part1(input: List<String>): Int {
        var counter = 0
        val rotations = readRotations(input)
        rotations.fold(50) { acc, r  ->
            val n = (r + acc) % 100
            if (n == 0) counter++
            n
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        var counter = 0
        val rotations = readRotations(input)
        var acc = 50 // initial position
        for ( r in rotations) {
            val newPosition = r + acc
            val fullRotations = abs(newPosition) / 100
            val zeroTicks = fullRotations + if(newPosition == 0 || newPosition * acc < 0) 1 else 0
            if (zeroTicks > 0) counter+=zeroTicks
            acc = newPosition % 100
        }
        return counter
    }

    val day = "day01"

//    validateInput( "$day-part1" , 3 ) {
//        part1(readInput("$day/example"))
//    }
//    runDay( "$day-part1" , 1023) {
//        part1(readInput("$day/input"))
//    }
    validateInput( "$day-part2" , 6 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2", 5899) {
        part2(readInput("$day/input"))
    }
}
