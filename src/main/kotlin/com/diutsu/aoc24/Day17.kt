package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import kotlin.math.pow
import kotlin.math.truncate

fun main() {
    fun runProgram(
        ip: Int,
        program: List<Int>,
        regA: Long,
        regB: Long,
        regC: Long,
    ): List<Long> {
        var ip1 = ip
        var regA1 = regA
        var regB1 = regB
        var regC1 = regC
        val output = mutableListOf<Long>()
        while (ip1 < program.size) {
            val op = program[ip1]
            val combo = program[ip1 + 1]
            val comboValue =
                when (combo) {
                    4 -> regA1
                    5 -> regB1
                    6 -> regC1
                    7 -> throw RuntimeException("Unknown combo value $combo")
                    else -> combo.toLong()
                }
            when (op) {
                0 -> regA1 = truncate(regA1 / 2.0.pow(comboValue.toDouble())).toLong()
                1 -> regB1 = regB1 xor combo.toLong()
                2 -> regB1 = comboValue.mod(8L)
                3 -> if (regA1 > 0) ip1 = (combo - 2)
                4 -> regB1 = regB1 xor regC1
                5 -> output.add(comboValue.mod(8L))
                6 -> regB1 = truncate(regA1 / 2.0.pow(comboValue.toDouble())).toLong()
                7 -> regC1 = truncate(regA1 / 2.0.pow(comboValue.toDouble())).toLong()
                else -> throw RuntimeException("Unknown op code $op")
            }

//            println("OP $op  $combo -> A: $regA1, B: $regB1, C: $regC1, IP: $ip1")
            ip1 += 2
        }
        return output.toList()
    }

    fun part1(input: List<String>): Int {
        var regA = input[0].split(": ")[1].toLong()
        var regB = input[1].split(": ")[1].toLong()
        var regC = input[2].split(": ")[1].toLong()

        val program = input[4].split(": ")[1].split(",").map { it.toInt() }

        val result = runProgram(0, program, regA, regB, regC)
        println(result.joinToString(","))
        return input.size
    }

    fun part2(input: List<String>): Long {
        var regA = 0L
        var regB = input[1].split(": ")[1].toLong()
        var regC = input[2].split(": ")[1].toLong()

        val programInput = input[4].split(": ")[1]
        val program = programInput.split(",").map { it.toInt() }

//        var result = runProgram(0, program, regA, regB, regC)

//        println(program)
//        println(program.size)
//        println("$regA -> ${result.size} $result")
//        while (result != program && regA < 1050) {
//            regA++
//            result = runProgram(0, program, regA, regB, regC)
//            println("$regA -> ${result.size} $result")
//        }

        val toVisit = ArrayDeque<Pair<Long, List<Int>>>().apply { add(0L to program) }
        val out = mutableSetOf<Long>()
        while (toVisit.isNotEmpty()) {
            val (a, missingProgram) = toVisit.removeFirst()

            if (missingProgram.isEmpty()) {
                out.add(a)
                continue
            }
            val goal = missingProgram.last()
            println("In search of A that results in $goal. in $a / $missingProgram. To visit: $toVisit ")

            for (i in 0 until 8) {
                val output = runProgram(0, program, 8L * a + i, 0, 0)
                if (output.first().toInt() == goal) {
                    println("found ${8 * a + i} -> $output matching goal. ")
                    toVisit.add(8 * a + i to missingProgram.dropLast(1))
                }
            }
        }
        return out.min()
    }

    val day = "day17"

//    validateInput( "$day-part1" , 11 ) {
//        part1(readInput("$day/example"))
//    }
//    validateInput( "$day-part1" , 11 ) {
//        part1(readInput("$day/example2"))
//    }
//    validateInput( "$day-part1" , 11 ) {
//        part1(readInput("$day/example3"))
//    }
//    runDay( "$day-part1" ) {
//        part1(readInput("$day/input"))
//    }
//    validateInput( "$day-part2" , 117440 ) {
//        part2(readInput("$day/example4"))
//    }
    runDay("$day-part2") {
        part2(readInput("$day/input"))
    }
}
