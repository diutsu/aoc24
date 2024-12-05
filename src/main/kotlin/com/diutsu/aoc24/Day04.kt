package com.diutsu.aoc24

import com.diutsu.aoc.library.directions4x
import com.diutsu.aoc.library.directions8
import com.diutsu.aoc.library.get
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    val word = "XMAS".toList()

    fun searchWord(
        row: Int,
        col: Int,
        rows: Int,
        cols: Int,
        input: List<List<Char>>,
    ): Int {
        return directions8.count { direction ->
            val maxStep =
                when {
                    direction.y > 0 -> rows - row
                    direction.y < 0 -> row + 1
                    else -> Int.MAX_VALUE
                }.coerceAtMost(
                    when {
                        direction.x > 0 -> cols - col
                        direction.x < 0 -> col + 1
                        else -> Int.MAX_VALUE
                    },
                )

            if (maxStep < word.size) return@count false

            for (i in word.indices) {
                val newRow = row + direction.y * i
                val newCol = col + direction.x * i
                if (input[newRow][newCol] != word[i]) return@count false
            }
            true
        }
    }

    fun part1(input: List<List<Char>>): Int {
        val rows = input.size
        val cols = input.first().size

        return (0 until rows).sumOf { row ->
            (0 until cols)
                .filter { input[row][it] == 'X' }
                .sumOf { col ->
                    searchWord(row, col, rows, cols, input)
                }
        }
    }

    fun part2(input: List<List<Char>>): Int {
        val validOptions = setOf("MMSS", "SMMS", "SSMM", "MSSM").map { it.toCharArray() }

        val rows = input.size
        val cols = input.first().size

        return (1 until rows - 1).sumOf { l ->
            (1 until cols - 1)
                .filter { input[l][it] == 'A' }
                .count { c ->
                    val corners =
                        charArrayOf(
                            input[l + directions4x[0].y][c + directions4x[0].x],
                            input[l + directions4x[1].y][c + directions4x[1].x],
                            input[l + directions4x[2].y][c + directions4x[2].x],
                            input[l + directions4x[3].y][c + directions4x[3].x],
                        )
                    validOptions.any { it.contentEquals(corners) }
                }
        }
    }

    val day = "day04"

    validateInput("$day-part1", 18) {
        part1(readFileAsMatrix("$day/example"))
    }

    runDay("$day-part1", 2514) {
        part1(readFileAsMatrix("$day/input"))
    }

    validateInput("$day-part2", 9) {
        part2(readFileAsMatrix("$day/example"))
    }
    // 1213 too low
    runDay("$day-part2", 1888) {
        part2(readFileAsMatrix("$day/input"))
    }
}
