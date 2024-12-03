package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
import java.util.regex.MatchResult
import java.util.regex.Pattern

fun main() {

    fun part1(input: List<String>): Int {
        val pattern = Regex("mul\\([0-9]{1,3},[0-9]{1,3}\\)")
        return input.flatMap { line ->
            pattern.findAll(line).map { it.value }.map {
                val (a,b) = it.substring(4, it.length-1).split(",")
                a.toInt() * b.toInt()
            }
        }.sum()

    }

    fun part2(input: List<String>): Int {
        val pattern = Regex("((do|don't)\\(\\))|(mul\\([0-9]{1,3},[0-9]{1,3}\\))")
        var enabled = true
        var acc = 0
        input.forEach { line ->
            pattern.findAll(line).map { it.value }.forEach {
                when {
                    it == "do()" -> enabled = true
                    it == "don't()" -> enabled = false
                    it.startsWith("mul(") -> if (enabled) {
                        val (a, b) = it.substring(4, it.length-1).split(",")
                        acc += a.toInt() * b.toInt()
                    }
                }
            }
        }
        return acc
    }

    val day = "day03"

    validateInput( "$day-part1" , 161 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1",156388521 ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 48 ) {
        part2(readInput("$day/example2"))
    }
    runDay( "$day-part2" ,75920122 ) {
        part2(readInput("$day/input"))
    }
}
