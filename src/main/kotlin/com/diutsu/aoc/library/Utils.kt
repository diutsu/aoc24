package com.diutsu.aoc.library

import com.diutsu.aoc.library.mutableMatrix.MutableMatrix
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Scanner
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> {
    val file = Path("inputs/aoc24/$name.txt")
    if (file.exists()) {
        return file.readText().trim().lines()
    } else {
        println("📄 File $file doesn't exist, return empty list.")
        return emptyList()
    }
}

fun readFileAsMatrix(inputFile: String): Matrix<Char> = readInput(inputFile).map { it.toList() }.toList()

fun readFileAsMutableMatrix(inputFile: String): MutableMatrix<Char> = readInput(inputFile).map { it.toMutableList() }.toList()

class LineScanner(inputStream: InputStream) : Iterator<String> {
    private val scanner = Scanner(inputStream)

    override fun hasNext(): Boolean = scanner.hasNextLine()

    override fun next(): String = scanner.nextLine()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun validateInput(
    description: String,
    expectedResult: Number,
    runnable: () -> Number,
) {
    try {
        val testResult = runnable()
        if (testResult != expectedResult) {
            println("❌ [$description] Test input from file: $testResult doesn't match $expectedResult")
        } else {
            println("✅ [$description] Test ok")
        }
    } catch (e: Exception) {
        println("💥 [$description] failed : ${e.message}}")
        e.printStackTrace()
    }
}

fun checkInput(
    description: String,
    expectecResult: Int,
    runnable: () -> Int,
) {
    val testResult = runnable()
    check(testResult == expectecResult) {
        println("❌ [$description] Test input from file: $testResult doesn't match $expectecResult")
    }
}

fun runDay(
    description: String,
    expected: Number? = null,
    runnable: () -> Number,
) {
    stressTest(description, warmup = 0, iterations = 0, expected) { runnable() }
}
