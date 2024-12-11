package com.diutsu.aoc24

import com.diutsu.aoc.library.Matrix
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.graphTraverseDfs
import com.diutsu.aoc.library.graphTraverseGeneric
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    val neighbours: (Reference, Matrix<Char>) -> List<Reference> = { visiting, graph ->
        val expectedNext = graph[visiting.y][visiting.x] + 1
        visiting.plusAll().values.filter {
            it.y in graph.indices && it.x in graph.first().indices && graph[it.y][it.x] == expectedNext
        }
    }

    val newNeighbours: (Reference, Matrix<Char>, Set<Reference>) -> List<Reference> =
        { visiting, graph, visited -> neighbours(visiting, graph) - visited }

    val isEnd: (Reference, Matrix<Char>) -> Boolean =
        { visiting, graph -> graph[visiting.y][visiting.x] == '9' }

    fun findStarts(input: Matrix<Char>): List<Reference> =
        input.flatMapIndexed { lIndex, line ->
            line.mapIndexedNotNull { cIndex, col ->
                if (col == '0') Reference(cIndex, lIndex) else null
            }
        }

    fun part1(input: Matrix<Char>): Int {
        return findStarts(input).sumOf { point ->
            graphTraverseDfs(point, input, newNeighbours)
                .count { input[it.y][it.x] == '9' }
        }
    }

    fun part2(input: Matrix<Char>): Int {
        return findStarts(input).sumOf { point ->
            var count = 0
            graphTraverseGeneric(point, input, neighbours, isEnd) { count++ }
            count
        }
    }

    val day = "day10"

    validateInput("$day-part1", 2) {
        part1(readFileAsMatrix("$day/example"))
    }

    validateInput("$day-part1", 4) {
        part1(readFileAsMatrix("$day/example2"))
    }
    validateInput("$day-part1", 3) {
        part1(readFileAsMatrix("$day/example3"))
    }
    validateInput("$day-part1", 36) {
        part1(readFileAsMatrix("$day/example4"))
    }
    runDay("$day-part1", 717) {
        part1(readFileAsMatrix("$day/input"))
    }
    validateInput("$day-part1", 3) {
        part2(readFileAsMatrix("$day/example5"))
    }
    validateInput("$day-part1", 13) {
        part2(readFileAsMatrix("$day/example2"))
    }
    validateInput("$day-part1", 81) {
        part2(readFileAsMatrix("$day/example4"))
    }
    validateInput("$day-part1", 227) {
        part2(readFileAsMatrix("$day/example6"))
    }
    runDay("$day-part2", 1686) {
        part2(readFileAsMatrix("$day/input"))
    }
}
