fun main() {

    fun getNumbers(line: String, y: Int): List<PartNumber> {
        val numbers = mutableListOf<PartNumber>()
        var currentNumber: Int? = null
        var startIndex: Int? = null
        line.plus('.').forEachIndexed { index, c ->
            if (c.isDigit()) {
                if (currentNumber == null) {
                    currentNumber = c.digitToIntOrNull()
                    startIndex = index
                } else {
                    currentNumber = currentNumber!! * 10 + c.digitToInt()
                }
            } else if (!c.isDigit()) {
                currentNumber?.let {
                    numbers.add(
                        PartNumber(
                            x = IntRange(startIndex!!,index - 1),
                            y = y,
                            value = it,
                        )
                    )
                }
                currentNumber = null
                startIndex = null
            }
        }
        return numbers
    }

    fun getSymbolPositions(line: String, y: Int): List<Pair<Int, Int>> =
        line.mapIndexedNotNull { index, char ->
            if (char != '.' && !char.isDigit()) index to y else null
        }

    fun getStarPositions(line: String, y: Int): List<Pair<Int, Int>> =
        line.mapIndexedNotNull { index, char ->
            if (char == '*') index to y else null
        }

    fun part1(input: List<String>): Int {
        val numbers = input.mapIndexed { index, s -> getNumbers(s, index) }.flatten().toMutableList()
        val symbols = input.mapIndexed { index, s -> getSymbolPositions(s, index) }.flatten()
        val partNumbers = mutableListOf<Int>()
        symbols.forEach { (x,y) ->
            val foundedNumbers = numbers.filter { number -> number.x.any { it in IntRange(x - 1, x + 1) } && number.y in IntRange(y - 1, y + 1) }
            partNumbers.addAll(foundedNumbers.map { it.value })
            numbers.removeAll(foundedNumbers)
        }

        return partNumbers.toList().sum()
    }

    fun part2(input: List<String>): Int {
        val numbers = input.mapIndexed { index, s -> getNumbers(s, index) }.flatten().toMutableList()
        val stars = input.mapIndexed { index, s -> getStarPositions(s, index) }.flatten()
        val partNumbers = mutableListOf<Int>()
        stars.forEach { (x,y) ->
            val foundedNumbers = numbers.filter { number -> number.x.any { it in IntRange(x - 1, x + 1) } && number.y in IntRange(y - 1, y + 1) }
            if (foundedNumbers.size == 2) {
                partNumbers.add(foundedNumbers.map { it.value }.reduce { acc, i -> acc * i })
                numbers.removeAll(foundedNumbers)
            }
        }
        return partNumbers.toList().sum()
    }


    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

data class PartNumber(val x: IntRange, val y: Int, val value: Int)