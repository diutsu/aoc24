package com.diutsu.aoc.library.mutableMatrix

import com.diutsu.aoc.library.Reference
import com.diutsu.aoc.library.Walker
import com.diutsu.aoc.library.println


typealias MutableMatrix<T> = List<MutableList<T>>

class FlatMatrix<T>(input: MutableMatrix<T>) {
    private val data: MutableList<T> = input.flatten().toMutableList()
    private val width: Int = input.first().size
    private val height: Int = input.size

    constructor(data: List<T>, width: Int, height: Int) : this(
        List(height) { y -> data.subList(y * width, (y + 1) * width).toMutableList() }
    )

    operator fun get(ref: Reference): T {
        val index = ref.y * width + ref.x
        return data[index]
    }

    operator fun set(ref: Reference, value: T) {
        val index = ref.y * width + ref.x
        data[index] = value
    }

    operator fun contains(next: Reference): Boolean {
        return next.y * width + next.x in data.indices
    }

    fun copy(): FlatMatrix<T> {
        return FlatMatrix(data.toMutableList(), width, height)
    }
}

operator fun <T> MutableMatrix<T>.get(ref: Reference): T {
    return this[ref.y][ref.x]
}

operator fun <T> MutableMatrix<T>.set(ref: Reference, value: T)  {
    this[ref.y][ref.x] = value
}

operator fun <T> MutableMatrix<T>.set(ref: Walker, value: T)  {
    this[ref.y][ref.x] = value
}

operator fun <T> MutableMatrix<T>.get(ref: Walker): T {
    return this[ref.y][ref.x]
}

fun <T> MutableMatrix<T>.println(): Unit {
    this.forEach {
        it.joinToString("").println()
    }
}