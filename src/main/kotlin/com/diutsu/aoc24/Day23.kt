package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {


    fun part1(input: List<String>): Int {
        val edges = input
            .map { line -> line.split("-") }
            .flatMap { (from, to) -> listOf(listOf(from, to), listOf(to, from)) }
            .groupBy { it[0] }
            .mapValues { it.value.map { it[1] } }
//        edges.forEach(::println)
//        println()

        val tripples = edges.flatMap { (from, to) ->
            to.flatMap { second ->
                to.filter { it != second && (edges[second]?.contains(it) ?: false) }
                    .map { third -> Triple(from, second, third) }
            }
        }.map { (a,b,c) -> listOf(a,c,b).sorted().let { Triple(it[0], it[1], it[2]) }
        }.sortedWith(compareBy({ it.first }, { it.second }, { it.third }))
            .distinct()

//        tripples.forEach { (from, to, third) ->
//            println("$from,$to,$third")
//        }

        val r = tripples.filter { (a, b, c) -> a.first() == 't' || b.first() == 't' || c.first() == 't' }
//        println(r)
        return r.count()
    }

    fun part2(input: List<String>): Int {
        val edges = input
            .map { line -> line.split("-") }
            .flatMap { (from, to) -> listOf(listOf(from, to), listOf(to, from)) }
            .groupBy { it[0] }
            .mapValues { it.value.map { it[1] }.sorted() }
//        edges.forEach(::println)
//        println()

        val networks = HashSet<Set<String>>()
        networks.add(edges.keys.toSet())
        edges.forEach { (from, target) ->
            val containing = networks.filter{ it.contains(from)}
            containing.forEach { c ->
//                println("Processing From $from To $target, checking in connected $c")
                if(c.any { !target.contains(it) } ) {
                    networks.remove(c)
                    val notContained = c - from
                    val contained = c.filter { it == from || target.contains(it) }.toSet()
//                    println(". Split in $contained and $notContained")
                    if(notContained.size > 2) networks.add(notContained)
                    if(contained.size > 2) networks.add(contained)
                }
            }
//            target.forEach { to ->
//
//                val connected = containing.filter { it.contains(to) }.toSet()
//                networks.removeAll(connected)
//                val newNetwork = connected + containing
//                networks.add(newNetwork)
//            }
        }
//        networks.forEach { v -> v.sorted().joinToString(",").println() }
        val lan =  networks.maxBy { it.size }.sorted()
        println(lan.joinToString(","))
        return lan.size
    }

    val day = "day23"

//    validateInput( "$day-part1" , 7 ) {
//        part1(readInput("$day/example"))
//    }
//    runDay( "$day-part1" ) {
//        part1(readInput("$day/input"))
//    }
    validateInput( "$day-part2" , 4 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2", 13 ) {
        part2(readInput("$day/input"))
    }
}
