
fun main() {
    val input = readInput("Day10")
        .filter(String::isNotEmpty)
        .map { line ->
            line.map { it.digitToIntOrNull() ?: -1 }
        }

    fun getReachablePeaks(start: Position): List<Position> {
        val currentHeight = input.getOrNull(start)
            ?: return emptyList()

        if (currentHeight == 9) {
            return listOf(start)
        }

        return sequenceOf(
            Position(Pair(start.x + 1, start.y)),
            Position(Pair(start.x - 1, start.y)),
            Position(Pair(start.x, start.y + 1)),
            Position(Pair(start.x, start.y - 1)),
        )
            .filter { input.getOrNull(it) == currentHeight + 1 }
            .map(::getReachablePeaks)
            .flatten()
            .toList()
    }

    var totalScore = 0
    var totalRating = 0

    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, height ->
            if (height == 0) {
                val reachablePicks = getReachablePeaks(Position(Pair(x, y)))
                totalScore += reachablePicks.distinct().size
                totalRating += reachablePicks.size
            }
        }
    }
    totalScore.println()
    totalRating.println()
}


@JvmInline
value class Position(private val pair: Pair<Int, Int>) {

    val x: Int
        get() = pair.first

    val y: Int
        get() = pair.second
}