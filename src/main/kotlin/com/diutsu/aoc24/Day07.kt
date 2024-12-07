package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
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
        fun countDigits(n: Long): Long {
            return when {
                n < 10L -> 10L
                n < 100L -> 100L
                n < 1000L -> 1000L
                n < 10000L -> 10000L
                n < 100000L -> 100000L
                n < 1000000L -> 1000000L
                n < 10000000L -> 10000000L
                n < 100000000L -> 100000000L
                n < 1000000000L -> 1000000000L
                n < 10000000000L -> 10000000000L
                n < 100000000000L -> 100000000000L
                n < 1000000000000L -> 1000000000000L
                n < 10000000000000L -> 10000000000000L
                n < 100000000000000L -> 100000000000000L
                else -> {
                    10.0.pow(n.toString().length).toLong() // fallback for larger numbers
                }
            }
        }

        fun concatDirect(
            runningResult: Long,
            nextValue: Long,
        ): Long {
            var multiplier = 1L
            var temp = nextValue
            while (temp > 0) {
                multiplier *= 10
                temp /= 10
            }
            return runningResult * multiplier + nextValue
        }

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
                        Operator.CONCAT -> runningResult * countDigits(nextValue) + nextValue
//                        Operator.CONCAT -> concatDirect(runningResult, nextValue)
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
                    .let { (goal, values) -> goal.toLong() to values.split(" ") }
            if (testValue(values.map(String::toLong), goal, operators) == goal) goal else 0
        }
    }

    fun part2(input: List<String>): Long {
        val operators = Operator.entries.toSet()
        return input.sumOf { line ->
            val (goal, values) =
                line.split(": ")
                    .let { (goal, values) -> goal.toLong() to values.split(" ") }
            if (testValue(values.map(String::toLong), goal, operators) == goal) goal else 0
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
