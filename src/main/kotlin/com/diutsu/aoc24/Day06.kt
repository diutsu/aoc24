package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirections
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
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
            val column = input[line].indexOf('^')
            if (column >= 0) return Reference(column, line)
        }
        throw Exception("Initial position not found")
    }

    fun walkWithLetteredBreadcrumbs(input: MutableMatrix<Char>, initialPosition: Reference, initialDirection: CardinalDirections): Boolean {
        var position = initialPosition
        var direction = initialDirection
        var next = position+direction
        while (next.inside(input)) {
            when {
                input[next] == direction.letter() -> return true
                input[next] == '#' -> direction = direction.rotate90()
                else -> {
                    position = next
                    input[position] = direction.letter()
                }
            }
            next = position + direction
        }
        return false
    }

    fun walkWithBreadcrumbs(input: MutableMatrix<Char>, initialPosition: Reference, initialDirection: CardinalDirections): MutableMatrix<Char> {
        walkWithLetteredBreadcrumbs(input,initialPosition,initialDirection)
        return input
    }

    fun part1(input: MutableMatrix<Char>): Int {
        walkWithBreadcrumbs(input,findInitialPosition(input), CardinalDirections.NORTH)
        return input.sumOf { line -> line.count { it != '.'  && it != '#' }}

    }

    fun part2(input: MutableMatrix<Char>): Int {
        val position = findInitialPosition(input)
        val direction = CardinalDirections.NORTH
        val testObstacles = walkWithBreadcrumbs(input.map { it.toMutableList() },position,direction)
            .flatMapIndexed { lIndex, l ->
                l.mapIndexedNotNull {  cIndex, c ->
                    if( c != '.' && c != '#' ) Reference(cIndex,lIndex) else null
            }
        }

        return testObstacles.count {obstacle ->
            if(input[obstacle] == '.') {
                val testInput = input.map { it.toMutableList() }
                testInput[obstacle] = '#'
                walkWithLetteredBreadcrumbs(testInput, position, direction)
            } else false
        }
    }

    val day = "day06"

    validateInput( "$day-part1" , 41 ) {
        part1(readFileAsMutableMatrix("$day/example"))
    }

    runDay( "$day-part1", 5531 ) {
        part1(readFileAsMutableMatrix("$day/input"))
    }

    validateInput( "$day-part2" , 6 ) {
        part2(readFileAsMutableMatrix("$day/example"))
    }

    runDay( "$day-part2", 2165 ) {
        part2(readFileAsMutableMatrix("$day/input"))
    }
}
