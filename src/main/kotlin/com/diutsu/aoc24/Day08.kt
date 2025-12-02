package com.diutsu.aoc24

import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.listString.contains
import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun calculateAntinodes(
    antennas: Map<Char, List<Reference>>,
    input: List<String>,
    harmonics: Boolean = false,
): MutableSet<Reference> {
    val antinodes = mutableSetOf<Reference>()
    antennas.forEach { (letter, references) ->
        for (first in references) {
            for (second in references) {
                if (first == second) continue
                val distance = second - first
                // apparently not needed to minimize the distance
//                    val dx = (second.y - first.y)
//                    val dy = (second.x - first.x)
//                    val gcd = gcd(dx, dy)
//                    val minDist = Reference( (second.x - first.x)/ gcd, (second.y - first.y)/ gcd)
                if (harmonics) {
                    val directions = listOf(-1, 1)
                    directions.forEach { direction ->
                        var currentRef = first // itself is an harmonic, plus distance gets to [second]
                        while (currentRef in input) {
                            antinodes.add(currentRef)
                            currentRef += distance * direction
                        }
                    }
                } else {
                    (first - distance).takeIf { it in input }?.let { antinodes.add(it) }
                    (second + distance).takeIf { it in input }?.let { antinodes.add(it) }
                }
            }
        }
    }
    return antinodes
}

fun parseAntennas(input: List<String>): Map<Char, List<Reference>> {
    val antennasMap = mutableMapOf<Char, MutableList<Reference>>()

    input.forEachIndexed { lIndex, line ->
        line.forEachIndexed { cIndex, letter ->
            if (letter != '.') {
                antennasMap.computeIfAbsent(letter) { mutableListOf() }
                    .add(Reference(cIndex, lIndex))
            }
        }
    }

    return antennasMap.mapValues { it.value.toList() }
}

fun printMapWithAntinodes(
    input: List<String>,
    antinodes: MutableSet<Reference>,
) {
    input.forEachIndexed { lIndex, line ->
        line.forEachIndexed { cIndex, letter ->
            if (letter != '.') {
                print(letter)
            } else if (antinodes.contains(Reference(lIndex, cIndex))) {
                print("#")
            } else {
                print(letter)
            }
        }
        println()
    }
}

fun day08part1(input: List<String>): Int {
    return parseAntennas(input).let { antennas ->
        calculateAntinodes(antennas, input).count()
    }
}

fun day08part2(input: List<String>): Int {
    return parseAntennas(input).let { antennas ->
        calculateAntinodes(antennas, input, true).count()
    }
}

fun main() {
    val day = "day08"

    validateInput("$day-part1", 14) {
        day08part1(readInput("$day/example"))
    }
    runDay("$day-part1", 269) {
        day08part1(readInput("$day/input"))
    }
    validateInput("$day-part2", 34) {
        day08part2(readInput("$day/example"))
    }
    validateInput("$day-part2", 9) {
        day08part2(readInput("$day/example2"))
    }
    // 949 too low
    runDay("$day-part2", 949) {
        day08part2(readInput("$day/input"))
    }
}
