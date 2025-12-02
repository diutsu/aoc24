package com.diutsu.aoc24

import com.diutsu.aoc.library.Matrix
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.contains
import com.diutsu.aoc.library.findChar
import com.diutsu.aoc.library.get
import com.diutsu.aoc.library.graphSSPWithBT
import com.diutsu.aoc.library.readFileAsMatrix
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    fun neighbours(
        r: Reference,
        map: Matrix<Char>,
    ) = r.plusAll().filter { it.value in map && map[it.value] != '#' }.map { it.value to 1 }

    fun around(
        start: Reference,
        costs: Map<Reference, Int>,
        checkingArea: Set<Pair<Reference, Int>>,
        target: Int,
    ): Int {
        val costCheck = target + costs[start]!!
        return checkingArea.count { (point, distance) ->
            (costs[point + start]?.let { it - distance } ?: 0) >= costCheck
        }
    }

    fun nodesDiamond(maxSteps: Int): Set<Pair<Reference, Int>> {
        val pointsInRadius = mutableSetOf<Pair<Reference, Int>>()
        var currentNodes = setOf(Reference(0, 0))
        var steps = 0

        while (steps <= maxSteps && currentNodes.isNotEmpty()) {
            pointsInRadius.addAll(currentNodes.map { it to steps })
            currentNodes =
                currentNodes.flatMap { it.plusAll().values }
                    .filterNot { it in pointsInRadius.map { pair -> pair.first } }
                    .toSet()
            steps++
        }
        return pointsInRadius
    }

    fun part2(
        input: Matrix<Char>,
        maxSteps: Int,
        target: Int,
    ): Int {
        val start = input.findChar('S')
        val end = input.findChar('E')

        val (costs, path) =
            graphSSPWithBT(
                start,
                input,
                neighbours = { r, _ -> neighbours(r, input) },
                end,
                acc = { _, _ -> },
            )

        val nodes = nodesDiamond(maxSteps)
        return path.sumOf { around(it, costs, nodes, target) }
    }

    val day = "day20"

    validateInput("$day-part1", 44) {
        part2(readFileAsMatrix("$day/example"), 2, 2)
    }

    runDay("$day-part1", 1402) {
        part2(readFileAsMatrix("$day/input"), 2, 100)
    }

    validateInput("$day-part2", 285) {
        part2(readFileAsMatrix("$day/example"), 20, 50)
    }
    // 246054 too low
    // 244622
    runDay("$day-part2") {
        part2(readFileAsMatrix("$day/input"), 20, 100)
    }
}
