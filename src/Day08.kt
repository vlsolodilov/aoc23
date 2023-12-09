import kotlin.math.abs

fun main() {

    fun getNetworkLine(line: String): Pair<String, Pair<String, String>> {
        val matches = Regex("[a-zA-Z0-9]+").findAll(line)
        val result = matches.map { it.value }.toList()
        return Pair(result[0], Pair(result[1], result[2]))
    }


    fun part1(input: List<String>): Int {
        val navigation = input.first().toCharArray()
        val network: Map<String, Pair<String, String>> =
            input.drop(2).associate { getNetworkLine(it) }
        var currentPosition = "AAA"
        var counter = 0
        while (currentPosition != "ZZZ") {
            val command = navigation.getOrNull(counter % navigation.size) ?: throw Exception()
            currentPosition = when (command) {
                'L' -> network[currentPosition]?.first ?: throw Exception()
                'R' -> network[currentPosition]?.second ?: throw Exception()
                else -> throw Exception()
            }
            counter++
        }
        return counter
    }

    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(numbers: List<Long>): Long = numbers.reduce { a, b -> a * b / gcd(a, b) }

    fun part2(input: List<String>): Long {
        val navigation = input.first().toCharArray()
        val network: Map<String, Pair<String, String>> =
            input.drop(2).associate { getNetworkLine(it) }
        val counters = mutableListOf<Long>()
        network.keys.filter { it.endsWith("A") }.forEach { position ->
            var counter = 0L
            var currentPosition = position
            while (!currentPosition.endsWith("Z")) {
                val command = navigation.getOrNull((counter % navigation.size).toInt()) ?: throw Exception()
                currentPosition = when (command) {
                    'L' -> network[currentPosition]?.first ?: throw Exception()
                    'R' -> network[currentPosition]?.second ?: throw Exception()
                    else -> throw Exception()
                }
                counter++
            }
            counters.add(counter)
        }
        return lcm(counters)
    }


    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    val testInput3 = readInput("Day08_test3")
    check(part1(testInput) == 2)
    check(part1(testInput2) == 6)
    check(part2(testInput3) == 6L)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
