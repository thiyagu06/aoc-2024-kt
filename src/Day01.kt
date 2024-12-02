import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
         val values = input.map { it.split("   ") }.map { Pair(it[0].toInt(), it[1].toInt()) }
        val left = values.map { it.first }.sorted()
        val right = values.map { it.second }.sorted()
        return left.zip(right).sumOf { abs(it.second - it.first) }
    }

    fun part2(input: List<String>): Int {
        val values = input.map { it.split("   ") }.map { Pair(it[0].toInt(), it[1].toInt()) }
        val left = values.map { it.first }
        val right = values.map { it.second }
        return left.sumOf { l -> right.count { it == l } * l }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
