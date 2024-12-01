package com.diutsu.aoc.library

import kotlin.system.measureNanoTime

var totalColdTime = 0L
var totalWarmTime = 0L

private val IDENT = "    "
fun stressTest(
        description: String,
        warmup: Int = 0,
        iterations: Int = 0,
        ut: () -> Unit
) {
    if(warmup == 0 && iterations == 0) {
        runOnce(description, ut)
        return
    } else {
        println("😰 [$description] Stress Testing profile: 1 cold run, $warmup warmup run(s), measure across $iterations iterations")
        println("\uD83D\uDCCA Report: ")
    }

    measureNanoTime {
        ut()
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

private fun runWithStatistics(iterations: Int, ut: () -> Unit) {
    val times = mutableListOf<Long>()

    val totalTime = measureNanoTime {
        for (i in 0 until iterations) {
            val time = measureNanoTime {
                ut.invoke()
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

private fun runOnce(description: String, ut: () -> Unit) {
    println("🏃‍➡️ [$description] Running result:")
    println("---------------------------------")
    measureNanoTime {
        ut.invoke()
    }.also {
        println("---------------------------------")
        println(String.format("$IDENT⌛ Time: %,7d µs", it / 1000))
    }
}
