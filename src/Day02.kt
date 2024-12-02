import kotlin.math.abs

fun main() {
    fun isValidLevel(levels: List<Int>): Boolean {
        val distinct = levels.distinct()
        val sortedAsc = distinct.sorted()
        val sortedDesc = distinct.sorted().reversed()
        val levelsDifferAtMostByThree = levels.windowed(2).all { abs(it.first() - it.last()) in 1..3 }

        return when {
            levels != distinct -> false
            sortedAsc == levels || sortedDesc == levels -> levelsDifferAtMostByThree
            else -> false
        }
    }

    fun part1(input: List<String>): Int {
        val values = input.map { it.split(" ").map { number -> number.toInt() } }.toList()
        return values.count { isValidLevel(it) }
    }

    fun part2(input: List<String>): Int {
        val values = input.map { it.split(" ").map { number -> number.toInt() } }.toList()
        return values.count { level ->
            if (isValidLevel(level)) true else {
                level.indices.any {
                    val subReport = level.toMutableList().apply { removeAt(it) }
                    isValidLevel(subReport)
                }
            }
        }
    }

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}