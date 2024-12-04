package com.diutsu.aoc24

import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.directions4x
import com.diutsu.aoc.library.directions8
import com.diutsu.aoc.library.get
import com.diutsu.aoc.library.inMatrix
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
        input: List<List<Char>>
    ) = directions8.count { direction ->
        var matches = true
        for (i in word.indices) {
            val newRow = row + direction.y * i
            val newCol = col + direction.x * i
            if (0 > newRow || newRow >= rows || 0 > newCol || newCol >= cols
                || (input[newRow][newCol] != word[i]
                        )
            ) {
                matches = false
                break
            }
        }
        matches
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
        val validOption = setOf("MMSS", "SMMS", "SSMM", "MSSM").map { it.toList() }
        return (1..input.size - 2)
            .sumOf { l ->
                (1..input.first().size - 2)
                    .filter { input[l][it] == 'A' }
                    .map { c -> directions4x.map { input[it.y + l][it.x + c] } }
                    .count { corners -> validOption.any { corners == it } }
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
