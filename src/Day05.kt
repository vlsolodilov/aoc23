fun main() {

    fun getAlmanacMap(lines: List<String>): AlmanacMap {
        val almanacLines = mutableListOf<AlmanacLine>()
        for (line in lines) {
            val (dest, source, range) = line.split(" ")
            almanacLines.add(AlmanacLine(dest.toLong(), source.toLong(), range.toLong()))
        }
        return AlmanacMap(almanacLines)
    }

    fun getAlmanacMaps(lines: List<String>): List<AlmanacMap> {
        val almanacMaps = mutableListOf<AlmanacMap>()
        val strs = mutableListOf<String>()
        lines.forEach { line ->
            if (line.contains("map")) {
                return@forEach
            }
            if (line.isEmpty()) {
                almanacMaps.add(getAlmanacMap(strs))
                strs.clear()
            } else {
                strs.add(line)
            }
        }
        almanacMaps.add(getAlmanacMap(strs))
        return almanacMaps
    }

    fun part1(input: List<String>): Long {
        val seeds = "\\d+".toRegex().findAll(input.first()).map { it.value.toLong() }.toList()
        val almanacMaps: List<AlmanacMap> = getAlmanacMaps(input.drop(2))
        val locations = mutableListOf<Long>()
        seeds.forEach { seed ->
            var currentValue = seed
            almanacMaps.forEach nextMap@{ almanacMap ->
                almanacMap.lines.forEach { line ->
                    if (currentValue in line.source..(line.source + line.range)) {
                        currentValue = line.dest + currentValue - line.source
                        return@nextMap
                    }
                }
            }
            locations.add(currentValue)
        }

        return locations.min()
    }


    fun part2(input: List<String>): Long {
        val seedPairs = "\\d+".toRegex().findAll(input.first())
            .map { it.value.toLong() }
            .toList()
            .chunked(2)
            .map { it[0] to it[1] }
        val almanacMaps: List<AlmanacMap> = getAlmanacMaps(input.drop(2))
        var minLocation = Long.MAX_VALUE
        seedPairs.forEach { seedPair ->
            (seedPair.first until seedPair.first + seedPair.second).forEach { seed ->
                var currentValue = seed
                almanacMaps.forEach nextMap@{ almanacMap ->
                    almanacMap.lines.forEach { line ->
                        if (currentValue in line.source..(line.source + line.range)) {
                            currentValue = line.dest + currentValue - line.source
                            return@nextMap
                        }
                    }
                }
                if (minLocation > currentValue) minLocation = currentValue
            }
        }

        return minLocation
    }


    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class AlmanacMap(val lines: List<AlmanacLine>)
data class AlmanacLine(val dest: Long, val source: Long, val range: Long)
