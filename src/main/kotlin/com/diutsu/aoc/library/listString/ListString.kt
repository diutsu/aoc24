package com.diutsu.aoc.library.listString

import com.diutsu.aoc.library.Reference

operator fun List<String>.contains(ref: Reference): Boolean = ref.y in this.indices && ref.x in this.first().indices
