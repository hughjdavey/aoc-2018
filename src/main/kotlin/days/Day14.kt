package days

class Day14 : Day(14) {

    override fun partOne(): Any {
        return recipeScoresAfter(inputString.trim().toInt())
    }

    override fun partTwo(): Any {
        return recipeScoresBefore(inputString.trim())
    }

    data class Elf(var indexOfCurrentRecipe: Int) {

        fun chooseNext(scoreboard: List<Int>) {
            val steps = scoreboard[indexOfCurrentRecipe] + 1
            for (i in 0 until steps) {
                if (++indexOfCurrentRecipe > scoreboard.lastIndex) {
                    indexOfCurrentRecipe = 0
                }
            }
        }
    }

    companion object {

        // todo performance of next 2 methods isn't ideal but i have already improved it a lot!

        fun recipeScoresAfter(after: Int): String {
            val minSize = after + 10
            val elves = Day14.Elf(0) to Day14.Elf(1)

            var scoreboard = mutableListOf(3, 7)
            while (scoreboard.size <= minSize) {
                scoreboard = foo(elves, scoreboard)
                //System.err.println(minSize - scoreboard.size)
            }

            return scoreboard.dropLast(scoreboard.size - minSize).takeLast(10).joinToString("")
        }

        fun recipeScoresBefore(pattern: String): Int {
            val elves = Day14.Elf(0) to Day14.Elf(1)

            var scoreboard = mutableListOf(3, 7)
            while (!scoreboard.takeLast(10).joinToString("").contains(pattern)) {
                scoreboard = foo(elves, scoreboard)
                //System.err.println(scoreboard.size)
            }
            return scoreboard.joinToString("").indexOf(pattern)
        }

        fun foo(elves: Pair<Elf, Elf>, scoreboard: MutableList<Int>): MutableList<Int> {
            val elf1Current = scoreboard[elves.first.indexOfCurrentRecipe]
            val elf2Current = scoreboard[elves.second.indexOfCurrentRecipe]

            val sum = (elf1Current + elf2Current).toString()
            val newRecipes = sum.map { Character.getNumericValue(it) }

            //val toReturn = scoreboard.plus(newRecipes)
            scoreboard.addAll(newRecipes)
            elves.toList().forEach { it.chooseNext(scoreboard) }
            return scoreboard
        }

        fun scoreboardToString(elves: Pair<Elf, Elf>, scoreboard: List<Int>): String {
            return scoreboard.mapIndexed { index, recipe ->
                when (index) {
                    elves.first.indexOfCurrentRecipe -> "($recipe)"
                    elves.second.indexOfCurrentRecipe -> "[$recipe]"
                    else -> recipe.toString()
                }
            }.joinToString("  ")
        }
    }
}
