package com.diutsu.aoc.library

import java.util.ArrayDeque

/**
 * Traverse [graph] from [start] in DFS
 *
 * @return visitedSet
 */
fun <T,U> graphTraverseDfs(
    start: T,
    graph: U,
    neighbours: (T, U, Set<T>) -> Collection<T>,
): Set<T> {
    val toVisit = ArrayDeque<T>()
        .apply { add(start!!) }
    val visited = mutableSetOf<T>()

    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.removeLast()
        if (visiting !in visited) {
            visited += visiting
            toVisit.addAll(neighbours(visiting, graph, visited))
        }
    }
    return visited
}

fun <T,U> graphTraverseGeneric(
    start: T,
    graph: U,
    neighbours: (T, U) -> Collection<T>,
    isEnd: (T, U) -> Boolean,
    acc: (T) -> Unit
) {
    val toVisit = ArrayDeque<T>()
        .apply { add(start!!) }
    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.removeLast()
        if (isEnd(visiting, graph) ) {
            acc(visiting)
            continue
        }
        toVisit.addAll(neighbours(visiting, graph))
    }
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
