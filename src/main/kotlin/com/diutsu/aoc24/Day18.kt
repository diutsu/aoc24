package com.diutsu.aoc24

import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.graphSSP
import com.diutsu.aoc.library.graphTraverseGeneric
import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun part1(input: List<String>, size: Int, firstB : Int): Int {

        val blocks = input.map { l ->
            l.split(",").map { it.toInt() }.let { (x, y) ->
                Reference(x, y)
            }
        }.take(firstB)
        val start = Reference(0, 0)
        val end = Reference(size, size)

        val paths = mutableListOf<Pair<Reference, Int>>()

//        for (i in 0 .. size) {
//            for (j in  0 .. size) {
//                if(Reference(j, i) in blocks) print('#') else
//                print('.')
//            }
//            println()
//        }
        val neighbours: (Reference, List<Reference>) -> List<Pair<Reference,Int>> = { visiting, graph ->
            visiting.plusAll().values
                .filter { it !in graph }
                .filter { it.x >= 0 && it.y >= 0 && it.x <= size && it.y <= size }
                .map { it to 1}
        }

        graphSSP(start, blocks,  neighbours = neighbours, { r, _ -> r == end }, { it, steps -> paths.add(it to steps) })

//        paths.map { it.second }.min()
        return paths.minOf { it.second }
    }

    fun part2(input: List<String>, size: Int, firstB : Int): Int {

        val blocks = input.map { l ->
            l.split(",").map { it.toInt() }.let { (x, y) ->
                Reference(x, y)
            }
        }
        val start = Reference(0, 0)
        val end = Reference(size, size)

        var i = firstB
        while(true) {
            val paths = mutableListOf<Pair<Reference, Int>>()
            val neighbours: (Reference, List<Reference>) -> List<Pair<Reference, Int>> = { visiting, graph ->
                visiting.plusAll().values
                    .filter { it !in graph }
                    .filter { it.x >= 0 && it.y >= 0 && it.x <= size && it.y <= size }
                    .map { it to 1 }
            }

            graphSSP(
                start,
                blocks.take(i),
                neighbours = neighbours,
                { r, _ -> r == end },
                { it, steps -> paths.add(it to steps) })
//            println(paths)
//            println(i)
//            println(blocks.take(i))
//            println("===")
            if(paths.isEmpty()) break
            else i++
        }
        println(blocks[i-1])
        return 0
    }

    val day = "day18"

    validateInput( "$day-part1" , 22 ) {
        part1(readInput("$day/example"), 6, 12)
    }
    runDay( "$day-part1" ) {
        part1(readInput("$day/input"), 70, 1024)
    }
    validateInput( "$day-part2" , 31 ) {
        part2(readInput("$day/example"), 6, 12)
    }
    runDay( "$day-part2" ) {
        part2(readInput("$day/input"), 70, 1024)
    }
}
