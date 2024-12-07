fun main() {

    val input = readInput("Day07")
    val add: (Long, Long) -> Long = { a, b -> a + b }
    val multiply: (Long, Long) -> Long = { a, b -> a * b }
    val join: (Long, Long) -> Long = { a, b -> "$a$b".toLong() }
    val calibrations = input.map {
        val parts = it.split(": ")
        val target = parts[0].toLong()
        val nums = parts[1].split(" ").map { operands -> operands.toLong() }
        target to nums
    }

    calibrations.filter { (target, operands) -> canReach(target, operands, listOf(add, multiply)) }
        .sumOf { (target, _) -> target }.println()
    calibrations.filter { (target, operands) -> canReach(target, operands, listOf(add, multiply, join)) }
        .sumOf { (target, _) -> target }.println()
}

fun canReach(result: Long, operands: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {
    if (operands.size == 1) {
        return operands[0] == result
    }

    return operators.any {
        canReach(result, listOf(it(operands[0], operands[1])) + operands.drop(2), operators)
    }
}