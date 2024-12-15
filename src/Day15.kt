fun main() {
    val input = readInput("Day15")
    part1(input)
    part2(input)
}

private fun part1(lines: List<String>) {
    val (gridLines, moveLines) = lines.split("")
    val grid = gridLines.map { it.toCharArray() }
    val moves = moveLines.asSequence().flatMap { it.asSequence() }.map(Route::ofChar)
    var pos = findRobot(grid)

    for (move in moves) {
        pos = tryMove(pos, move, grid)
    }

    println(sumGps(grid, 'O'))
}

private fun part2(lines: List<String>) {
    val (gridLines, moveLines) = lines.split("")
    val grid = gridLines.map { doubleWidthLine(it) }
    val moves = moveLines.asSequence().flatMap { it.asSequence() }.map(Route::ofChar)
    var pos = findRobot(grid)

    for (move in moves) {
        if (canMoveBig(pos, move, grid)) {
            moveBig(pos, move, grid)
            pos = pos.move(move)
        }
    }

    println(sumGps(grid, '['))
}

private fun findRobot(grid: List<CharArray>): Pt {
    for ((y, row) in grid.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == '@') {
                return Pt(x, y)
            }
        }
    }
    error("Couldn't find robot")
}

private fun tryMove(pos: Pt, dir: Route, grid: List<CharArray>): Pt {
    var current = pos
    while (true) {
        current = current.move(dir)
        when (grid[current]) {
            '#' -> return pos
            '.' -> break
        }
    }
    grid[current] = 'O'
    grid[pos] = '.'
    grid[pos.move(dir)] = '@'
    return pos.move(dir)
}

private fun canMoveBig(pos: Pt, dir: Route, grid: List<CharArray>): Boolean {
    val char = grid[pos]
    when (char) {
        '.' -> return true
        '#' -> return false
        '@' -> return canMoveBig(pos.move(dir), dir, grid)
    }
    check(char == '[' || char == ']')

    if (dir.dy == 0) {
        return canMoveBig(pos.move(dir), dir, grid)
    }

    return when (char) {
        '[' -> canMoveBig(pos.move(dir), dir, grid) &&
                canMoveBig(pos.move(Route.RIGHT).move(dir), dir, grid)
        ']' -> canMoveBig(pos.move(dir), dir, grid) &&
                canMoveBig(pos.move(Route.LEFT).move(dir), dir, grid)
        else -> error("Invalid char $char")
    }
}

private fun moveBig(pos: Pt, dir: Route, grid: List<CharArray>) {
    val char = grid[pos]
    when (char) {
        '.' -> return
        '#' -> error("Tried to move wall!")
        '[' -> {
            if (dir.dx == 0) {
                moveBig(pos.move(dir), dir, grid)
                moveBig(pos.move(Route.RIGHT).move(dir), dir, grid)
                grid[pos.move(dir)] = '['
                grid[pos.move(Route.RIGHT).move(dir)] = ']'
                grid[pos] = '.'
                grid[pos.move(Route.RIGHT)] = '.'
                return
            }
        }
        ']' -> {
            if (dir.dx == 0) {
                moveBig(pos.move(dir), dir, grid)
                moveBig(pos.move(Route.LEFT).move(dir), dir, grid)
                grid[pos.move(dir)] = ']'
                grid[pos.move(Route.LEFT).move(dir)] = '['
                grid[pos] = '.'
                grid[pos.move(Route.LEFT)] = '.'
                return
            }
        }
    }
    moveBig(pos.move(dir), dir, grid)
    grid[pos.move(dir)] = char
    grid[pos] = '.'
}

private fun doubleWidthLine(line: String): CharArray {
    val result = CharArray(line.length * 2)
    for ((x, char) in line.withIndex()) {
        result[x * 2] = when (char) {
            'O' -> '['
            else -> char
        }
        result[x * 2 + 1] = when (char) {
            'O' -> ']'
            '@' -> '.'
            else -> char
        }
    }
    return result
}

private fun sumGps(grid: List<CharArray>, boxChar: Char): Int {
    var total = 0
    for ((y, row) in grid.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == boxChar) {
                total += 100 * y + x
            }
        }
    }
    return total
}

private data class Pt(val x: Int, val y: Int) {
    fun move(dir: Route) = Pt(x + dir.dx, y + dir.dy)
}

private operator fun List<CharArray>.get(pt: Pt) = this[pt.y][pt.x]
private operator fun List<CharArray>.set(pt: Pt, value: Char) {
    this[pt.y][pt.x] = value
}

private enum class Route(val dx: Int, val dy: Int) {
    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1);

    companion object {
        fun ofChar(char: Char) = when (char) {
            '<' -> LEFT
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            else -> error("Invalid direction $char")
        }
    }
}

private fun <T> List<T>.split(separator: T): List<List<T>> {
    val result = mutableListOf(mutableListOf<T>())
    for (item in this) {
        if (item == separator) {
            result.add(mutableListOf())
        } else {
            result.last().add(item)
        }
    }
    return result
}