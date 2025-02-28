package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.db.NutrientsDB
import ru.korostylev.easycalories.db.WaterDB
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.entity.WaterEntity
import ru.korostylev.easycalories.repository.NutrientsRepository
import ru.korostylev.easycalories.repository.NutrientsRepositoryImpl
import ru.korostylev.easycalories.repository.WaterRepository
import ru.korostylev.easycalories.repository.WaterRepositoryImpl

class NutrientsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NutrientsRepository = NutrientsRepositoryImpl(
        NutrientsDB.getInstance(application).nutrientsDao
    )
    private val waterRepository: WaterRepository = WaterRepositoryImpl(WaterDB.getInstance(application).waterDao)
    val liveDataNutrients = repository.getAll()
    val liveDataWater = waterRepository.getAllWaterLD()

    fun limitsOfNutrients() = repository.getLimits()

    fun getActualEatenNutrients(dayId: Int) = repository.getDayActualNutrients(dayId)

    fun setLimit(limit: NutrientsEntity) {
        repository.setLimits(limit)
    }

    fun addNutrients(id: Int, nutrients: NutrientsEntity) {
        try {
            val currentNutrients = repository.getDayActualNutrients(id)
            val changedNutrients = NutrientsEntity(id, currentNutrients!!.proteins + nutrients.proteins, currentNutrients!!.fats + nutrients.fats, currentNutrients!!.carbs + nutrients.carbs, currentNutrients!!.calories + nutrients.calories )
            repository.addNutrients(changedNutrients)
        } catch (e: Exception) {
            repository.addNutrients(nutrients)

        }
    }

    fun removeNutrients(dayId: Int, nutrients: NutrientsEntity) {
        try {
            val currentNutrients = repository.getDayActualNutrients(dayId)
            val changedNutrients = NutrientsEntity(dayId, currentNutrients.proteins - nutrients.proteins, currentNutrients.fats - nutrients.fats, currentNutrients.carbs - nutrients.carbs, currentNutrients.calories - nutrients.calories )
            repository.addNutrients(changedNutrients)
        } catch (e: Exception) {
            TODO()

        }
    }

    fun removeNutrients(eatenFoodsEntity: EatenFoodsEntity) {
        repository.removeNutrients(eatenFoodsEntity)
    }

    fun addWater(waterEntity: WaterEntity) {
        waterRepository.addWater(waterEntity)
    }

}