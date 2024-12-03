package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun isSafe(levels: List<Int>): Pair<Boolean, Int> {
        val direction = if (levels[0] < levels[1]) 1 else -1
        for (index in (0..< levels.lastIndex)) {
            val diff = (levels[index + 1] - levels[index]) * direction
            if (diff > 3 || diff < 1 ) {
                return Pair(false, index + 1)
            }
        }
        return Pair(true, levels.lastIndex)
    }

    fun isSafeWithDampener(levels: List<Int>): Boolean {
        val firstPass = isSafe(levels)
        if (firstPass.first) return true

        val one = levels.subList(0, firstPass.second) + levels.subList(firstPass.second + 1, levels.size)
        if (isSafe(one).first) return true

        if (firstPass.second < 1) return false
        val two = levels.subList(0, firstPass.second - 1) + levels.subList(firstPass.second, levels.size)
        if (isSafe(two).first) return true

        if (firstPass.second < 2) return false
        val three = levels.subList(0, firstPass.second - 2) + levels.subList(firstPass.second - 1, levels.size)
        return isSafe(three).first
    }

    fun part1(input: List<String>): Int {
        return input.count { report ->
            val levels = report.split(" ").map(String::toInt)
            isSafe(levels).first
        }

    }

    fun part2(input: List<String>): Int {

        return input.count {
            report ->
            val levels = report.split(" ").map(String::toInt)
            isSafeWithDampener(levels)
        }
    }

    val day = "day02"

    validateInput( "$day-part1" , 2 ) {
        part1(readInput("$day/example"))
    }
    validateInput( "$day-part1" , 1 ) {
        part1(readInput("$day/example2"))
    }
    validateInput( "$day-part1" , 510 ) {
        part1(readInput("$day/input"))
    }
    runDay( "$day-part1" ) {
        part1(readInput("$day/input"))
    }

    //  part2
    validateInput( "$day-part2" , 4 ) {
        part2(readInput("$day/example"))
    }

    validateInput( "$day-part2" , 9 ) {
        part2(readInput("$day/example2"))
    }

    validateInput( "$day-part2" , 553 ) {
        part2(readInput("$day/input"))
    }

    runDay( "$day-part2" ) {
        part2(readInput("$day/input"))
    }
}
