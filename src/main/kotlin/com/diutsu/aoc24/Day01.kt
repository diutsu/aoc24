package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.stressTest
import com.diutsu.aoc.library.validateInput

fun main() {
    fun part1(input: List<String>): Int {
        val group1 = mutableListOf<Int>()
        val group2 = mutableListOf<Int>()

        input.forEach {
            val (a, b) = it.split("   ").map(String::toInt)
            group1.add(a)
            group2.add(b)
        }
        group1.sort()
        group2.sort()

        return group1.indices.sumOf { Math.abs(group1[it] - group2[it]) }
    }

    fun part2(input: List<String>): Int {
        val group1 = mutableListOf<Int>()
        val map2 = mutableMapOf<Int, Int>()

        input.forEach {
            val (a, b) = it.split("   ").map(String::toInt)
            group1.add(a)
            map2[b] = map2.getOrDefault(b, 0) + 1
        }

        return group1.sumOf { it * map2.getOrDefault(it, 0) }
    }

    val day = "day01"

    val example =
        """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
        """.trimIndent()

    validateInput("$day-part1-inline", 11) {
        part1(example.lines())
    }
    validateInput("$day-part2-inline", 31) {
        part2(example.lines())
    }

    runDay("$day-part1") {
        part1(readInput("$day/input"))
    }

    runDay("$day-part2") {
        part2(readInput("$day/input"))
    }

    stressTest("$day-part2", 100, 100) {
        part2(readInput("$day/input"))
    }
}
