fun main() {

    fun parseValueList(line: String): List<Int> =
        "\\d+".toRegex().findAll(line)
            .mapNotNull { it.value.toIntOrNull() }
            .toList()

    fun getRaceRecords(race: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (totalTime, recordDistance) = race
        val records = mutableListOf<Pair<Int, Int>>()
        (1 until totalTime).forEach { time ->
            val currentDistance = time * (totalTime - time)
            if (currentDistance > recordDistance) records.add(Pair(time, currentDistance))
        }
        return records
    }

    fun part1(input: List<String>): Int {
        val times: List<Int> = parseValueList(input[0])
        val distances: List<Int> = parseValueList(input[1])
        val races: List<Pair<Int, Int>> = times.zip(distances)
        return races.map { race -> getRaceRecords(race).size }.reduce { acc, i -> acc * i }
    }

    fun parseValue(line: String): Long =
        line.replace(Regex("[^\\d]"), "").toLong()

    fun getRaceRecords(race: Pair<Long, Long>): Int {
        val (totalTime, recordDistance) = race
        var records = 0
        (1 until totalTime).forEach { time ->
            val currentDistance = time * (totalTime - time)
            if (currentDistance > recordDistance) records++
        }
        return records
    }

    fun part2(input: List<String>): Int {
        val time: Long = parseValue(input[0])
        val distance: Long = parseValue(input[1])
        return getRaceRecords(Pair(time, distance))
    }


    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
