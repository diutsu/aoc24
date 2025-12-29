package com.diutsu.aoc25

import com.diutsu.aoc.library.Matrix
import com.diutsu.aoc.library.mutableMatrix.set
import com.diutsu.aoc.library.get
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.contains
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.readFileAsMutableMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    val around = listOf(
        Reference(-1, -1),
        Reference(-1, 0),
        Reference(-1, 1),
        Reference(0, -1),
        Reference(0, 1),
        Reference(1, -1),
        Reference(1, 0),
        Reference(1, 1)
    )

    fun part1(input: Matrix<Char>): Int {
        return input.flatMapIndexed { lIndex, line ->
            line.mapIndexed { cIndex, ch ->
                if( ch == '@') {
                    val pos = Reference(cIndex, lIndex)
                    val count = around.map { pos + it }.count {
                        input.contains(it) && input[it] == '@'
                    }
                    count < 4
                } else false
            }
        }.count { it }

    }

    fun part2(input: MutableMatrix<Char>): Int {

        var removedTotal = 0
        val toRemove = ArrayList<Reference>(1024)

        while (true) {
            toRemove.clear()

            for (y in 0 until input.size) {
                for (x in 0 until input.size) {
                    val pos = Reference(x, y)
                    if (input[pos] != '@') continue

                    var neighbors = 0
                    for (d in around) {
                        val p = pos + d
                        if (input.contains(p) && input[p] == '@') neighbors++
                    }

                    if (neighbors < 4) toRemove.add(pos)
                }
            }

            if (toRemove.isEmpty()) {
                break
            }
            for (p in toRemove) {
                input[p] = 'x'
            }
            removedTotal += toRemove.size
        }

        return removedTotal
    }

    val day = "day04"

    validateInput( "$day-part1" , 13 ) {
        part1(readFileAsMatrix("$day/example"))
    }
    runDay( "$day-part1" , 1445) {
        part1(readFileAsMatrix("$day/input"))
    }
    validateInput( "$day-part2" , 43 ) {
        part2(readFileAsMutableMatrix("$day/example"))
    }
    runDay( "$day-part2" , 8317) {
        part2(readFileAsMutableMatrix("$day/input"))
    }
}
