package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.timeIt

fun main() {
    fun printListHighlight(
        v: List<Int>,
        value: Int,
        key: Int,
    ) {
        val highlightedV =
            v.map { if (it == value || it == key) "\u001B[31m$it\u001B[0m" else it } // Bold matching value
        println("   $key|$value in $highlightedV")
    }

    fun isValid(
        update: List<Int>,
        rules: Map<Int, List<Int>>,
    ): Boolean {
        val seen = mutableSetOf<Int>()
        return update.all { i ->
            if (rules[i]?.any { it in seen } == true) {
                false
            } else {
                seen.add(i)
                true
            }
        }
    }

    fun invalidRules(
        update: List<Int>,
        rules: Map<Int, List<Int>>,
    ): List<Pair<Int, Int>> {
        val seen = mutableSetOf<Int>()
        val invalid = mutableListOf<Pair<Int, Int>>()
        for (i in update) {
            rules[i]?.forEach { rule ->
                if (rule in seen) {
                    invalid.add(i to rule)
                }
            }
            seen.add(i)
        }
        return invalid
    }

    fun part1(input: List<String>): Int {
        val rules =
            input
                .takeWhile { '|' in it }
                .map { it.split("|").let { (a, b) -> a.toInt() to b.toInt() } }
                .groupBy({ it.first }, { it.second })
        val updates =
            input.takeLastWhile { !it.contains("|") }.filter { it.isNotEmpty() }
                .map { line -> line.split(",").map { it.toInt() } }

        return updates.filter { isValid(it, rules) }
            .map { it[it.size / 2] }
            .sum()
    }

    fun part2(input: List<String>): Int {

        val rules = timeIt("read rules"){
                input
                    .takeWhile { '|' in it }
                    .map { it.split("|").let { (a, b) -> a.toInt() to b.toInt() } }
                    .groupBy({ it.first }, { it.second })
        }

        val updates = timeIt("read updates") {
            input.takeLastWhile { !it.contains("|") }.filter { it.isNotEmpty() }
                .map { line -> line.split(",").map { it.toInt() } }
        }
        //                val applicableRules = rules.filter {
        //                    it.key in invalid
        //                }.flatMap { r ->
        //                    r.value.filter { it in invalid }.map { r.key to it }
        //                }

        val invalid = timeIt("Filter Invalid") {
            updates.mapNotNull { val invalidRules = invalidRules(it, rules)
                if(invalidRules.isNotEmpty()) it to invalidRules else null
            }
        }

        return timeIt("Sort") {
            invalid.map { (iv, invalidRules) ->
                val newIv = iv.toMutableList()
                var newRules = invalidRules
                do {
                    println(newRules)
                    newRules.forEach { rule ->
                        val newIndex = newIv.indexOf(rule.first)
                        newIv.remove(rule.second)
                        newIv.add(newIndex, rule.second)
                    }
                    println(newRules)
                    newRules = invalidRules(newIv, rules)
                } while ( invalidRules.isNotEmpty() )
                newIv
            }
        }.sumOf { it[it.size / 2] }
    }

    val day = "day05"

//    validateInput("$day-part1", 143) {
//        part1(readInput("$day/example"))
//    }

    runDay("$day-part1", 5064) {
        part1(readInput("$day/input"))
    }
//    validateInput("$day-part2", 123) {
//        part2(readInput("$day/example"))
//    }
    runDay("$day-part2",5152) {
        part2(readInput("$day/input"))
    }
}
