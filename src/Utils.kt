import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

fun readInputAsString(name: String) = Path("src/$name.txt").readText()

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

inline fun <T> measureAndPrintResult(crossinline block: () -> T) {
    measureTime {
        println(block())
    }.also { println("Took $it") }
}

fun String.ints(delimiter: String = " "): List<Int> = split(delimiter).map(String::toInt)

fun <T> List<T>.pair() = this[0] to this[1]

inline fun <T> Collection<T>.partitionBy(predicate: (T) -> Boolean): List<List<T>> {
    return fold(mutableListOf(mutableListOf<T>())) { acc, it ->
        when {
            predicate(it) -> acc.add(mutableListOf())
            else -> acc.last() += it
        }
        acc
    }
}

fun List<String>.toGrid(): Map<Point, Char> = flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
        Point(x, y) to c
    }
}.toMap()

fun <T> Iterable<T>.appendToString(transform: ((T) -> Char)): String {
    val buffer = StringBuilder()
    for (c in this) {
        buffer.append(transform(c))
    }
    return buffer.toString()
}

data class Point(val x: Int, val y: Int) {
    val cardinal
        get() = listOf(
            this + Direction.Right,
            this + Direction.Down,
            this + Direction.Left,
            this + Direction.Up,
        )

    val diagonals
        get() = listOf(
            this + Direction.DownRight,
            this + Direction.DownLeft,
            this + Direction.UpLeft,
            this + Direction.UpRight,
        )

    fun turnLeft() = copy(y, -x)
    fun turnRight() = copy(-y, x)
    fun withinBounds(end: Point, start: Point = Zero) = x >= start.x && y >= start.y && x < end.x && y < end.y

    operator fun plus(other: Point) = copy(x = x + other.x, y = y + other.y)
    operator fun times(factor: Int) = copy(x = x * factor, y = y * factor)
     operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun plus(direction: Direction) = when (direction) {
        Direction.Right -> copy(x = x + 1)
        Direction.Left -> copy(x = x - 1)
        Direction.Down -> copy(y = y + 1)
        Direction.Up -> copy(y = 1 - 1)
        Direction.DownRight -> copy(x = x + 1, y = y + 1)
        Direction.DownLeft -> copy(x = x - 1, y = y + 1)
        Direction.UpLeft -> copy(x = x - 1, y = y - 1)
        Direction.UpRight -> copy(x = x + 1, y = y - 1)
    }

     companion object {
        val Zero = Point(0, 0)
    }
}

enum class Direction {
    Right, Left, Down, Up, DownRight, DownLeft, UpLeft, UpRight;

    companion object {
        val cardinals = listOf(
            Right, Left, Down, Up
        ).map { it.asPoint() }

        val diagonals = listOf(
            DownRight, DownLeft, UpLeft, UpRight
        ).map { it.asPoint() }
    }
}

fun Direction.asPoint() = when (this) {
    Direction.Right -> Point(1, 0)
    Direction.Left -> Point(-1, 0)
    Direction.Down -> Point(0, 1)
    Direction.Up -> Point(0, -1)
    Direction.DownRight -> Point(1, 1)
    Direction.DownLeft -> Point(-1, 1)
    Direction.UpLeft -> Point(-1, -1)
    Direction.UpRight -> Point(1, -1)
}

fun <T> bfs(start: T, neighbors: (T) -> List<T>) = sequence {
    val queue = ArrayDeque(listOf(start.withIndex(index = 0)))
    val seen = mutableSetOf(start)
    while (queue.isNotEmpty()) {
        val (index, current) = queue.removeFirst().also { yield(it) }
        neighbors(current).forEach { neighbor ->
            if (seen.add(neighbor)) {
                queue.add(neighbor.withIndex(index = index + 1))
            }
        }
    }
}

fun <T> T.withIndex(index: Int) = IndexedValue(index, value = this)

infix fun Point.inBoundsOf(lines: List<String>): Boolean {
    return y in lines.indices && x in lines[0].indices
}

fun <T> generateCombinations(values: List<T>, times: Int): List<List<T>> =
    generateCombinations((0 until times).map { values })

fun <T> generateCombinations(lists: List<List<T>>): List<List<T>> =
    lists.fold(listOf(listOf<T>())) { acc, set -> acc.flatMap { list -> set.map { element -> list + element } } }