import kotlin.math.pow

fun main() {

    fun parseCard(line: String): Card {
        val (card, results) = line.split(":")
        val number = card.filter { it.isDigit() }.toIntOrNull() ?: 0
        val numbersStr = results.split("|").map { it.trim() }
        val numbers = numbersStr.map { it.split(" ").mapNotNull { number -> number.toIntOrNull()} }
        return Card(number = number, numbers = numbers)
    }

    fun getPointByCard(card: Card): Int {
        return card.numbers[0].count{ num -> card.numbers[1].contains(num) }
    }

    fun part1(input: List<String>): Int {
        val cards = input.map { parseCard(it) }
        return cards.sumOf { 2.0.pow(getPointByCard(it) - 1).toInt() }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { parseCard(it) }
        val maxCardNumber = cards.maxBy { it.number }.number
        val allCards = cards.toMutableList()
        cards.forEach { card ->
            val currentNumber = card.number
            val points = getPointByCard(card)
            if (points == 0) return@forEach
            val cardCount = allCards.count { it.number == currentNumber }
            (1..points).forEach { point ->
              if (currentNumber + point <= maxCardNumber) {
                  (1..cardCount).onEach { allCards.add(cards.firstOrNull { it.number == currentNumber + point } ?: throw Error()) }
              }
            }
        }
        return allCards.size
    }


    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Card(val number: Int, val numbers: List<List<Int>>)