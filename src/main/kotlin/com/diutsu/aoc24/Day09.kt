package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {
    fun part1(input: List<String>): Long {
        val fragmented =
            input.first().mapIndexedNotNull { index, char ->
                Triple(char.digitToInt(), index / 2, index % 2 == 0)
            }.toMutableList()

        var checksum = 0L
        var diskIndex = 0
        while (fragmented.isNotEmpty()) {
            val (blockLength, blockId, blockIsFile) = fragmented.removeFirst()
            if (blockIsFile) {
                checksum += blockId * (diskIndex * blockLength + blockLength * (blockLength - 1) / 2L)
                diskIndex += blockLength
            } else {
                var space = blockLength
                while (space > 0) {
                    val (fill, fillId, _) = fragmented.removeLast()
                    val consume =
                        if (space > fill) {
                            fragmented.removeLast()
                            fill
                        } else {
                            fragmented.addLast(Triple(fill - space, fillId, true))
                            space
                        }
                    checksum += fillId * (diskIndex * consume + consume * (consume - 1) / 2L)
                    diskIndex += consume
                    space -= consume
                }
            }
        }
        return checksum
    }

    val cache = IntArray(10) { Int.MAX_VALUE } // Cache indexed by block size

    fun findLastFileOfSize(
        fragmented: MutableList<Triple<Int, Int, Boolean>>,
        blockLength: Int,
    ): Int {
        val search = fragmented.size - 1
        for (i in search downTo 0) {
            val (spaceSize, _, isFile) = fragmented[i]
            if (isFile && spaceSize <= blockLength) {
                return i
            }
        }
        return -1
    }

    fun part2(input: List<String>): Long {
        val fragmented =
            input.first().mapIndexedNotNull { index, char ->
                Triple(char.digitToInt(), index / 2, index % 2 == 0) // size, id, isFile
            }.toMutableList()

        var checksum = 0L
        var diskIndex = 0
        while (fragmented.isNotEmpty()) {
            val (blockLength, blockId, blockIsFile) = fragmented.removeFirst()
            if (blockIsFile) {
                checksum += blockId * (diskIndex * blockLength + blockLength * (blockLength - 1) / 2L)
                diskIndex += blockLength
            } else {
                var remaining = blockLength
                while (remaining > 0) {
                    val toRemap = findLastFileOfSize(fragmented, remaining)
                    if (toRemap < 0) {
                        diskIndex += remaining
                        remaining = -1
                    } else {
                        val (fill, fillId, _) = fragmented[toRemap]
                        fragmented[toRemap] = Triple(fill, fillId, false)
                        checksum += fillId * (diskIndex * fill + fill * (fill - 1) / 2L)
                        diskIndex += fill
                        remaining -= fill
                    }
                }
            }
        }
        return checksum
    }

    val day = "day09"

    validateInput("$day-part1", 1928L) {
        part1(readInput("$day/example"))
    }
    runDay("$day-part1", 6241633730082L) {
        part1(readInput("$day/input"))
    }
    validateInput("$day-part2", 2858L) {
        part2(readInput("$day/example"))
    }
    runDay("$day-part2", 6265268809555L) {
        part2(readInput("$day/input"))
    }
}
