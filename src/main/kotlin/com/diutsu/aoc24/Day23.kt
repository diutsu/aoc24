package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun part1(input: List<String>): Int {
        val edges = input
            .flatMap { line ->
                val (from, to) = line.split("-")
                listOf(from to to, to to from)
            }
            .groupBy({ it.first }, { it.second })

        val triplets = HashSet<Triple<String, String, String>>()
        edges.forEach { (from, neighbors) ->
            neighbors.forEachIndexed { i, second ->
                val secondNeighbors = edges[second]!!
                for (j in i + 1 until neighbors.size) {
                    val third = neighbors[j]
                    if (third in secondNeighbors) {
                        val sortedTriplet = listOf(from, second, third).sorted()
                        triplets.add(Triple(sortedTriplet[0], sortedTriplet[1], sortedTriplet[2]))
                    }
                }
            }
        }
        return triplets.count { (a, b, c) -> a.first() == 't' || b.first() == 't' || c.first() == 't' }
    }

    fun part2(input: List<String>): Int {
        val edges = input.flatMap { it.split("-").let { (from, to) -> listOf(from to to, to to from) } }
            .groupBy({ it.first }, { it.second })
            .mapValues { (from, neighbors) -> (neighbors + from).toSet() }

        val networks = mutableSetOf(edges.keys.toSet())
        edges.forEach { (from, target) ->
            networks.filter{ it.contains(from)}.forEach { contained ->
                if(contained.any { !target.contains(it) } ) {
                    networks.remove(contained)
                    networks.add(contained - from)
                    networks.add(contained.filter { it == from || target.contains(it) }.toSet())
                }
            }
        }
        val lan =  networks.maxBy { it.size }.sorted()
        println(lan.joinToString(","))
        return lan.size
    }

    val day = "day23"

    validateInput( "$day-part1" , 7 ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1", 1154 ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 4 ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2", 13 ) {
        part2(readInput("$day/input"))
    }
}
