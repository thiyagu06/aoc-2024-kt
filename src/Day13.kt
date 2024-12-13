fun main() {
    val input = readInput("Day13")
    solve(input, 0).println()
    solve(input, 10000000000000L).println()
}

fun solve(input: List<String>, prizeValue: Long): Long {
    return input
        .chunked(4)
        .map { line -> line.map { equation -> equation.split(" ").map { it.split(", ") } } }
        .map { values ->
            listOf(
                values[0][2][0] to values[0][3][0],
                values[1][2][0] to values[1][3][0],
                values[2][1][0] to values[2][2][0],
            )
                .map { (x, y) -> x.filter { it.isDigit() } to y.filter { it.isDigit() } }
                .map { (x, y) -> x.toLong() to y.toLong() }
        }.sumOf { getCount(it[0], it[1], it[2].first + prizeValue to it[2].second + prizeValue) }
}

fun getCount(a: Pair<Long, Long>, b: Pair<Long, Long>, target: Pair<Long, Long>): Long {
    val a1 = a.first
    val a2 = a.second
    val b1 = b.first
    val b2 = b.second
    val c1 = target.first
    val c2 = target.second

    val y = (c1 * a2 - c2 * a1) / (a2 * b1 - a1 * b2)
    val x = (c1 - b1 * y) / a1

    val e1 = a1 * x + b1 * y
    val e2 = a2 * x + b2 * y
    if (e1 == c1 && e2 == c2) {
        return 3L * x + 1L * y
    }

    return 0L
}

