package com.diutsu.aoc24

import com.diutsu.aoc.library.readInput
import com.diutsu.aoc.library.runDay
import com.diutsu.aoc.library.validateInput

fun main() {

    fun part1(input: List<String>): Long {
        val fragmented = input.first().split("").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableList()

        var checksum = 0L
        var diskIndex = 0
        var fileID = 0
        var lastFileID = fragmented.size / 2
        var isFile = true
        var fragmentingFileSize = 0
        var fragmentingFileId = 0
        while(fragmented.isNotEmpty()) {
            val blockLength = fragmented.removeFirst()
            if (isFile) {
                checksum += fileID * (diskIndex * blockLength + blockLength * (blockLength - 1) / 2L)
                diskIndex += blockLength
                fileID++
            } else {
                var spaceLength = blockLength
                while (spaceLength > 0) {
                    if (fragmentingFileSize == 0) {
                        fragmentingFileSize = fragmented.removeLast()
                        fragmented.removeLast() // and a free space
                        fragmentingFileId = lastFileID
                        lastFileID--
                    }
                    val fill = if (spaceLength > fragmentingFileSize) fragmentingFileSize else spaceLength
                    fragmentingFileSize -= fill
                    checksum += fragmentingFileId * (diskIndex * fill + fill * (fill - 1) / 2L)
                    diskIndex += fill
                    spaceLength -= fill
                }
            }
            isFile = !isFile
        }
        // Process any remaining fragmented file
        if (fragmentingFileSize > 0) {
            checksum += fragmentingFileId * (diskIndex * fragmentingFileSize + fragmentingFileSize * (fragmentingFileSize - 1) / 2L)
        }
        return checksum
    }

    fun part2(input: List<String>): Long {
        val fragmented = input.first().split("").filter { it.isNotEmpty() }.mapIndexed { index, count ->
            Triple(count.toInt(), index / 2, index % 2 == 0)
        }.toMutableList()
        var checksum = 0L
        var diskIndex = 0
        while(fragmented.isNotEmpty()) {
            val (blockLength, blockId, blockIsFile) = fragmented.removeFirst()
            if (blockIsFile) {
                checksum += blockId * (diskIndex * blockLength + blockLength * (blockLength - 1) / 2L)
                diskIndex += blockLength
            } else {
                val toRemap = fragmented.indexOfLast { it.first <= blockLength && it.third }
                if (toRemap >= 0) {
                    val (fill, fillId, _) = fragmented[toRemap]
                    fragmented[toRemap] = Triple(fill, fillId, false)
                    checksum += fillId * (diskIndex * fill + fill * (fill - 1) / 2L)
                    diskIndex += fill
                    val remaining = blockLength - fill
                    if (remaining > 0) fragmented.addFirst(Triple(remaining, 0, false))
                } else {
                    diskIndex += blockLength
                }
            }
        }
        return checksum
    }

    val day = "day09"

    validateInput( "$day-part1" , 1928L ) {
        part1(readInput("$day/example"))
    }
    runDay( "$day-part1" , 6241633730082L ) {
        part1(readInput("$day/input"))
    }
    validateInput( "$day-part2" , 2858L ) {
        part2(readInput("$day/example"))
    }
    runDay( "$day-part2", 6265268809555L ) {
        part2(readInput("$day/input"))
    }
}
