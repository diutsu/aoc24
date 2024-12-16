package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirection
import com.diutsu.aoc.library.Matrix
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.Walker
import com.diutsu.aoc.library.get
import com.diutsu.aoc.library.graphSSP
import com.diutsu.aoc.library.graphTraverseDfs
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun findStart(input: Matrix<Char>): Pair<Reference, CardinalDirection> {
        input.forEachIndexed { lIndex, line ->
            line.forEachIndexed { cIndex, char ->
                if (char == 'S') return Reference(cIndex, lIndex) to CardinalDirection.EAST
            }
        }
        throw IllegalArgumentException("Start point not found")
    }

    fun graphAllPaths(
        start: Pair<Reference, CardinalDirection>,
        input: Matrix<Char>
    ): Map<Walker, Int> {
        val neighbours: (Walker, Matrix<Char>) -> Collection<Pair<Walker, Int>> = { curr, map ->
            curr.first.plusAll()
                .filter { it.key != curr.second.opposite() }
                .filter { map[it.value] != '#' }
                .map { it.value to it.key }
                .map { it to if (it.second == curr.second) 1 else 1001 }
        }
        val isEnd: (Walker, Matrix<Char>) -> Boolean = { curr, map -> map[curr.first] == 'E' }
        val visited = graphSSP(start, input, isEnd, neighbours)
        return visited
    }

    fun part1(input: Matrix<Char>): Long {
        val start = findStart(input)
        val visited = graphAllPaths(start, input)
        return visited.entries.first { input[it.key.first] == 'E' }.value.toLong()
    }

    fun part2(input: Matrix<Char>): Long {
        val start = findStart(input)
        val visited = graphAllPaths(start, input)
            .entries.groupBy({ it.key.first }, { it.value })

        val end = visited.entries.first { input[it.key] == 'E' }.let { it.key to it.value.min() }
        val reversedNeighbours: (Pair<Reference, Int>, Map<Reference,List<Int>>, Set<Pair<Reference, Int>>) -> Collection<Pair<Reference, Int>> = {
                (curr, currCost), map, _ ->
                curr.plusAll()
                    .filter { map.containsKey(it.value) }
                    .map { it.value }
                    .flatMap { direction ->
                        map[direction]!!
                            .filter { it == currCost - 1 || it == currCost - 1001 }
                            .map { direction to it }
                    }
        }

        return graphTraverseDfs(end, visited, reversedNeighbours)
            .plus(Pair(start.first, 0))
            .map { it.first }
            .distinct()
            .count()
            .toLong()
    }

    val day = "day16"

    validateInput( "$day-part1" , 7036 ) {
        part1(readFileAsMatrix("$day/example"))
    }
    validateInput( "$day-part1" , 11048 ) {
        part1(readFileAsMatrix("$day/example2"))
    }
    validateInput( "$day-part1" , 2008 ) {
        part1(readFileAsMatrix("$day/example3"))
    }
    runDay( "$day-part1",83444 ) {
        part1(readFileAsMatrix("$day/input"))
    }
    validateInput( "$day-part2" , 45 ) {
        part2(readFileAsMatrix("$day/example"))
    }
    validateInput( "$day-part2" , 64 ) {
        part2(readFileAsMatrix("$day/example2"))
    }
    validateInput( "$day-part2" , 15 ) {
        part2(readFileAsMatrix("$day/example3"))
    }
    runDay( "$day-part2", 483 ) {
        part2(readFileAsMatrix("$day/input"))
    }
}
