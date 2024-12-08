fun main() {

    val input = readInput("Day08").let { lines ->
        Input(frequencies = lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Point(x, y) to c }.filter { (_, frequency) -> frequency.isLetterOrDigit() }
        }.groupBy({ (_, frequency) -> frequency }) { (point, _) -> point }.values,
            max = Point(lines[0].length, lines.size)
        )
    }
    val antiNodes: (Point, Point) -> List<Point> =
        { a, b -> (a + a - b).let { if (it.withinBounds(input.max)) listOf(it) else emptyList() } }

    val antiNodes2: (Point, Point) -> List<Point> =
        { a, b -> generateSequence(a) { it + a - b }.takeWhile { it.withinBounds(input.max) }.toList() }

    input.frequencies.flatMap { points ->
        generateCombinations(points, 2).filter { (a, b) -> a != b }.flatMap { (a, b) -> antiNodes(a, b) }
    }.distinct().size.println()

    input.frequencies.flatMap { points ->
        generateCombinations(points, 2).filter { (a, b) -> a != b }.flatMap { (a, b) -> antiNodes2(a, b) }
    }.distinct().size.println()

}

data class Input(val frequencies: Collection<List<Point>>, val max: Point)