package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    fun blinker(
        depth: Int,
        stone: String,
        cache: MutableMap<Pair<String, Int>, Long>,
    ): Long {
        if (depth == 0) return 1L
        val cached = cache[stone to depth]
        if (cached != null) return cached

        val result =
            when {
                stone == "0" -> blinker(depth - 1, "1", cache)
                stone.length % 2 == 0 -> {
                    val mid = stone.length / 2
                    val first = stone.substring(0, mid)
                    val second = stone.substring(mid).toLong().toString()
                    blinker(depth - 1, first, cache) + blinker(depth - 1, second, cache)
                }
                else -> {
                    blinker(depth - 1, (stone.toLong() * 2024).toString(), cache)
                }
            }
        cache[stone to depth] = result
        return result
    }

    fun blinkStones(
        input: List<String>,
        totalBlinks: Int,
    ): Long {
        val cache = mutableMapOf<Pair<String, Int>, Long>()
        return input.first().split(" ")
            .sumOf { blinker(totalBlinks, it, cache) }
    }

    fun part1(input: List<String>): Long {
        return blinkStones(input, 25)
    }

    fun part2(input: List<String>): Long {
        return blinkStones(input, 75)
    }

    val day = "day11"

    validateInput("$day-part1", 55312L) {
        part1(readInput("$day/example"))
    }
    runDay("$day-part1", 199986L) {
        part1(readInput("$day/input"))
    }
    validateInput("$day-part2", 65601038650482L) {
        part2(readInput("$day/example"))
    }
    runDay("$day-part2", 236804088748754L) {
        part2(readInput("$day/input"))
    }
}
