package com.diutsu.aoc.library

import com.diutsu.aoc.library.CardinalDirections.EAST
import com.diutsu.aoc.library.CardinalDirections.NORTH
import com.diutsu.aoc.library.CardinalDirections.SOUTH
import com.diutsu.aoc.library.CardinalDirections.WEST
import java.util.EnumMap
import kotlin.math.abs

typealias Distance = Long

typealias Matrix<T> = List<List<T>>

fun <T> Matrix<T>.inMatrix(
    line: Int,
    column: Int,
): Boolean {
    return line in this.indices && column in this.first().indices
}

fun <T> Matrix<T>.inMatrix(reference: Reference): Boolean {
    return reference.y in this.indices && reference.x in this.first().indices
}

operator fun <T> Matrix<T>.get(ref: Reference): T {
    return this[ref.y][ref.x]
}

fun <T> Matrix<T>.println() {
    this.forEach {
        it.println()
    }
}

//
// operator fun <T> Matrix<T>.get(ref: Reference, value: T) {
//    this[ref.y][ref.x] = T
// }

val directions4plus =
    listOf(
        Reference(-1, 0),
        Reference(0, 1),
        Reference(1, 0),
        Reference(0, -1),
    )

val directions4x =
    listOf(
        Reference(-1, 1),
        Reference(1, 1),
        Reference(1, -1),
        Reference(-1, -1),
    )

val directions8 =
    listOf(
        Reference(-1, 0),
        Reference(-1, 1),
        Reference(0, 1),
        Reference(1, 1),
        Reference(1, 0),
        Reference(1, -1),
        Reference(0, -1),
        Reference(-1, -1),
    )

enum class CardinalDirections(val id: Int, val letter: Char, val dx: Int, val dy: Int) {
    NORTH(0, 'U', 0, -1),
    EAST(1, 'R', 1, 0),
    SOUTH(2, 'D', 0, 1),
    WEST(3, 'L', -1, 0),
    ;

    fun opposite(): CardinalDirections =
        when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }

    fun toShortString(): String {
        return when (this) {
            NORTH -> ""
            EAST -> "E"
            SOUTH -> "S"
            WEST -> "W"
        }
    }

    fun rotate90(): CardinalDirections =
        when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }

    companion object {
        fun fromLetter(letter: String): CardinalDirections {
            return when (letter) {
                "U" -> NORTH
                "R" -> EAST
                "D" -> SOUTH
                "L" -> WEST
                else -> throw RuntimeException("Invalid direction $letter")
            }
        }
        fun fromLetter(letter: Char): CardinalDirections {
            return when (letter) {
                'U' -> NORTH
                'R' -> EAST
                'D' -> SOUTH
                'L' -> WEST
                else -> throw RuntimeException("Invalid direction $letter")
            }
        }
    }
}

class Resetable<T>(val initial: T) {
    var value: T = initial

    fun reset() {
        value = initial
    }
}

class Walker(var x: Int, var y: Int) {
    operator fun plus(direction: CardinalDirections): Walker {
        when (direction) {
            NORTH -> y -= 1
            EAST -> x += 1
            SOUTH -> y += 1
            WEST -> x -= 1
        }
        return this
    }
}

data class Reference(val x: Int, val y: Int) {
    operator fun plus(direction: CardinalDirections): Reference =
        when (direction) {
            NORTH -> Reference(x, y - 1)
            EAST -> Reference(x + 1, y)
            SOUTH -> Reference(x, y + 1)
            WEST -> Reference(x - 1, y)
        }

    operator fun plus(other: Reference): Reference = Reference(this.x + other.x, this.y + other.y)

    operator fun times(value: Int): Reference = Reference(this.x * value, this.y * value)

    fun plusAll() =
        run {
            val ref = this
            EnumMap<CardinalDirections, Reference>(CardinalDirections::class.java).apply {
                CardinalDirections.entries.forEach {
                    put(it, ref + it)
                }
            }
        }

    override fun toString(): String = "[$x][$y]"

    fun cartesianDistance(reference: Reference): Distance {
        return abs(x - reference.x) + abs(y - reference.y).toLong()
    }

    fun inside(list: List<List<*>>): Boolean {
        return this.y in list.indices && this.x in list.first().indices
    }

    // This might limits the maximum X/Y value, but dramatically improves performance in some scenarions
    override fun hashCode(): Int = x * 512 + y

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reference

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}

data class Reference3DDouble(val x: Double, val y: Double, val z: Double)

data class ReferenceDouble(val x: Double, val y: Double) {
    fun inside(
        from: ReferenceLong,
        to: ReferenceLong,
    ): Boolean = from.y <= this.y && this.y <= to.y && from.x <= this.x && this.x <= to.x
}

data class ReferenceLong(val x: Long, val y: Long) {
    fun plus(
        direction: CardinalDirections,
        walk: Long,
    ): ReferenceLong =
        when (direction) {
            NORTH -> ReferenceLong(x, y - walk)
            EAST -> ReferenceLong(x + walk, y)
            SOUTH -> ReferenceLong(x, y + walk)
            WEST -> ReferenceLong(x - walk, y)
        }

    override fun toString(): String = "[$x][$y]"

    operator fun plus(other: ReferenceLong): ReferenceLong = ReferenceLong(this.x + other.x, this.y + other.y)

    override fun hashCode(): Int = (x * 512 + y).toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReferenceLong

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}

data class Movement(val direction: CardinalDirections, val reference: Reference) {
    override fun toString(): String {
        return "$direction - $reference"
    }

    fun move(direction: CardinalDirections) = Movement(direction, reference + direction)
}

data class Reference3D(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String = "[$x][$y][$z]"
}

data class Reference3DLong(val x: Long, val y: Long, val z: Long) {
    override fun toString(): String = "[$x][$y][$z]"

    operator fun plus(other: Reference3DLong): Reference3DLong = Reference3DLong(this.x + other.x, this.y + other.y, this.z + other.z)

    operator fun minus(v: Reference3DLong) = Reference3DLong(x - v.x, y - v.y, z - v.z)

    operator fun times(scalar: Long) = Reference3DLong(x * scalar, y * scalar, z * scalar)

    fun lengthSquared(): Long = x * x + y * y + z * z

    fun length(): Double = Math.sqrt(lengthSquared().toDouble())

    override fun hashCode(): Int = (x * 512 + y).toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reference3DLong

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }
}

object IdGenerator {
    private var value: Int = 0

    fun next(): Int = value++
}

data class Volume3D(val a: Reference3D, val b: Reference3D) {
    val id = IdGenerator.next()

    init {
        require(a.x <= b.x)
        require(a.y <= b.y)
        require(a.z <= b.z)
    }

    fun lowesetZ(): Int = a.z

    fun fallTo(z: Int): Volume3D = Volume3D(Reference3D(a.x, a.y, z), Reference3D(b.x, b.y, b.z - a.z + z))

    override fun toString(): String {
        return "{$id}"
    }
}
