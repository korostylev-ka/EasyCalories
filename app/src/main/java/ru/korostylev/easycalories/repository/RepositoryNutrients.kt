package ru.korostylev.easycalories.repository

class RepositoryNutrients {
    //var data: List<Pair<Float, Float>> =  emptyList()
    var dayId: Int = 0
    set(value) {
        field = value
    }
    var limitsOfNutrients: List<Float> = listOf(100F, 100F, 100F)
    var actualEatenNutrients: MutableList<Float> = listOf(0F, 0F, 0F).toMutableList()
    var thatDayData: Map<Int, Pair<List<Float>, List<Float>>> = emptyMap()

    fun addLimits(protein: Float, fat: Float, carbs: Float) {
        limitsOfNutrients = listOf(protein, fat, carbs)
    }
    fun addProtein(protein: Float) {
        actualEatenNutrients[0] = protein
    }
    fun addFat(fat: Float) {
        actualEatenNutrients[1] = fat
    }
    fun addCarbs(carbs: Float) {
        actualEatenNutrients[2] = carbs
    }

    init{

    }
}