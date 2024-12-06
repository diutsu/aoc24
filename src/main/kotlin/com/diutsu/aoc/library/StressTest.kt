@file:OptIn(ExperimentalContracts::class)

package com.diutsu.aoc.library

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.system.measureNanoTime

var totalColdTime = 0L
var totalWarmTime = 0L

private val IDENT = "      "

fun stressTest(
    description: String,
    warmup: Int = 0,
    iterations: Int = 0,
    expected: Int? = null,
    ut: () -> Int,
) {
    if (warmup == 0 && iterations == 0) {
        runOnce(description, expected, ut)
        return
    } else {
        println(
            "😰 [$description] Stress Testing:\n${IDENT}Profile: 1 cold run, $warmup warmup run(s), measure across $iterations iterations",
        )
        println("\uD83D\uDCCA Report: ")
    }

    measureNanoTime {
        val result = ut()
        if (expected != null) {
            if (result == expected) {
                println(IDENT + "Solution:   $result ✅")
            } else {
                println(IDENT + "Solution:   $result ❌ expected $expected")
            }
        } else {
            println(IDENT + "Solution:   $result ❓")
        }
    }.also {
        totalColdTime += it
        println(String.format(IDENT + "Cold:   %,7d µs", it / 1000))
    }
    if (warmup > 0) {
        for (i in 0..warmup) {
            ut.invoke()
        }
    }
    if (iterations > 0) {
        runWithStatistics(iterations, ut)
    }
    println()
}

fun <T> timeIt(message: String = "", block: () -> T) : T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val start = System.nanoTime()
    val result = block()
    val time = System.nanoTime() - start
    println(String.format( "%s⏱️ %s %,7d  µs", IDENT,  message, time / 1000))
    return result
}

private fun runWithStatistics(
    iterations: Int,
    ut: () -> Int,
) {
    val times = mutableListOf<Long>()

    val totalTime =
        measureNanoTime {
            for (i in 0 until iterations) {
                val time =
                    measureNanoTime {
                        ut()
                    }
                times.add(time)
            }
        }

    val avgTime = totalTime / iterations
    val minTime = times.minOrNull() ?: 0L
    val maxTime = times.maxOrNull() ?: 0L
    val mean = times.average()
    val variance = times.map { (it - mean) * (it - mean) }.average()
    val stddev = Math.sqrt(variance)

    println(String.format(IDENT + "Min:    %,7d µs", minTime / 1000))
    println(String.format(IDENT + "Max:    %,7d µs", maxTime / 1000))
    println(String.format(IDENT + "Avg:    %,7d µs", avgTime / 1000))
    println(String.format(IDENT + "Stddev: %,7d µs", (stddev / 1000).toLong()))
    println(String.format(IDENT + "Total:  %,7d µs", totalTime / 1000))
    totalWarmTime += totalTime
}

private fun runOnce(
    description: String,
    expected: Int? = null,
    ut: () -> Int,
) {
    println("🏃‍➡️ [$description] Running result:")
    measureNanoTime {
        val result = ut()
        if (expected != null) {
            if (result == expected) {
                println(IDENT + "✅ Solution is ok: $result")
            } else {
                println(IDENT + "❌ $result doesn't match expected problem solution $expected")
            }
        } else {
            println(IDENT + "❓ Current solution is:")
            println(IDENT + "$result")
        }
    }.also {
        println(String.format("$IDENT⌛ Time: %,7d µs", it / 1000))
    }
}
