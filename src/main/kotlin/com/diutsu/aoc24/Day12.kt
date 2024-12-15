package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirection
import com.diutsu.aoc.library.Matrix
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.contains
import com.diutsu.aoc.library.get
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
import java.util.*

fun main() {
    fun walkSameFrom(
        i: Int,
        j: Int,
        test: Char,
        input: List<List<Char>>,
        visited: MutableMatrix<Boolean>,
    ): Pair<Int, Int> {
        if (visited[i][j]) return 0 to 0
        visited[i][j] = true
        val neighbours = Reference(j, i).plusAll().values
        val perimeter = neighbours.count { it !in input || input[it] != test }
        val area = 1
        return neighbours.filter { it in input && input[it] == test && !visited[it] }
            .map { walkSameFrom(it.y, it.x, test, input, visited) }
            .fold(area to perimeter) { acc, pair -> acc.first + pair.first to acc.second + pair.second }
    }

    fun walkSameFromWithCorners(
        i: Int,
        j: Int,
        test: Char,
        input: List<List<Char>>,
        visited: MutableMatrix<Boolean>,
    ): Pair<Int, Int> {
        fun matches(a: Reference) = a in input && input[a] == test

        fun getNumberOfCorners(point: Reference): Int {
            var numberOfCorners = 0
            val matches = Array(3) { BooleanArray(3) }
            val value = input[point.y][point.x]
            // Populate the matches array
            for (rOffset in 0..2) {
                for (cOffset in 0..2) {
                    val comparePoint = point + Reference(cOffset - 1, rOffset - 1)
                    matches[rOffset][cOffset] = input.contains(comparePoint) && input[comparePoint] == value
                }
            }

            val a = numberOfCorners
//            println("TEsting $point with $test")
//            matches.forEach {
//                println(it.map { if (it) "T" else "F" })
//            }
            // Top row checks
            if (matches[0][1]) {
                if (matches[1][0] && !matches[0][0]) {
                    numberOfCorners++
                }
                if (matches[1][2] && !matches[0][2]) {
                    numberOfCorners++
                }
            } else {
                if (!matches[1][0]) numberOfCorners++
                if (!matches[1][2]) numberOfCorners++
            }

            // Bottom row checks
            if (matches[2][1]) {
                if (matches[1][2] && !matches[2][2]) {
                    numberOfCorners++
                }
                if (matches[1][0] && !matches[2][0]) {
                    numberOfCorners++
                }
            } else {
                if (!matches[1][0]) numberOfCorners++
                if (!matches[1][2]) numberOfCorners++
            }
//            println("Found ${numberOfCorners-a} corners")
            return numberOfCorners
        }

        fun countCorners(
            reference: Reference,
            entry: EnumMap<CardinalDirection, Reference>,
        ): Int {
            return CardinalDirection.entries.count { dir ->
                val nextToIt = reference + dir
                if (matches(nextToIt)) {
                    matches(nextToIt + dir.rotate90()) || matches(nextToIt + dir.rotate270())
                } else {
                    !matches(reference + dir.opposite())
                }
            }
        }

        val curr = Reference(j, i)
        if (visited[i][j]) return 0 to 0
        visited[i][j] = true
        val neighbours = Reference(j, i).plusAll()

        val perimeter = getNumberOfCorners(curr)
        val area = 1
        return neighbours.values.filter { matches(it) && !visited[it] }
            .map { walkSameFromWithCorners(it.y, it.x, test, input, visited) }
            .fold(area to perimeter) { acc, pair -> acc.first + pair.first to acc.second + pair.second }
    }

    fun part1(input: Matrix<Char>): Long {
        val visited = input.map { it.map { false }.toMutableList() }
        var acc = 0L
        for (i in input.indices) {
            for (j in input.first().indices) {
                if (visited[i][j]) {
                    continue
                } else {
                    val (perimeter, area) = walkSameFrom(i, j, input[i][j], input, visited)
                    acc += perimeter * area
                }
            }
        }
        return acc
    }

    fun part2(input: Matrix<Char>): Long {
        val visited = input.map { it.map { false }.toMutableList() }
        var acc = 0L
        for (i in input.indices) {
            for (j in input.first().indices) {
                if (visited[i][j]) {
                    continue
                } else {
                    val (area, perimeter) = walkSameFromWithCorners(i, j, input[i][j], input, visited)
//                    println("Found $perimeter x $area on $i $j -> ${input[i][j]}")
                    acc += perimeter * area
                }
            }
        }
        return acc
    }

    val day = "day12"

//    validateInput( "$day-part1" , 1930 ) {
//        part1(readFileAsMatrix("$day/example"))
//    }
//    validateInput( "$day-part1" , 772 ) {
//        part1(readFileAsMatrix("$day/example2"))
//    }
//    runDay( "$day-part1",1396562 ) {
//        part1(readFileAsMatrix("$day/input"))
//    }
    validateInput("$day-part2", 1206) {
        part2(readFileAsMatrix("$day/example"))
    }

    validateInput("$day-part2", 236) {
        part2(readFileAsMatrix("$day/example3"))
    }
    // 1393924 too high
    runDay("$day-part2") {
        part2(readFileAsMatrix("$day/input"))
    }
}
