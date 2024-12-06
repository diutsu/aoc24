package com.diutsu.aoc.library.mutableMatrix

import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.println


typealias MutableMatrix<T> = List<MutableList<T>>

operator fun <T> MutableMatrix<T>.get(ref: Reference): T {
    return this[ref.y][ref.x]
}

operator fun <T> MutableMatrix<T>.set(ref: Reference, value: T)  {
    this[ref.y][ref.x] = value
}

fun <T> MutableMatrix<T>.println(): Unit {
    this.forEach {
        it.joinToString("").println()
    }
}