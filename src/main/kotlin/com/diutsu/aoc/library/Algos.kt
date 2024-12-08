package com.diutsu.aoc.library

import java.util.ArrayDeque

/**
 * Traverse [graph] from [start] in DFS
 *
 * @return visitedSet
 */
fun <T> graphTraverseDfs(
    start: T,
    graph: MutableMap<T, MutableSet<T>>,
): MutableSet<T> {
    val toVisit = ArrayDeque<T>().apply { add(start) }
    val visited = mutableSetOf(start)

    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.pop()
        if (visiting !in visited) {
            visited += visiting
            toVisit.addAll(graph[visiting]!! - visited)
        }
    }
    return visited
}

/**
 * Traverse [graph] from [start] in BFS
 *
 * @return visitedSet
 */
fun <T> graphTraverseBfs(
    start: T,
    graph: MutableMap<T, MutableSet<T>>,
): MutableSet<T> {
    val toVisit = ArrayDeque<T>().apply { add(start) }
    val visited = mutableSetOf<T>()

    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.removeFirst()
        if (visiting !in visited) {
            visited += visiting
            toVisit.addAll(graph[visiting]!! - visited)
        }
    }
    return visited
}



fun gcd( a:Int, b:Int): Int {
    return if(b == 0) a else gcd(b, a % b)
}
