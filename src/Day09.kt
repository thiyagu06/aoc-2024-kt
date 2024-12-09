import java.util.*

fun main() {

    val input = readInputAsString("Day09")
    part1(input)
    part2(input)
}

private fun part2(input: String) {
    val (raw, blocks) = input.expandDisk2()
    raw.blockDeFragment(blocks)
    raw.calculateHash().println()
}

private fun part1(input: String) {
    val expanded = input.expandDisk1().toMutableList()
    expanded.deFragment()
    expanded.calculateHash().println()
}

fun String.expandDisk1() = fold(Triple(mutableListOf<Long>(), true, 0L)) { (current, file, id), c: Char ->
    val count = c.toIntValue()
    val toAdd = if (file) id else -id
    repeat(count) { current.add(toAdd) }
    Triple(current, !file, if (file) id + 1 else id)
}.first.dropLastWhile { it < 0 }

fun MutableList<Long>.deFragment() {
    var backIndex = indices.last()
    var frontIndex = indices.first
    while (backIndex > frontIndex) {
        while (get(backIndex) < 0) backIndex--
        while (get(frontIndex) >= 0) frontIndex++
        if (backIndex > frontIndex) swap(frontIndex, backIndex)
    }
}

fun List<Long>.calculateHash() = foldIndexed(0L) { index, acc, value -> if (value < 0) acc else acc + value * index }


fun String.expandDisk2() =
    fold(Triple(mutableListOf<Long>() to mutableListOf<DiskPart>(), true, 0L)) { (current, file, id), c: Char ->
        val (raw, parts) = current
        val size = c.toIntValue()
        val toAdd = if (file) id else -id
        val index = raw.size
        repeat(size) { raw.add(toAdd) }
        parts.add(DiskPart(size, !file, index))
        Triple(raw to parts, !file, if (file) id + 1 else id)
    }.first

fun MutableList<Long>.blockDeFragment(blocks: List<DiskPart>) {
    val freeSpace = mutableMapOf<Int, SortedSet<DiskPart>>()
    (1..9).forEach { space ->
        freeSpace[space] =
            blocks.filter { it.free }.filter { it.size >= space }.sortedBy { it.index }.toSortedSet(compareBy { it.index })
    }
    val movable = blocks.filter { !it.free }.sortedBy { -it.index }
    movable.forEach { toMove ->
        val freeSpaceBlock = freeSpace[toMove.size]!!.firstOrNull { it.index < toMove.index }
        if (freeSpaceBlock == null) return@forEach
        (1..freeSpaceBlock.size).forEach { freeSpace[it]?.remove(freeSpaceBlock) }
        repeat(toMove.size) { swap(toMove.index + it, freeSpaceBlock.index + it) }
        if (freeSpaceBlock.size > toMove.size) {
            val newFreeSpace = DiskPart(freeSpaceBlock.size - toMove.size, true, freeSpaceBlock.index + toMove.size)
            (1..newFreeSpace.size).forEach {
                freeSpace[it]!!.add(newFreeSpace)
            }
        }
    }
}

data class DiskPart(
    val size: Int, val free: Boolean, val index: Int
)