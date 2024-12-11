
fun main() {
    val input = readInput("Day11")[0].split(' ').map { it.toLong() }
    simulateBlinks(input, 25).println()
    simulateBlinks(input, 75).println()
}

private val cache = mutableMapOf<String, Long>()
private fun createCacheKey(stone: Long, blink: Int) = "$stone,$blink"

private fun simulateBlinks(stones: List<Long>, numBlinks: Int): Long {
    cache.clear()
    return stones.sumOf { stone -> countStoneSplits(stone, numBlinks) }
}

fun Long.countDigits(): Int = when (this) {
    0L -> 1
    else -> this.toString().length
}

private fun countStoneSplits(stone: Long, blink: Int): Long {
    val cacheKey = createCacheKey(stone, blink)
    if (cache.containsKey(cacheKey)) {
        return cache[cacheKey]!!
    }
    if (blink == 0) {
        return 1L
    }
    val stoneCount = when {
        stone == 0L -> countStoneSplits(1, blink - 1)
        stone.countDigits() % 2 == 0 -> {
            val stoneStr = stone.toString()
            val numDigits = stoneStr.length
            val mid = numDigits / 2
            return countStoneSplits(
                stoneStr.substring(0, mid).toLong(),
                blink - 1,
            ) + countStoneSplits(stoneStr.substring(mid).toLong(), blink - 1)
        }
        else -> countStoneSplits(stone * 2024, blink - 1)
    }
    cache[cacheKey] = stoneCount
    return stoneCount
}
