import java.lang.IllegalArgumentException

fun main() {

    fun parseCubes(cubesStr: String): List<Cube> {
        val cubeRegex = Regex("""(\d+) (\w+)""")

        return cubeRegex.findAll(cubesStr).map { matchResult ->
            val (quantity, colorStr) = matchResult.destructured
            val color = when (colorStr) {
                "red" -> Color.RED
                "green" -> Color.GREEN
                "blue" -> Color.BLUE
                else -> throw IllegalArgumentException()
            }
            Cube(color, quantity.toIntOrNull() ?: throw IllegalArgumentException())
        }.toList()
    }

    fun parseGame(line: String): Game {
        val (game, results) = line.split(":")
        val number = game.split(" ")[1].toIntOrNull() ?: 0
        val setsStr = results.split(";").map { it.trim() }
        val sets = setsStr.map { parseCubes(it) }
        return Game(number = number, sets = sets)
    }

    fun isPossibleGame(game: Game, maxRedCubes: Int, maxGreenCubes: Int, maxBlueCubes: Int): Boolean {
        game.sets.flatten().forEach { cube ->
            when (cube.color) {
                Color.RED -> if (cube.quantity > maxRedCubes) return false
                Color.GREEN -> if (cube.quantity > maxGreenCubes) return false
                Color.BLUE -> if (cube.quantity > maxBlueCubes) return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val maxRedCubes = 12
        val maxGreenCubes = 13
        val maxBlueCubes = 14
        val games = input.map { parseGame(it) }
        return games.filter { game ->  isPossibleGame(game, maxRedCubes, maxGreenCubes, maxBlueCubes) }.sumOf { it.number }
    }

    fun getPowerGame(game: Game): Int {
        var maxRedCubes = 1
        var maxGreenCubes = 1
        var maxBlueCubes = 1
        game.sets.forEach { set ->
            set.forEach { cube ->
                when (cube.color) {
                    Color.RED -> maxRedCubes = cube.quantity.coerceAtLeast(maxRedCubes)
                    Color.GREEN -> maxGreenCubes = cube.quantity.coerceAtLeast(maxGreenCubes)
                    Color.BLUE -> maxBlueCubes = cube.quantity.coerceAtLeast(maxBlueCubes)
                }
            }
        }
        return maxRedCubes * maxGreenCubes * maxBlueCubes
    }

    fun part2(input: List<String>): Int {
        val games = input.map { parseGame(it) }
        return games.sumOf { getPowerGame(it) }
    }


    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

data class Game(val number: Int, val sets: List<List<Cube>>)
data class Cube(val color: Color, val quantity: Int)
enum class Color { RED, GREEN, BLUE }