package com.diutsu.aoc25

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput
import java.lang.Math.pow
import kotlin.math.pow

fun main() {

    fun readRanges(input: List<String>): List<LongRange> {
        val ranges = input[0].split(",")
            .map { range ->
                range.split("-").map { it.toLong() }
                    .let { (a, b) -> a..b }
            }
        return ranges
    }

    fun part1(input: List<String>): Long {
        val ranges = readRanges(input)

        return ranges.sumOf { range ->
            range
//                .take(1)
                .filter { number ->
                val stringNr = number.toString()
                val halfLen = stringNr.length / 2.0
                val res = halfLen.takeIf { stringNr.length % 2 == 0 }?.let {
                    val decimal = 10.0.pow(it).toLong()
                    val b = number % decimal
                    val a = number / decimal
//                    println("$number = $a . $b")
                    a == b
                } ?: false
//                println("$number is $res")
                res
//                stringNr.length % 2 == 0
//                        && stringNr.subSequence(0, halfLen) == stringNr.subSequence(halfLen,stringNr.length)
            }.sum()
        }
    }

    fun part2(input: List<String>): Long {
        val ranges = readRanges(input)

        return ranges.sumOf { range ->
            val isValid = range
                .filter { number ->
                    val stringNr = number.toString()
                    val halfLen = stringNr.length / 2
                    (1 .. halfLen). any {
                        stringNr.chunked(it).toSet().size == 1
                    }
            }
//            println("$range has invalid ID in ${isValid}")
            isValid.sum()
        }
    }

    val day = "day02"

    validateInput( "$day-part1" , 1227775554 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1", 40214376723 ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 4174379265 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2" , 50793864718) {
        part2(readInput("$day/input"))
    }
}
