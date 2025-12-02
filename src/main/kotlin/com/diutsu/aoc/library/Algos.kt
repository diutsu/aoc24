package com.diutsu.aoc.library

import java.util.ArrayDeque
import java.util.PriorityQueue

/**
 * Traverse [graph] from [start] in DFS
 *
 * @return visitedSet
 */
fun <T, U> graphTraverseDfs(
    start: T,
    graph: U,
    neighbours: (T, U, Set<T>) -> Collection<T>,
): Set<T> {
    val toVisit =
        ArrayDeque<T>()
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

fun <T, U> graphSSPWithBT(
    start: T,
    graph: U,
    neighbours: (T, U) -> Collection<Pair<T, Int>>,
    end: T,
    acc: (T, Int) -> Unit,
): Pair<Map<T, Int>, List<T>> {
    val toVisit =
        PriorityQueue(compareBy<Pair<T, Int>> { it.second })
            .apply { add(start to 0) }
    val minCosts = mutableMapOf<T, Int>()
    val previous = mutableMapOf<T, T>()

    while (toVisit.isNotEmpty()) {
        val (currentNode, currentCost) = toVisit.poll()
        if (currentCost > minCosts.getOrDefault(currentNode, Int.MAX_VALUE)) continue

        if (currentNode == end) {
            acc(currentNode, currentCost)
            continue
        }
        minCosts[currentNode] = currentCost
        neighbours(currentNode, graph).forEach { (neighbor, cost) ->
            val newCost = currentCost + cost
            if (newCost < minCosts.getOrDefault(neighbor, Int.MAX_VALUE)) {
                toVisit.add(neighbor to newCost)
                minCosts[neighbor] = newCost
                previous[neighbor] = currentNode
            }
        }
    }

    // Reconstruct the shortest path
    val path = mutableListOf<T>()
    var current: T? = end
    while (current != null) {
        path.add(0, current)
        current = previous[current]
    }
    return minCosts.toMap() to path.toList()
}

fun <T, U> graphSSP(
    start: T,
    graph: U,
    neighbours: (T, U) -> Collection<Pair<T, Int>>,
    isEnd: (T, U) -> Boolean,
    acc: (T, Int) -> Unit,
): Map<T, Int> {
    val toVisit =
        PriorityQueue(compareBy<Pair<T, Int>> { it.second })
            .apply { add(start to 0) }
    val minCosts = mutableMapOf<T, Int>()

    while (toVisit.isNotEmpty()) {
        val (currentNode, currentCost) = toVisit.poll()
        if (currentCost > minCosts.getOrDefault(currentNode, Int.MAX_VALUE)) continue

        if (isEnd(currentNode, graph)) {
            acc(currentNode, currentCost)
            continue
        }
        minCosts[currentNode] = currentCost
        neighbours(currentNode, graph).forEach { (neighbor, cost) ->
            val newCost = currentCost + cost
            if (newCost < minCosts.getOrDefault(neighbor, Int.MAX_VALUE)) {
                toVisit.add(neighbor to newCost)
                minCosts[neighbor] = newCost
            }
        }
    }
    return minCosts.toMap()
}

fun <T, U> graphTraverseGeneric(
    start: T,
    graph: U,
    neighbours: (T, U) -> Collection<T>,
    isEnd: (T, U) -> Boolean,
    acc: (T) -> Unit,
) {
    val toVisit =
        ArrayDeque<T>()
            .apply { add(start!!) }
    while (toVisit.isNotEmpty()) {
        val visiting = toVisit.removeLast()
        if (isEnd(visiting, graph)) {
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

fun gcd(
    a: Int,
    b: Int,
): Int {
    return if (b == 0) a else gcd(b, a % b)
}
