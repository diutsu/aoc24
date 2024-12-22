package com.diutsu.aoc24

import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    val smemo = mutableMapOf<Pair<Pair<Char,Char>, Int>, Long>()

    val dpad = mapOf(
        '^' to (0 to 1), 'A' to (0 to 2),
        '<' to (1 to 0), 'v' to (1 to 1), '>' to (1 to 2))
        .mapValues { Reference(it.value.second, it.value.first) }

    val npad = mapOf(
        '7' to (0 to 0), '8' to (0 to 1), '9' to (0 to 2),
        '4' to (1 to 0), '5' to (1 to 1), '6' to (1 to 2),
        '1' to (2 to 0), '2' to (2 to 1), '3' to (2 to 2),
        '0' to (3 to 1), 'A' to (3 to 2))
        .mapValues { Reference(it.value.second, it.value.first) }

    fun move(
        start: Char,
        next: Char,
        pad: Map<Char, Reference>
    ): String {
        var startRef = pad[start]!!
        val goal = pad[next]!!
        val diff = goal - startRef
        val yp = if (diff.y > 0) "v".repeat(diff.y) else "^".repeat(diff.y * -1)
        val xp = if (diff.x > 0) ">".repeat(diff.x) else "<".repeat(diff.x * -1)
        val newPath = if (diff.x == 0) {
            yp + "A"
        } else if (diff.y == 0) {
            xp + "A"
        } else {
            val result = mutableListOf<String>()
            if (Reference(goal.x, startRef.y) in pad.values) {
                result.add(xp + yp + 'A')
            }
            if (Reference(startRef.x, goal.y) in pad.values) {
                result.add(yp + xp + 'A')
            }
            if (result.size != 2 || diff.x < 0) {
                result[0]
            } else {
                result[1]
            }
        }
        return newPath
    }

    fun numpadPath(startPath: String): String {
        var start = 'A'
        return startPath.toList().joinToString("") { goal ->
            move(start, goal, npad).also { start = goal }
        }
    }

    fun dpadPath2(start: Char, end: Char, robotN: Int) : Long {
        smemo[(start to end) to robotN]?.let { return it }
        return if(robotN == 1) {
            move(start, end, dpad).length.toLong()
        } else {
            val seq = move(start, end, dpad)
            seq.indices.sumOf { i ->
                if(i==0) {
                    dpadPath2('A', seq[i], robotN-1)
                } else {
                    dpadPath2(seq[i-1], seq[i], robotN-1)
                }
            }
        }.also { smemo[(start to end) to robotN] = it }
    }

    fun part2(input: List<String>, depth : Int): Long {
        return input.sumOf {
            val pad = numpadPath(it)
            val seq = "A$pad"
            val a = pad.indices.sumOf { i ->
                dpadPath2(seq[i], seq[i+1], depth)
            }
            a * it.dropLast(1).toInt()
        }
    }

    val day = "day21"

    validateInput( "$day-part1" , 126384) {
        part2(readInput("$day/example"), 2)
    }

    runDay( "$day-part1",278568) {
        part2(readInput("$day/input"), 2)
    }
    runDay( "$day-part2" ,341460772681012) {
        part2(readInput("$day/input"), 25)
    }
}
