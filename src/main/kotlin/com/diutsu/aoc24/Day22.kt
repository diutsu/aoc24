package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun generate(number: Int, n: Int): MutableList<Int> {
        val seq = mutableListOf<Int>()
        var x = number
        repeat(n) {
            x = x xor (x shl 6) and 0xffffff
            x = x xor (x shr 5) and 0xffffff
            x = x xor (x shl 11) and 0xffffff
            seq.add(x)
        }
        return seq
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { buyer ->
            generate(buyer.toInt(), 2000).last().toLong()
        }
    }

    fun part2(input: List<String>): Int {
        val allSequences = mutableMapOf<List<Int>,Int>()
        input.map { buyer ->
            val prices = generate(buyer.toInt(), 2000).map { it % 10 }
            val buyerSequence = mutableSetOf<List<Int>>()
            val changes = prices.zipWithNext { a, b -> b - a }
            changes.windowed(4).forEachIndexed { index, window ->
                if(buyerSequence.add(window)) {
                    allSequences[window] = (allSequences[window] ?: 0) + prices[index + 4]
                }
            }
        }
        return allSequences.values.max()
    }

    val day = "day22"

    validateInput( "$day-part1" , 37327623 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1", 17577894908) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 23 ) {
        part2(readInput("$day/example2"))
    }

    runDay( "$day-part2" , 1931) {
        part2(readInput("$day/input"))
    }
}
