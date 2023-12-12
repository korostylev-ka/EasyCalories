package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.dao.NutrientsDao
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.NutrientsEntity


class NutrientsRepositoryImpl(val nutrientsDao: NutrientsDao): NutrientsRepository {

    //заглушка с нулевыми нутриентами
    private val emptyNutrients = NutrientsEntity(0, 0F, 0F, 0F, 0F)

    override fun getAll(): LiveData<List<NutrientsEntity>> {
        return nutrientsDao.getAllLiveData()
    }

    override fun setLimits(limit: NutrientsEntity) {
        nutrientsDao.insert(limit)
    }

    override fun getLimits(): NutrientsEntity {
        return nutrientsDao.getTheNutrients(0)
    }

    override fun getDayActualNutrients(dayId: Int): NutrientsEntity {
        if (nutrientsDao.getTheNutrients(dayId) == null) {
            nutrientsDao.insert(emptyNutrients.copy(id = dayId))
            return emptyNutrients.copy(id = dayId)
        } else {
            return nutrientsDao.getTheNutrients(dayId)
        }
    }

    override fun addNutrients(nutrients: NutrientsEntity) {
        nutrientsDao.insert(nutrients)
    }

    override fun removeNutrients(eatenFoods: EatenFoods) {
        val actualNutrients = nutrientsDao.getTheNutrients(eatenFoods.dayId)
        val newProteins = actualNutrients.proteins - eatenFoods.portionProteins
        val newFats = actualNutrients.fats - eatenFoods.portionFats
        val newCarbs = actualNutrients.carbs - eatenFoods.portionCarbs
        val newCalories = actualNutrients.calories - eatenFoods.portionCalories
        val newNutrients = NutrientsEntity(eatenFoods.dayId, newProteins, newFats, newCarbs, newCalories)
        nutrientsDao.insert(newNutrients)
    }

    init {
        if (nutrientsDao.getTheNutrients(0) == null) {
            nutrientsDao.insert(emptyNutrients)
        }

    }
}

