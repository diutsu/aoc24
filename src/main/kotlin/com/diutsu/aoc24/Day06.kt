package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirections
import com.diutsu.aoc.library.Matrix
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.inMatrix
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.mutableMatrix.get
import com.diutsu.aoc.library.mutableMatrix.set
import com.diutsu.aoc.library.mutableMatrix.println
import com.diutsu.aoc.library.println
import com.diutsu.aoc.library.readFileAsMutableMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun findInitialPosition(input: MutableMatrix<Char>): Reference {
        for (line in input.indices) {
            for (column in input.indices) {
                if (input[line][column] == '^') {
                    return Reference(column, line)
                }
            }
        }
        throw RuntimeException("Position not found exception")
    }

    fun part1(input: MutableMatrix<Char>): Int {
        var position = findInitialPosition(input)
        var direction = CardinalDirections.NORTH
        input[position] = 'X'
        try {
            while (true) {
                val next = position + direction
                if(input[next] == '#') {
                    direction = direction.rotate90()
                } else {
                    position = next
                    input[position] = 'X'
                }
            }
        } catch (ex: Exception ) {
            println(ex)
        }
        return input.sumOf { line -> line.count { it == 'X' } }
    }

    fun trywalk(input: MutableMatrix<Char>, initialPosition: Reference, direction: CardinalDirections): Boolean {
        var position = initialPosition
        var direction = CardinalDirections.NORTH
        try {
            while (true) {
                val next = position + direction
                if(input[next] == direction.letter() ) {
                    return true
                }
                if(input[next] == '#') {
                    direction = direction.rotate90()
                } else {
                    position = next
                    input[position] = direction.letter()
                }
            }
        } catch (ex: Exception ) {
            println(ex)
        }
        return false
    }

    fun part2(input: MutableMatrix<Char>): Int {
        val position = findInitialPosition(input)
        val direction = CardinalDirections.NORTH

        val testObstacles = input.flatMapIndexed { index, line -> List(line.size) { cIndex -> Reference(cIndex,index) } }
        return testObstacles.count {
            val prev = input[it]
            if(prev == '.') {
                input[it] = '#'
                val result = trywalk(input.map { it.toMutableList() }, position, direction)
                input[it] = '.'
                result
            } else false
        }
    }

    val day = "day06"

    validateInput( "$day-part1" , 41 ) {
        part1(readFileAsMutableMatrix("$day/example"))
    }

    runDay( "$day-part1" ) {
        part1(readFileAsMutableMatrix("$day/input"))
    }
    validateInput( "$day-part2" , 31 ) {
        part2(readFileAsMutableMatrix("$day/example"))
    }
    // 13963 too high
    runDay( "$day-part2" ) {
        part2(readFileAsMutableMatrix("$day/input"))
    }
}
