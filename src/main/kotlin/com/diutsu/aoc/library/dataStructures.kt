package com.diutsu.aoc.library

import com.diutsu.aoc23.Distance
import com.diutsu.aoc23.library.CardinalDirections.*
import java.util.EnumMap
import kotlin.math.abs

enum class CardinalDirections(val id: Int) {
    NORTH(0),
    EAST(1),
    SOUTH(2),
    WEST(3),
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

    fun inside(list: List<List<*>>): Boolean =
        0 <= this.y && this.y < list.size && 0 <= this.x && this.x < list.first().size

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

    operator fun plus(other: Reference3DLong): Reference3DLong =
        Reference3DLong(this.x + other.x, this.y + other.y, this.z + other.z)

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