fun main() {
    fun getDigit(line: String): Int {
        val digits = line.replace(Regex("[^\\d]"), "")
        return (digits.firstOrNull()?.digitToIntOrNull() ?: 0) * 10 + (digits.lastOrNull()
            ?.digitToIntOrNull() ?: 0)
    }


    fun part1(input: List<String>): Int =
        input.sumOf { line -> getDigit(line) }


    fun foundDigits(line: String): String {
        val digitMap = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
        )
        val foundDigits = mutableMapOf<Int, String>()


        digitMap.keys.forEach { word ->

            var currentIndex = line.indexOf(word)
            while (currentIndex != -1) {
                foundDigits[currentIndex] = digitMap.getOrDefault(word, "")
                currentIndex = line.indexOf(word, currentIndex + word.length)
            }
        }

        line.forEachIndexed { index, c ->
            if (c.isDigit()) {
                foundDigits[index] = c.toString()
            }
        }

        return foundDigits.toSortedMap().values.joinToString(separator = "")
    }

    fun part2(input: List<String>): Int =
        input.sumOf { line ->
            getDigit(foundDigits(line))
        }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val test2Input = readInput("Day01_test2")
    check(part1(testInput) == 142)
    check(part2(test2Input) == 281)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
