package com.diutsu.aoc.library

import java.io.InputStream
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = Path("inputs/aoc24/$name.txt").readText().trim().lines()

fun readInputLines(inputFile: String): List<String> = Files.readAllLines(Paths.get("inputs/aoc23/", inputFile)).toList()

fun readFileAsMatrix(inputFile: String): List<List<Char>> = readInputLines(inputFile).map { it.toList() }.toList()

class LineScanner(inputStream: InputStream) : Iterator<String> {
    private val scanner = Scanner(inputStream)

    override fun hasNext(): Boolean = scanner.hasNextLine()

    override fun next(): String = scanner.nextLine()
}


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun validateInput(description: String, expectecResult: Int, runnable: () -> Int) {
    try {
        val testResult = runnable()
        if(testResult != expectecResult) {
            println("❌ [$description] Test input from file: $testResult doesn't match $expectecResult")
        } else {
            println("✅ [$description] Test ok")
        }
    } catch (e : Exception) {
        println("💥 [$description] failed : ${e.message}}")
        e.printStackTrace()
    }
}

fun checkInput(description: String, expectecResult: Int, runnable: () -> Int) {
    val testResult = runnable()
    check (testResult == expectecResult) {
        println("❌ [$description] Test input from file: $testResult doesn't match $expectecResult")
    }
}

fun runDay(description: String, runnable: () -> Int) {
    stressTest( description, warmup = 0, iterations = 0)  { runnable().println() }
}
