fun part1(input: List<List<Char>>): Int {
    var count = 0
    for (i in input.indices) {
        for (j in input[0].indices) {
            val ij = Coord(i, j)
            if (input[ij] == 'X') {
                if (input.isXmas(ij, ij.goDown(1), ij.goDown(2), ij.goDown(3))) count++
                if (input.isXmas(ij, ij.goUp(1), ij.goUp(2), ij.goUp(3))) count++
                if (input.isXmas(ij, ij.goLeft(1), ij.goLeft(2), ij.goLeft(3))) count++
                if (input.isXmas(ij, ij.goRight(1), ij.goRight(2), ij.goRight(3))) count++

                if (input.isXmas(
                        ij,
                        ij.goDown(1).goRight(1),
                        ij.goDown(2).goRight(2),
                        ij.goDown(3).goRight(3)
                    )
                ) count++
                if (input.isXmas(
                        ij,
                        ij.goDown(1).goLeft(1),
                        ij.goDown(2).goLeft(2),
                        ij.goDown(3).goLeft(3)
                    )
                ) count++
                if (input.isXmas(
                        ij,
                        ij.goUp(1).goRight(1),
                        ij.goUp(2).goRight(2),
                        ij.goUp(3).goRight(3)
                    )
                ) count++
                if (input.isXmas(ij, ij.goUp(1).goLeft(1), ij.goUp(2).goLeft(2), ij.goUp(3).goLeft(3))) count++
            }
        }
    }
    return count
}

fun part2(input: List<List<Char>>): Int {
    var count = 0
    for (i in 1..<input.lastIndex) {
        for (j in 1..<input[0].lastIndex) {
            if (input[Coord(i, j)] == 'A') {
                if (input.isXCrossedMasPattern(i, j)) count++
            }
        }
    }
    return count
}

private fun List<List<Char>>.isXmas(
    x: Coord,
    m: Coord,
    a: Coord,
    s: Coord,
): Boolean =
    listOf(x, m, a, s).all { it.validIndex(this) } &&
            this[x] == 'X' &&
            this[m] == 'M' &&
            this[a] == 'A' &&
            this[s] == 'S'

private fun List<List<Char>>.isXCrossedMasPattern(
    i: Int,
    j: Int,
): Boolean {
    val ij = Coord(i, j)
    return isXCrossedMas(
        ij.goUp().goLeft(),
        ij,
        ij.goDown().goRight(),
        ij.goUp().goRight(),
        ij.goDown().goLeft()
    ) ||
            isXCrossedMas(
                ij.goDown().goRight(),
                ij,
                ij.goUp().goLeft(),
                ij.goDown().goLeft(),
                ij.goUp().goRight()
            ) ||
            isXCrossedMas(
                ij.goUp().goLeft(),
                ij,
                ij.goDown().goRight(),
                ij.goDown().goLeft(),
                ij.goUp().goRight()
            ) ||
            isXCrossedMas(ij.goDown().goRight(), ij, ij.goUp().goLeft(), ij.goUp().goRight(), ij.goDown().goLeft())
}

private fun List<List<Char>>.isXCrossedMas(
    m: Coord,
    a: Coord,
    s: Coord,
    m2: Coord,
    s2: Coord,
): Boolean =
    listOf(m, a, s, m2, s2).all { it.validIndex(this) } &&
            this[m] == 'M' &&
            this[a] == 'A' &&
            this[s] == 'S' &&
            this[m2] == 'M' &&
            this[s2] == 'S'

fun main() {

    // Read the input from the `src/Day02.txt` file.
    val input = readInputAsString("Day04")
    val wordSearch = input.split("\n").map { it.toCharArray().toList() }
    part1(wordSearch).println()
    part2(wordSearch).println()
}