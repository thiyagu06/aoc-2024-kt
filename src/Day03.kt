fun main() {
    val regex1 = """mul\((\d+),(\d+)\)""".toRegex()
    val regex2 = """(do\(\))|(don't\(\))|mul\((\d+),(\d+)\)""".toRegex()

    fun part1(input: String): Int {
        return regex1.findAll(input).sumOf { match ->
            val (x, y) = match.destructured
            x.toInt() * y.toInt()
        }
    }

    fun part2(input: String): Int {
        var enable = true
        return regex2.findAll(input)
            .filter { match ->
                val (yes, no) = match.destructured
                enable = when {
                    yes.isNotEmpty() -> true
                    no.isNotEmpty() -> false
                    else -> return@filter enable
                }
                false
            }
            .sumOf { match ->
                val (_, _, x, y) = match.destructured
                x.toInt() * y.toInt()
            }
    }

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day03")
    part1(input.joinToString("")).println()
    part2(input.joinToString ( "" )).println()
}