package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirection
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.mutableMatrix.get
import com.diutsu.aoc.library.mutableMatrix.set
import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    fun moveBox(
        warehouse: MutableMatrix<Char>,
        newPos: Reference,
        direction: CardinalDirection,
    ): Boolean {
        val moved =
            when (warehouse[newPos + direction]) {
                '.' -> true
                '#' -> false
                'O' -> moveBox(warehouse, newPos + direction, direction)
                else -> throw RuntimeException("Invalid check on ${warehouse[newPos + direction]}  $newPos $direction")
            }
        if (moved) {
            warehouse[newPos + direction] = 'O'
            warehouse[newPos] = '.'
        }
        return moved
    }

    fun canMoveWideBoxes(
        warehouse: MutableMatrix<Char>,
        boxPosLeft: Reference,
        boxPosRight: Reference,
        it: CardinalDirection,
    ): Boolean {
        val targetLeft = boxPosLeft + it
        val targetRight = boxPosRight + it
        when {
            warehouse[targetRight] == '#' || warehouse[targetLeft] == '#' -> return false
            warehouse[targetLeft] == '.' && warehouse[targetRight] == '.' -> return true
            warehouse[targetLeft] == '[' && warehouse[targetRight] == ']' ->
                return canMoveWideBoxes(warehouse, targetLeft, targetRight, it)
            else -> {
                val left =
                    when (warehouse[targetLeft]) {
                        ']' -> canMoveWideBoxes(warehouse, targetLeft + CardinalDirection.WEST, targetLeft, it)
                        '.' -> true
                        else -> false
                    }
                val right =
                    when (warehouse[targetRight]) {
                        '[' -> canMoveWideBoxes(warehouse, targetRight, targetRight + CardinalDirection.EAST, it)
                        '.' -> true
                        else -> false
                    }
                return left && right
            }
        }
    }

    fun moveWideBox(
        warehouse: MutableMatrix<Char>,
        boxLeft: Reference,
        boxRight: Reference,
        direction: CardinalDirection,
    ): Boolean {
        val targetLeft = boxLeft + direction
        val targetRight = boxRight + direction

        fun moveWide() {
            warehouse[boxLeft] = '.'
            warehouse[boxRight] = '.'
            warehouse[targetLeft] = '['
            warehouse[targetRight] = ']'
        }
        return when (direction) {
            CardinalDirection.EAST, CardinalDirection.WEST -> {
                val targetCell = if (direction == CardinalDirection.EAST) warehouse[targetRight] else warehouse[targetLeft]
                val moved =
                    when (targetCell) {
                        '.' -> true
                        '#' -> false
                        '[' -> moveWideBox(warehouse, targetLeft + direction, targetRight + direction, direction)
                        ']' -> moveWideBox(warehouse, targetLeft + direction, targetRight + direction, direction)
                        else -> throw IllegalStateException("Unknown value $targetCell in direction $direction")
                    }
                moved
            }
            CardinalDirection.NORTH, CardinalDirection.SOUTH -> {
                if (canMoveWideBoxes(warehouse, boxLeft, boxRight, direction)) {
                    if (warehouse[targetLeft] == '[') moveWideBox(warehouse, targetLeft, targetRight, direction)
                    if (warehouse[targetLeft] == ']') moveWideBox(warehouse, targetLeft + CardinalDirection.WEST, targetLeft, direction)
                    if (warehouse[targetRight] == '[') moveWideBox(warehouse, targetRight, targetRight + CardinalDirection.EAST, direction)
                    true
                } else {
                    false
                }
            }
        }.also { if (it) moveWide() }
    }

    fun computeGPS(warehouse: MutableMatrix<Char>) =
        warehouse.flatMapIndexed { lIndex, line ->
            line.mapIndexed { cIndex, col ->
                if (col == '[' || col == 'O') {
                    cIndex + 100 * lIndex
                } else {
                    0
                }
            }
        }.sum()

    fun readWarehouse(input: List<String>): Pair<MutableMatrix<Char>, Reference> {
        val warehouse: MutableMatrix<Char> =
            input.takeWhile { it.isNotEmpty() }
                .map { line -> line.map { it }.toMutableList() }
        val robotPos =
            warehouse.mapIndexedNotNull { lIndex, line ->
                line.mapIndexedNotNull { cIndex, it -> if (it == '@') Reference(cIndex, lIndex) else null }.singleOrNull()
            }.single()
        warehouse[robotPos] = '.' // robot is moving
        return Pair(warehouse, robotPos)
    }

    fun readRobotInputs(input: List<String>) =
        input.takeLastWhile { it.isNotEmpty() }
            .flatMap { line -> line.map { CardinalDirection.fromArrow(it) } }

    fun part1(input: List<String>): Int {
        val warehousePair = readWarehouse(input)
        val warehouse = warehousePair.first
        var robotPos = warehousePair.second
        val robotInput = readRobotInputs(input)
        robotInput.forEach {
            val moved =
                when (warehouse[robotPos + it]) {
                    '.' -> true
                    'O' -> moveBox(warehouse, robotPos + it, it)
                    else -> false
                }
            if (moved) {
                robotPos += it
            }
        }
        return computeGPS(warehouse)
    }

    fun part2(input: List<String>): Int {
        val warehousePair = readWarehouse(input)
        val warehouse =
            warehousePair.first
                .map { line ->
                    line.flatMap {
                        when (it) {
                            '#' -> listOf('#', '#')
                            'O' -> listOf('[', ']')
                            '.' -> listOf('.', '.')
                            else -> throw RuntimeException("Broken warehouse")
                        }
                    }.toMutableList()
                }
        var robotPos = Reference(warehousePair.second.x * 2, warehousePair.second.y)
        val robotInput = readRobotInputs(input)
        robotInput.forEach {
            val moved =
                when (warehouse[robotPos + it]) {
                    '.' -> true
                    '[' -> moveWideBox(warehouse, robotPos + it, robotPos + it + CardinalDirection.EAST, it)
                    ']' -> moveWideBox(warehouse, robotPos + it + CardinalDirection.WEST, robotPos + it, it)
                    else -> false
                }
            if (moved) robotPos += it
        }
        return computeGPS(warehouse)
    }

    val day = "day15"

    validateInput("$day-part1", 2028) {
        part1(readInput("$day/example"))
    }
    validateInput("$day-part1", 10092) {
        part1(readInput("$day/example2"))
    }
    runDay("$day-part1", 1538871) {
        part1(readInput("$day/input"))
    }
    validateInput("$day-part2", 9021) {
        part2(readInput("$day/example2"))
    }
    validateInput("$day-part2", 1138) {
        part2(readInput("$day/example3"))
    }
    runDay("$day-part2", 1543338) {
        part2(readInput("$day/input"))
    }
}
