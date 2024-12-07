package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirections
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.mutableMatrix.get
import com.diutsu.aoc.library.mutableMatrix.println
import com.diutsu.aoc.library.mutableMatrix.contains
import com.diutsu.aoc.library.mutableMatrix.set
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

    fun walkWithLetteredBreadcrumbs(input: MutableMatrix<Char>, initialPosition: Reference, initialDirection: CardinalDirections): MutableList<Pair<Reference, CardinalDirections>> {
        var position = initialPosition
        var direction = initialDirection
        var next = position + direction
        val xIndices = input.first().indices
        val indices = input.indices
        val visited = mutableListOf<Pair<Reference,CardinalDirections>>()
        while (next.y in indices && next.x in xIndices) {
            when {
                input[next] == direction.letter -> return visited
                input[next] == '#' -> direction = direction.rotate90()
                else -> {
                    position = next
                    input[next] = direction.letter
                    visited.add(position to direction)
                }
            }
            next = position + direction
        }
        visited.add(next to direction)
        return visited
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
            val testInput = input.map { it.toMutableList() }
            testInput[obstacle] = '#'

            val walkResult = walkWithLetteredBreadcrumbs(testInput, position, direction)

            val count = walkResult.last().first in testInput

            if(count) {
                "---".println()
                testInput.println()
            }
            count
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

//    runDay( "$day-part2", 2165 ) {
//        part2(readFileAsMutableMatrix("$day/input"))
//    }
}
