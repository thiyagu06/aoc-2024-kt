fun main() {
    val input = readInput("Day14")
    Floor(input.map { Robot.of(it) })
        .elapse(100)
        .getSafetyFactor().println();

    Floor(input.map { Robot.of(it) })
        .findChristmasTree().println()
}

class Floor(private val robots: List<Robot>) {

    fun elapse(times: Int): Floor {
        repeat(times) {
            robots.forEach(Robot::move)
        }
        return this
    }

    fun getSafetyFactor(): Int {
        return robots.count { it.x in 0..49 && it.y in 0..50 }
            .times(robots.count { it.x in 51..100 && it.y in 0..50 })
            .times(robots.count { it.x in 0..49 && it.y in 52..102 })
            .times(robots.count { it.x in 51..100 && it.y in 52..102 })
    }

    fun findChristmasTree(): Int {
        repeat(10000) {
            robots.forEach(Robot::move)
            if (containsChristmasTree()) {
                return it + 1
            }
        }
        return -1
    }

    private fun containsChristmasTree(): Boolean {
        val floor = Array(101) { Array(103) { '.' } }
        robots.forEach { floor[it.x][it.y] = '#' }

        if (floor.any { it.joinToString("").contains("##########")}) {
            floor.forEach { println(it.joinToString("")) }
            println()
            return true
        }
        return false
    }

}

class Robot(var x: Int, var y: Int, var dx: Int, var dy: Int) {
    companion object {
        fun of(input: String): Robot {
            val robot = Regex("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)")
            val (x, y, dx, dy) = robot.find(input)!!.destructured
            return Robot(x.toInt(), y.toInt(), dx.toInt(), dy.toInt())
        }
    }

    fun move() {
        val newX = (x + dx) % 101
        val newY = (y + dy) % 103

        x = if (newX >= 0) newX else 101 + newX
        y = if (newY >= 0) newY else 103 + newY
    }
}