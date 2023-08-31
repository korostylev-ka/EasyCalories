package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.dao.NutrientsDao
import ru.korostylev.easycalories.entity.NutrientsEntity


class NutrientsRepositoryImpl(val nutrientsDao: NutrientsDao): NutrientsRepository {
    //var data: List<Pair<Float, Float>> =  emptyList()
    override var dayId: Int = 1
    set(value) {
        field = value
    }
    //заглушка с нулевыми нутриентами
    override val emptyNutrients = NutrientsEntity(0, 0F, 0F, 0F, 0F)
    //установленные лимиты нутриентов
    override var limitsOfNutrients: NutrientsEntity = getLimits() ?: emptyNutrients
    override var liveDataLimitsOfNutrients: MutableLiveData<NutrientsEntity> = MutableLiveData(limitsOfNutrients)

    //текущее потребление нутрентов
    override var actualEatenNutrients: NutrientsEntity = getTheNutrients(dayId) ?: emptyNutrients
    //связь лимита и текущего значения для графика
    override var data: List<Pair<Float, Float>> = listOf(Pair(limitsOfNutrients.proteins, actualEatenNutrients.proteins), Pair(limitsOfNutrients.fats, actualEatenNutrients.fats), Pair(limitsOfNutrients.carbs, actualEatenNutrients.carbs))
    override var liveDataDiagram = MutableLiveData(data)

    var thatDayData: Map<Int, List<Float>> = emptyMap()

    override fun setLimits(limit: NutrientsEntity) {
        nutrientsDao.insert(limit)
        liveDataLimitsOfNutrients.value = limit
        liveDataDiagram.value = listOf(Pair(limit.proteins, actualEatenNutrients.proteins), Pair(limit.fats, actualEatenNutrients.fats), Pair(limit.carbs, limit.carbs))
        limitsOfNutrients = limit
        println("В репозитории ${limitsOfNutrients}")
    }

    override fun getLimits(): NutrientsEntity? {
        return nutrientsDao.getTheNutrients(0)
    }

    override fun getTheNutrients(dayId: Int): NutrientsEntity? {
        return nutrientsDao.getTheNutrients(dayId)
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