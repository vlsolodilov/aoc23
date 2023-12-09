fun main() {
    fun getCamelCard(line: String): Pair<String, Long> {
        val res = line.split(" ")
        return Pair(res[0].trim(), res[1].toLong())
    }

    fun getCardValue(rank: Char, isJoker: Boolean = false): Int =
        when (rank) {
            'T' -> 10
            'J' -> if (isJoker) 1 else 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> rank.digitToIntOrNull() ?: 0
        }

    fun isFive(hand: Map<Char, Int>): Boolean =
        hand.values.count { it == 5 } == 1

    fun isFour(hand: Map<Char, Int>): Boolean =
        hand.values.count { it == 4 } == 1

    fun isFullHouse(hand: Map<Char, Int>): Boolean =
        hand.values.count { it == 3 } == 1 && hand.values.count { it == 2 } == 1

    fun isThree(hand: Map<Char, Int>): Boolean =
        hand.values.count { it == 3 } == 1

    fun isTwoPair(hand: Map<Char, Int>): Boolean =
        hand.values.count { it == 2 } == 2

    fun isOnePair(hand: Map<Char, Int>): Boolean =
        hand.values.count { it == 2 } == 1

    fun isHigh(hand: Map<Char, Int>): Boolean {
        val handValue = hand.keys.map(::getCardValue).sorted()
        return (0 until handValue.size - 1).all { i -> handValue[i + 1] - handValue[i] == 1 }
    }

    fun getHandCost(cards: String): Int {
        val hand = cards.toCharArray().groupBy { it }.mapValues { it.value.size }
        return when {
            isFive(hand) -> 7
            isFour(hand) -> 6
            isFullHouse(hand) -> 5
            isThree(hand) -> 4
            isTwoPair(hand) -> 3
            isOnePair(hand) -> 2
            isHigh(hand) -> 1
            else -> 0
        }
    }

    fun getHandCostWithJoker(cards: String): Int {
        val hand = cards.toCharArray().groupBy { it }.mapValues { it.value.size }
        val handWithoutJokers = hand.filterKeys { it != 'J' }
        val jokerCount = hand['J'] ?: 0
        if (jokerCount >= 4) return 7
        return when {
            isFive(handWithoutJokers) || isFour(handWithoutJokers) && jokerCount == 1 || isThree(
                handWithoutJokers
            ) && jokerCount == 2 || isOnePair(handWithoutJokers) && jokerCount == 3 -> 7
            isFour(handWithoutJokers) || isThree(handWithoutJokers) && jokerCount == 1 || isOnePair(
                handWithoutJokers
            ) && jokerCount == 2 || jokerCount == 3 -> 6
            isFullHouse(handWithoutJokers) || isTwoPair(handWithoutJokers) && jokerCount == 1 || jokerCount == 3 -> 5
            isThree(handWithoutJokers) || isOnePair(hand) && jokerCount == 1 || jokerCount == 2 -> 4
            isTwoPair(handWithoutJokers) -> 3
            isOnePair(handWithoutJokers) || jokerCount == 1 -> 2
            isHigh(handWithoutJokers) -> 1
            else -> 0
        }
    }

    fun compareCardValues(hand1: String, hand2: String, isJoker: Boolean = false): Int {
        val values1 = hand1.toCharArray().map { getCardValue(it, isJoker) }
        val values2 = hand2.toCharArray().map { getCardValue(it, isJoker) }
        (hand1.indices).forEach { i ->
            val elementComparison = values1[i].compareTo(values2[i])
            if (elementComparison != 0) {
                return elementComparison
            }
        }
        return 0
    }

    fun part1(input: List<String>): Long {
        val camelCardMap = input.associate(::getCamelCard)
        val cardComparator = compareBy<String> { getHandCost(it) }.thenComparator { hand1, hand2 ->
            compareCardValues(
                hand1,
                hand2
            )
        }
        val sortedHands = camelCardMap.keys.sortedWith(cardComparator)
        return sortedHands.mapNotNull { camelCardMap[it] }
            .reduceIndexed { index, acc, l -> acc + (index + 1) * l }
    }

    fun part2(input: List<String>): Long {
        val camelCardMap = input.associate(::getCamelCard)
        val cardComparator =
            compareBy<String> { getHandCostWithJoker(it) }.thenComparator { hand1, hand2 ->
                compareCardValues(
                    hand1,
                    hand2,
                    true
                )
            }
        val sortedHands = camelCardMap.keys.sortedWith(cardComparator)
        return sortedHands.mapNotNull { camelCardMap[it] }
            .reduceIndexed { index, acc, l -> acc + (index + 1) * l }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
