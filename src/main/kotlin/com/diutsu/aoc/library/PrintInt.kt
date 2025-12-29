package com.diutsu.aoc.library

import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import com.diutsu.aoc.library.mutableMatrix.get
import com.diutsu.aoc.library.mutableMatrix.set

fun printMap(
    spaceY: Long,
    spaceX: Long,
    inSpace: List<ReferenceLong>,
) {
    val map = (0..spaceY).map { (0..spaceX).map { 0 }.toMutableList() }
    inSpace.forEach { map[it.y.toInt()][it.x.toInt()]++ }
    map.forEachIndexed { index, line ->
        line.forEach {
            if (it == 0) {
                print(".")
            } else {
                print(it)
            }
        }
        println()
    }
}

// 2024 day 15
fun printStatus(
    warehouse: MutableMatrix<Char>,
    robotPos: Reference,
    newPos: Reference,
    it: CardinalDirection,
    target: Char,
) {
    val old = warehouse[robotPos]
    warehouse[robotPos] = '@'
    val moved = if (newPos != robotPos) "not" else ""
    println("Moved Robot $it to $newPos robot $moved has moved. Warehouse has $target")
    printMap(warehouse)
    println()
    require(old == '.') { "Robot is not empty" }
    warehouse[robotPos] = old
}

fun printMap(
    map: Matrix<Char>,
    filter: (Reference) -> Char = { map[it] },
) {
    map.forEachIndexed { y, line ->
        line.forEachIndexed { x, it ->
            print(filter(Reference(x, y)))
        }
        println()
    }
}

fun pad(value : Number, pad: Int) =
    value.toString().padStart(pad, ' ')
