package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    fun part1(input: List<String>): Int {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        input.chunked(8).map { block ->
            if (block[0] == "#####") {
                val lock = mutableListOf(-1, -1, -1, -1, -1)
                block.forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '#') lock[index] += 1
                    }
                }
                keys.add(lock.toList())
            } else {
                val lock = mutableListOf(-1, -1, -1, -1, -1)
                block.forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '.') lock[index] += 1
                    }
                }
                locks.add(lock.toList())
            }
        }
        var acc = 0
        keys.forEach { k ->
            locks.forEach { l ->
                val m = l.filterIndexed { lI, lv -> k[lI] <= lv }
                if (m.size >= 5) {
                    acc += 1
                }
            }
        }
        return acc
    }

    fun part2(input: List<String>): Int {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        input.chunked(8).map { block ->
            if (block[0] == "#####") {
                val lock = mutableListOf(-1, -1, -1, -1, -1)
                block.forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '#') lock[index] += 1
                    }
                }
                keys.add(lock.toList())
            } else {
                val lock = mutableListOf(-1, -1, -1, -1, -1)
                block.forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '.') lock[index] += 1
                    }
                }
                locks.add(lock.toList())
            }
        }
        var acc = 0
        keys.forEach { k ->
            locks.forEach { l ->
                val m = l.filterIndexed { lI, lv -> k[lI] <= lv }
                if (m.size >= 5) {
                    acc += 1
                }
            }
        }
        return acc
    }

    val day = "day25"

    validateInput("$day-part1", 3) {
        part1(readInput("$day/example"))
    }
    runDay("$day-part1", 3196) {
        part1(readInput("$day/input"))
    }
    validateInput("$day-part2", 31) {
        part2(readInput("$day/example"))
    }
    runDay("$day-part2") {
        part2(readInput("$day/input"))
    }
}
