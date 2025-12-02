package com.diutsu.aoc24

import com.diutsu.aoc.library.CardinalDirection
import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.mutableMatrix.set
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

    /**
     * Follows the input matrix from initial position and direction,
     *
     * Returns a list of each visited point and current direction of travel
     * It turns 90 degrees everytime a '#' is found
     */
    fun walkWithBreadcrumbs(
        input: MutableMatrix<Char>,
        initialPosition: Reference,
        initialDirection: CardinalDirection,
    ): MutableList<Pair<Reference, CardinalDirection>> {
        var positionX = initialPosition.x
        var positionY = initialPosition.y
        var direction = initialDirection
        val visited = mutableListOf(Reference(positionX, positionY) to direction)
        var nextX = positionX + direction.dx
        var nextY = positionY + direction.dy
        while (nextX in input.first().indices && nextY in input.indices) {
            when (input[nextY][nextX]) {
                direction.letter -> return visited
                '#' -> direction = direction.rotate90()
                else -> {
                    positionY = nextY
                    positionX = nextX
                    input[nextY][nextX] = direction.letter
                    visited.add(Reference(positionX, positionY) to direction)
                }
            }
            nextX = positionX + direction.dx
            nextY = positionY + direction.dy
        }

        return visited
    }

    /**
     * Follows the input matrix from initial position and direction,
     *
     * Updates breadcrumbs with each visited point, but not the direction of travel
     * It turns 90 degrees everytime a '#' is found
     *
     * Faster than [walkWithBreadcrumbs] as it only cares about the visited point. Taking 30ms less.
     */
    fun fastWalkWithBreadcrumbs(
        input: MutableMatrix<Char>,
        initialPosition: Reference,
        initialDirection: CardinalDirection,
        height: Int,
        width: Int,
        breadcrumbs: MutableList<Reference>,
    ): Boolean {
        var positionX = initialPosition.x
        var positionY = initialPosition.y
        var direction = initialDirection
        var nextX = positionX + direction.dx
        var nextY = positionY + direction.dy

        while (nextX in 0 until width && nextY in 0 until height) {
            when (input[nextY][nextX]) {
                direction.letter -> return true
                '#' -> direction = direction.rotate90()
                else -> {
                    // Save original state before modifying
                    val nextPos = Reference(nextX, nextY)
                    breadcrumbs.add(nextPos)
                    positionY = nextY
                    positionX = nextX
                    input[nextY][nextX] = direction.letter
                }
            }
            nextX = positionX + direction.dx
            nextY = positionY + direction.dy
        }

        return false
    }

    fun day06part1(input: MutableMatrix<Char>): Int {
        val changes = mutableListOf<Reference>()
        fastWalkWithBreadcrumbs(input, findInitialPosition(input), CardinalDirection.NORTH, input.size, input.first().size, changes)
        // marginally faster than doing a distinctBy or a map toSet
        return input.sumOf { line -> line.count { it != '.' && it != '#' } }
    }

    fun day06part2(input: MutableMatrix<Char>): Int {
        val position = findInitialPosition(input)
        val direction = CardinalDirection.NORTH
        val originalMatrix = input.map { it.toMutableList() } // Copy once for safe modification
        val testObstacles =
            walkWithBreadcrumbs(input.map { it.toMutableList() }, position, direction)
                .distinctBy { it.first }

        val height = input.size
        val width = input.first().size
        var nextStart = position
        var nextDirection = direction
        val changes = mutableListOf<Reference>()
        return testObstacles.count { obstacle ->
            val (obstaclePos, _) = obstacle
            // Set the obstacle
            originalMatrix[obstaclePos] = '#'
            // Prepare to track changes made during fastWalk
            val walkResult = fastWalkWithBreadcrumbs(originalMatrix, nextStart, nextDirection, height, width, changes)
            // Revert the obstacle change
            originalMatrix[obstaclePos.y][obstaclePos.x] = '.'
            // Revert all changes made during fastWalk
            changes.forEach { originalMatrix[it.y][it.x] = '.' }
            changes.clear()
            nextStart = obstacle.first
            nextDirection = obstacle.second
            walkResult
        }
    }

    val day = "day06"

    validateInput("$day-part1", 41) {
        day06part1(readFileAsMutableMatrix("$day/example"))
    }

    runDay("$day-part1", 5531) {
        day06part1(readFileAsMutableMatrix("$day/input"))
    }

    validateInput("$day-part2", 6) {
        day06part2(readFileAsMutableMatrix("$day/example"))
    }

    runDay("$day-part2", 2165) {
        day06part2(readFileAsMutableMatrix("$day/input"))
    }
}
