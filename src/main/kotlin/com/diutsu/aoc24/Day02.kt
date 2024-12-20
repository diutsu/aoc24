package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.stressTest
import com.diutsu.aoc.library.validateInput

fun main() {
    fun isSafe(levels: List<Int>): Pair<Boolean, Int> =
        (if (levels[0] < levels[1]) 1 else -1).let { direction ->
            levels.zipWithNext { a, b -> (b - a) * direction }
                // The difference times direction is a positive number as long as the numbers are in the same direction
                .indexOfFirst { it > 3 || it < 1 }
                .let { it: Int ->
                    if (it > -1) {
                        Pair(false, it + 1)
                    } else {
                        Pair(true, levels.lastIndex)
                    }
                }
        }

    fun isSafeWithDampener(levels: List<Int>): Boolean =
        // if the first pass fails, we need to try to remove one of the elements and try again
        // The naive way is to try with each one of levels, but if we remember the index of the first failure
        // there are only 3 possible causes for a failure: the current level, the previous index or the one before that.
        isSafe(levels).let { firstPass ->
            firstPass.first ||
                sequenceOf(firstPass.second, firstPass.second - 1, firstPass.second - 2)
                    .filter { it >= 0 }
                    .any { index ->
                        isSafe(levels.subList(0, index) + levels.subList(index + 1, levels.size)).first
                    }
        }

    fun part1(input: List<String>): Int = input.count { isSafe(it.split(" ").map(String::toInt)).first }

    fun part2(input: List<String>): Int = input.count { isSafeWithDampener(it.split(" ").map(String::toInt)) }

    val day = "day02"

    validateInput("$day-part1-example", 2) {
        part1(readInput("$day/example"))
    }

//    validateInput( "$day-part1" , 1 ) {
//        part1(readInput("$day/example2"))
//    }

    stressTest("$day-part1-input", 10, 100) {
        part1(readInput("$day/input"))
    }

    //  part2
    validateInput("$day-part2-example", 4) {
        part2(readInput("$day/example"))
    }

//    validateInput( "$day-part2" , 9 ) {
//        part2(readInput("$day/example2"))
//    }

    stressTest("$day-part2-input", 10, 100) {
        part2(readInput("$day/input"))
    }
}
