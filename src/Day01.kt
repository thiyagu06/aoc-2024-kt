import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val left = input.map { it.split("   ")[0] }.map { it.toInt() }.sorted()
        val right = input.map { it.split("   ")[1] }.map { it.toInt() }.sorted()
        val distance = left.zip(right).map { abs(it.second - it.first) }
        return distance.sum()
    }

    fun part2(input: List<String>): Int {
        val left = input.map { it.split("   ")[0] }.map { it.toInt() }
        val right = input.map { it.split("   ")[1] }.map { it.toInt() }
        val countOfRight = right.groupingBy { it }.eachCount()
        return left.sumOf { countOfRight.getOrDefault(it, 0) * it }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
