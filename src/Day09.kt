fun main() {

    fun getHistoryNextLine(history: List<Int>): List<Int> =
        (0 until history.size - 1).map { i -> history[i + 1] - history[i] }

    fun getHistories(line: String): List<List<Int>> {
        var currentHistory = line.split(" ").mapNotNull { it.toIntOrNull() }
        val histories = mutableListOf(currentHistory)
        while (!currentHistory.all { it == 0 }) {
            currentHistory = getHistoryNextLine(currentHistory)
            histories.add(currentHistory)
        }
        return histories
    }

    fun getHistoryValue(line: String): Int {
        val histories = getHistories(line)
        var counter = 0
        (histories.size - 2 downTo 0).forEach { i -> counter += histories[i].last() }
        return counter
    }


    fun part1(input: List<String>): Int =
        input.sumOf(::getHistoryValue)

    fun getHistoryValue2(line: String): Int {
        val histories = getHistories(line)
        var counter = 0
        (histories.size - 2 downTo 0).forEach { i -> counter = histories[i].first() - counter }
        return counter
    }

    fun part2(input: List<String>): Int =
        input.sumOf(::getHistoryValue2)


    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
