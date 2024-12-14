package com.diutsu.aoc24

import com.diutsu.aoc.library.ReferenceLong
import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun printMap(
        spaceY: Long,
        spaceX: Long,
        inSpace: List<ReferenceLong>
    ) {
        val map = (0..spaceY).map { (0..spaceX).map { 0 }.toMutableList() }
        inSpace.forEach { map[it.y.toInt()][it.x.toInt()]++ }
        map.forEachIndexed { index, line ->
            line.forEach {
                if (it == 0) print(".")
                else print(it)
            }
            println ()
        }
    }

    fun navigate(robots: List<Pair<ReferenceLong, ReferenceLong>>, time: Long, space: ReferenceLong): Long {
        val halfX = space.x / 2
        val halfY = space.y / 2
        val finalPositions = robots.map { (initial, velocity) -> (initial + velocity * time).mod(space) }
        var q1=0L
        var q2=0L
        var q3=0L
        var q4=0L
        finalPositions.forEach { (x,y) ->
            when {
                x == halfX || y == halfY -> return@forEach
                x < halfX && y < halfY -> q1++
                x < halfX -> q4++
                y < halfY -> q2++
                else -> q3++
            }
        }
        return q1 * q2 * q3 * q4
    }


    fun findEgg(robots: List<Pair<ReferenceLong, ReferenceLong>>, space: ReferenceLong): Long {
        var time = 1
        var positions = robots.map { it.first  }
        while (time < 10000) { // assume its before this
            val newPositions = positions.mapIndexed{ index, it -> it + robots[index].second.mod(space) }
            val groupedByY = newPositions.groupBy { it.y }
            if (groupedByY.count { it.value.size > 30 } >= 2) {
                val groupedByX = newPositions.groupBy { it.x }
                if (groupedByX.count { it.value.size > 30 } >= 2) {
                    return time.toLong()
                }
            }
            positions = newPositions
            time++
        }
        return 0
    }

    fun readRobots(input: List<String>): List<Pair<ReferenceLong, ReferenceLong>> {
        val robots = input.map { line ->
            val (p, v) = line.split(" ")
            p.drop(2).split(",").map { it.toInt() }.let { ReferenceLong(it[0], it[1]) } to
                    v.drop(2).split(",").map { it.toInt() }.let { ReferenceLong(it[0], it[1]) }
        }
        return robots
    }

    fun part1(input: List<String>): Long {
        val robots = readRobots(input)
        return navigate(robots,100, ReferenceLong(101, 103))
    }

    fun part1Example(input: List<String>): Long {
        val robots = readRobots(input)
        return navigate(robots,100, ReferenceLong(11, 7))
    }

    fun part2(input: List<String>): Long {
        val robots = readRobots(input)
        return findEgg(robots,ReferenceLong(101, 103))
    }

    val day = "day14"

    validateInput( "$day-part1" , 12 ) {
        part1Example(readInput("$day/example"))
    }

    runDay( "$day-part1" , 228421332 ) {
        part1(readInput("$day/input"))
    }

//  Part2 has no example equivalent
//  validateInput( "$day-part2" , 31 ) {
//      part2(readInput("$day/example"))
//  }
    runDay( "$day-part2", 7790) {
        part2(readInput("$day/input"))
    }
}
