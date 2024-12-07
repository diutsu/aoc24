package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
import kotlin.math.log10
import kotlin.math.pow

enum class Operator {
    PLUS,
    TIMES,
    CONCAT,
}

fun main() {
    fun testValue(
        values: List<Long>,
        goal: Long,
        operators: Set<Operator>,
    ): Any {
        fun dfs(
            index: Int,
            runningResult: Long,
        ): Boolean {
            if (index == values.size) {
                return runningResult == goal
            }
            val nextValue = values[index]
            for (operator in operators) {
                val result =
                    when (operator) {
                        Operator.CONCAT -> {
                            val digits = if (nextValue == 0L) 1 else (log10(nextValue.toDouble()).toInt() + 1)
                            runningResult * 10.0.pow(digits).toLong() + nextValue
                        }
                        Operator.PLUS -> runningResult + nextValue
                        Operator.TIMES -> runningResult * nextValue
                    }
                if (result <= goal && dfs(index + 1, result)) {
                    return true
                }
            }
            return false
        }
        return if (dfs(0, 0L)) goal else 0
    }

    fun part1(input: List<String>): Long {
        val operators = setOf(Operator.PLUS, Operator.TIMES)
        return input.sumOf { line ->
            val (goal, values) =
                line.split(": ")
                    .let { (goal, values) -> goal.toLong() to values.split(" ").map(String::toLong) }
            if (testValue(values, goal, operators) == goal) goal else 0
        }
    }

    fun part2(input: List<String>): Long {
        val operators = Operator.entries.toSet()
        return input.sumOf { line ->
            val (goal, values) =
                line.split(": ")
                    .let { (goal, values) -> goal.toLong() to values.split(" ").map(String::toLong) }
            if (testValue(values, goal, operators) == goal) goal else 0
        }
    }

    val day = "day07"

    validateInput("$day-part1", 3749L) {
        part1(readInput("$day/example"))
    }
    runDay("$day-part1", 20281182715321L) {
        part1(readInput("$day/input"))
    }
    validateInput("$day-part2", 11387L) {
        part2(readInput("$day/example"))
    }
    runDay("$day-part2", 159490400628354) {
        part2(readInput("$day/input"))
    }
}
