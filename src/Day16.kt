
import java.util.*

fun main() {
    val input = readInput("Day16")
    part1(input)
    part2(input)
}

private fun part1(input: List<String>) {
    val map = walk(input, trackRoute = false) { false }
    val end = Index(input[0].lastIndex - 1, 1)
    val result = Directions.Cardinal.firstNotNullOf { map[Pos(end, it)] }
    println(result)
}

private fun part2(input: List<String>) {
    var bestCost = Int.MAX_VALUE
    val bestSeats = hashSetOf<Index>()
    walk(input, trackRoute = true) { (pos, cost, previous) ->
        bestCost = minOf(bestCost, cost)
        bestSeats.add(pos.index)
        if (cost <= bestCost) bestSeats.addAll(previous)
        return@walk true
    }
    println(bestSeats.size)
}

private fun walk(input: List<String>, trackRoute: Boolean, onEnd: (Path) -> Boolean): Map<Pos, Int> {
    val start = Index(1, input.lastIndex - 1)
    val end = Index(input[0].lastIndex - 1, 1)

    val pq = PriorityQueue<Path> { a, b -> a.cost - b.cost }
    pq.offer(Path(Pos(start, Directions.E), 0))
    val map = hashMapOf<Pos, Int>()
    while (pq.isNotEmpty()) {
        val path = pq.poll()
        val (pos, cost, previous) = path
        map[pos] = cost
        if (pos.index == end) if (onEnd(path)) continue else break
        for (d in Directions.Cardinal) {
            val next = Pos(pos.index + d, d)
            if (next in map || input[next.index.y][next.index.x] == '#') continue
            val potential = if (d == pos.dir) 1 else 1001
            val routeSoFar = if (trackRoute) previous + pos.index else emptyList()
            pq.offer(Path(next, cost + potential, routeSoFar))
        }
    }
    return map
}

private data class Pos(val index: Index, val dir: Index)
private data class Path(val pos: Pos, val cost: Int, val previous: List<Index> = emptyList())

data class Index(val x: Int, val y: Int)
private operator fun Index.plus(other: Index) = Index(x + other.x, y + other.y)
private operator fun Index.minus(other: Index) = Index(x - other.x, y - other.y)
private operator fun Index.times(multiplier: Int) = Index(x * multiplier, y * multiplier)

private data class Rect(val x: IntRange, val y: IntRange)
private operator fun Rect.contains(other: Index) = other.y in y && other.x in x

object Directions {
    @JvmField val N = Index(0, -1)
    @JvmField val S = Index(0, 1)
    @JvmField val E = Index(1, 0)
    @JvmField val W = Index(-1, 0)
    @JvmField val NE = Index(1, -1)
    @JvmField val NW = Index(-1, -1)
    @JvmField val SE = Index(1, 1)
    @JvmField val SW = Index(-1, 1)

    @JvmField
    val Cardinal = listOf(N, E, S, W)
    @JvmField
    val InterCardinal = listOf(NE, SE, SW, NW)
    @JvmField val Compass = Cardinal + InterCardinal
}