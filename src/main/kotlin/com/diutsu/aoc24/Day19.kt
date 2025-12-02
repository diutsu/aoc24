package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    fun checkTowel(
        towel: String,
        patterns: List<String>,
    ): Int {
        val toVisit = mutableListOf(towel)
        val visited = mutableMapOf<String, Int>() // Memoization map to track counts
        var totalCount = 0 // Total count of matches

        while (toVisit.isNotEmpty()) {
            val curr = toVisit.removeLast()

            if (curr in visited) {
                totalCount += visited[curr]!! // Add previously computed count
                continue
            }

            val applicable = patterns.filter { curr.startsWith(it) }
            var currentCount = 0 // Count matches for the current substring

            applicable.forEach {
                val rem = curr.substring(it.length)
                if (rem.isEmpty()) {
                    currentCount++ // Increment for a valid match
                } else {
                    toVisit.add(rem) // Add remaining substring to process
                }
            }

            visited[curr] = currentCount // Cache the count for this substring
            totalCount += currentCount
        }

        return totalCount
    }

    fun checkTowelCount(
        towel: String,
        patterns: List<String>,
    ): Long {
        val memo = mutableMapOf<String, Long>()

        fun helper(current: String): Long {
            if (current.isEmpty()) return 1L
            if (current in memo) return memo[current]!!

            val applicable = patterns.filter { current.startsWith(it) }
            var count = 0L

            for (pattern in applicable) {
                val rem = current.substring(pattern.length)
                count += helper(rem)
            }

            memo[current] = count
            return count
        }

        return helper(towel)
    }

//    fun part1(input: List<String>): Int {
//        val patterns = input.first().split(", ")
//
//        val towels = input.drop(2)
//
//        val cache = mutableMapOf<String,Boolean>()
//        return towels.count { towel ->
//            checkTowel(towel, patterns).also {
//                println("Towel $towel is ${if(it) "good" else "bad"}")
//            }}
//    }

    fun part2(input: List<String>): Long {
        val patterns = input.first().split(", ")

        val towels = input.drop(2)

        val cache = mutableMapOf<String, Boolean>()
        return towels.sumOf { towel ->
            checkTowelCount(towel, patterns).also {
                println("Towel $towel has $it matches")
            }
        }
    }

    val day = "day19"

//    validateInput( "$day-part1" , 6 ) {
//        part1(readInput("$day/example"))
//    }
//    runDay( "$day-part1" ) {
//        part1(readInput("$day/input"))
//    }
    validateInput("$day-part2", 16) {
        part2(readInput("$day/example"))
    }
    runDay("$day-part2") {
        part2(readInput("$day/input"))
    }
}
