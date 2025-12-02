package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

enum class GateOperator {
    AND,
    OR,
    XOR,
    ;

    fun apply(
        input1: Boolean,
        input2: Boolean,
    ): Boolean {
        return when (this) {
            AND -> input1 && input2
            OR -> input1 || input2
            XOR -> input1 xor input2
        }
    }

    companion object {
        fun fromString(value: String): GateOperator {
            return when (value) {
                "AND" -> AND
                "OR" -> OR
                "XOR" -> XOR
                else -> throw IllegalArgumentException("Invalid gate: $value")
            }
        }
    }
}

data class Gate(val a: String, val b: String, val op: GateOperator, val out: String) {
    fun wired(wires: Map<String, Boolean>): Pair<String, Boolean> {
        return out to op.apply(wires[a]!!, wires[b]!!)
    }

    fun reversed(): Gate {
        return Gate(b, a, op, out)
    }

    fun isXorY(): Boolean {
        return a.startsWith("x") || a.startsWith("y") || b.startsWith("x") || b.startsWith("y")
    }

    fun isZout(): Boolean {
        return out.startsWith("z")
    }
}

fun main() {
    fun toLong(zWires: List<Boolean>): Long {
        var acc = 0L
        zWires.forEachIndexed { index, b -> acc = acc or (if (b) 1L shl index else 0L) }
        return acc
    }

    fun readInput(input: List<String>): Pair<MutableMap<String, Boolean>, MutableList<Gate>> {
        var readGates = false
        val wires = mutableMapOf<String, Boolean>()
        val gates = mutableListOf<Gate>()
        input.forEach {
            if (it.isEmpty()) {
                readGates = true
            } else if (!readGates) {
                val (wire, value) = it.split(": ")
                wires[wire] = value == "1"
            } else {
                val (gate, output) = it.split(" -> ")
                val (a, op, b) = gate.split(" ")
                gates.add(Gate(a, b, GateOperator.fromString(op), output))
            }
        }
        return Pair(wires, gates)
    }

    fun part1(input: List<String>): Long {
        val (wires, gates) = readInput(input)
        while (gates.isNotEmpty()) {
            val gate = gates.first { wires[it.a] != null && wires[it.b] != null }
            gates.remove(gate)
            val (outWire, outVal) = gate.wired(wires)
//            println("Processing $gate -> $outWire = $outVal")
            wires[outWire] = outVal
        }

        val zWires =
            wires.entries.filter { (k, _) -> k.startsWith("z") }
                .sortedBy { (k, _) -> k }

        return toLong(zWires.map { it.value })
    }

    fun part2(input: List<String>): Int {
        val (_, gates) = readInput(input)
        val suspiciousOutputs = mutableSetOf<String>()
        gates.forEach { gate ->
            when {
                gate.isZout() && gate.out != "z45" && gate.op != GateOperator.XOR -> {
                    suspiciousOutputs.add(gate.out)
                }
                !gate.isZout() && !gate.isXorY() && gate.op == GateOperator.XOR -> {
                    suspiciousOutputs.add(gate.out)
                }
                gate.isXorY() && gate.op == GateOperator.XOR &&
                    gates.any { (it.a == gate.out || it.b == gate.out) && it.op == GateOperator.OR } -> {
                    suspiciousOutputs.add(gate.out)
                }
                gate.isXorY() && gate.op == GateOperator.AND && gate.a != "x00" && gate.b != "x00" &&
                    gates.any { (it.a == gate.out || it.b == gate.out) && it.op == GateOperator.XOR } -> {
                    suspiciousOutputs.add(gate.out)
                }
            }
        }

        val errors = suspiciousOutputs.sorted()
        println(errors.joinToString(","))
        return errors.size
    }

    fun part2Exploratory(input: List<String>): Int {
        val correction = mutableMapOf<String, String>()
        val (_, gates) =
            readInput(input).let { (w, gates) ->
                w to
                    gates.map {
                        if (correction[it.out] != null) {
                            Gate(correction[it.out]!!, it.b, it.op, it.out)
                        } else {
                            it
                        }
                    }
            }
        val rename = mutableMapOf<String, String>()
        gates.filter {
            (it.a.startsWith("x") || it.a.startsWith("y")) && !it.out.startsWith("z")
        }.forEach {
            val isHalfCarry = it.op == GateOperator.AND
            val isSum = it.op == GateOperator.XOR
            if (isSum) {
                rename[it.out] = "P" + it.a.drop(1)
            } else if (isHalfCarry) {
                if (it.a.drop(1) == "00") {
                    rename[it.out] = "C01"
                } else {
                    rename[it.out] = "H" + it.a.drop(1)
                }
            } else {
                println("Unexpected Gate $it")
            }
        }

        gates.filter {
            !it.isXorY() && !it.out.startsWith("z")
        }.forEach {
            val ar = rename[it.a] ?: it.a
            val br = rename[it.b] ?: it.b
            val gateN =
                (
                    ar.takeIf { it.first().isUpperCase() }
                        ?: br.takeIf { it.first().isUpperCase() }
                )?.drop(1)
                    ?: throw IllegalArgumentException("Unexpected gate $it")

            if (ar.startsWith("P") || br.startsWith("P")) {
                val isNextCarry = it.op == GateOperator.AND
                val isSum = it.op == GateOperator.XOR
                if (isSum) {
                    println("Sum is always unexpected (it should be z filtered) $it")
                    rename[it.out] = "S$gateN"
                } else if (isNextCarry) {
                    rename[it.out] = "N$gateN"
                }
            }
            if (ar.startsWith("H") || br.startsWith("H")) {
                val isNextCarry = it.op == GateOperator.OR
                if (isNextCarry) {
                    rename[it.out] = "C" + (gateN.toInt() + 1).toString().padStart(2, '0')
                }
            }
        }

        gates.filter { it.out.startsWith("z") && it.out != "Z00" }
            .forEach {
                val ar = rename[it.a] ?: it.a
                val br = rename[it.b] ?: it.b
                val or = rename[it.out] ?: it.out
                if (it.op != GateOperator.XOR) {
                    println("Unexpected Gate ${it.a} ($ar) ${it.op} ${it.b}($br) -> $or (${it.out})")
                    if (ar.startsWith("y")) {
                        val expected = rename.filter { (_, v) -> v == "H31" }
                        println("expected to be $expected")
                    }

                    if (ar.startsWith("P") && it.op == GateOperator.AND) {
                        val expected = rename.filter { (_, v) -> v == "N18" }
                        println("expected to be $expected")
                    }

                    if (ar == "H44") {
                        val expected = rename.filter { (_, v) -> v == "C45" }
                        println("expected to be $expected")
                    }
                    if (ar == "H27") {
                        val expected = rename.filter { (_, v) -> v == "C28" }
                        println("expected to be $expected")
                    }
                }
            }

        gates.forEach {
            val ar = rename[it.a] ?: it.a
            val br = rename[it.b] ?: it.b
            val or = rename[it.out] ?: it.out
            println(" Gate $ar (${it.a}) ${it.op} $br (${it.b}) -> $or ${it.out}")
        }

        val vername = rename.entries.associate { (k, v) -> v to k }
        (1 until 44).forEach { gate ->
            val gn = gate.toString().padStart(2, '0')
            val gn1 = (gate + 1).toString().padStart(2, '0')
            listOf(
                Gate("x$gn", "y$gn", GateOperator.XOR, vername["P$gn"] ?: "PX$gn"),
                Gate("x$gn", "y$gn", GateOperator.AND, vername["H$gn"] ?: "HX$gn"),
                Gate(vername["P$gn"] ?: "PX$gn", vername["C$gn"] ?: "CX$gn", GateOperator.XOR, "z$gn"),
                Gate(vername["P$gn"] ?: "PX$gn", vername["C$gn"] ?: "CX$gn", GateOperator.AND, vername["N$gn"] ?: "NX$gn"),
                Gate(vername["H$gn"] ?: "HX$gn", vername["N$gn"] ?: "NX$gn", GateOperator.OR, vername["C$gn1"] ?: "CX$gn}"),
            ).forEach { test ->
                if (test !in gates && test.reversed() !in gates) {
                    val instead =
                        gates.filter {
                            it.op == test.op && ((it.a == test.a && it.b == test.b) || (it.a == test.b && it.b == test.a))
                        }
                    if (instead.isNotEmpty()) {
                        println("Could not find expected gate $test (${rename[test.a]}, ${rename[test.b]}} -> ${rename[test.out]}")
                        println("Instead found $instead")
                    }
                }
            }
        }

        val errors = correction.keys.sorted()
        println(errors.all { it in vername.keys })
        println(errors.joinToString(","))
        return 0
    }
    val day = "day24"

    validateInput("$day-part1", 2024) {
        part1(readInput("$day/example"))
    }
    runDay("$day-part1", 51745744348272) {
        part1(readInput("$day/input"))
    }
    runDay("$day-part2", 8) {
        part2(readInput("$day/input"))
    }
}
