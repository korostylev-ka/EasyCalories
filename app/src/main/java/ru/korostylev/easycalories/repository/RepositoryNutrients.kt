package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.dao.NutrientsDao
import ru.korostylev.easycalories.entity.NutrientsEntity


class RepositoryNutrients(val nutrientsDao: NutrientsDao) {
    //var data: List<Pair<Float, Float>> =  emptyList()
    var dayId: Int = 1
    set(value) {
        field = value
    }
    //заглушка с нулевыми нутриентами
    val emptyNutrients = NutrientsEntity(0, 0F, 0F, 0F)
    //установленные лимиты нутриентов
    var limitsOfNutrients: NutrientsEntity = getTheLimit() ?: emptyNutrients
    //текущее потребление нутрентов
    var actualEatenNutrients: NutrientsEntity = getTheNutrients(dayId) ?: emptyNutrients
    //связь лимита и текущего значения для графика
    var data: MutableList<Pair<Float, Float>> = mutableListOf(Pair(limitsOfNutrients.proteins, actualEatenNutrients.proteins), Pair(limitsOfNutrients.fats, actualEatenNutrients.fats), Pair(limitsOfNutrients.carbs, actualEatenNutrients.carbs))
    var thatDayData: Map<Int, List<Float>> = emptyMap()

    fun addLimits(limit: NutrientsEntity) {
        nutrientsDao.insert(limit)
        limitsOfNutrients = limit
        println("В репозитории ${limitsOfNutrients}")
    }

    fun getTheLimit(): NutrientsEntity? {
        return nutrientsDao.getTheNutrients(0)
    }

    fun getTheNutrients(id: Int): NutrientsEntity? {
        return nutrientsDao.getTheNutrients(id)
    }

    fun addProtein(protein: Float) {
        actualEatenNutrients = actualEatenNutrients.copy(proteins = protein)
    }
    fun addFat(fat: Float) {
        actualEatenNutrients = actualEatenNutrients.copy(fats = fat)
    }
    fun addCarbs(carbs: Float) {
        actualEatenNutrients = actualEatenNutrients.copy(carbs = carbs)
    }

    init{

        //nutrientsDao.insert(NutrientsEntity(2,20F, 40F, 60F))
    }
}