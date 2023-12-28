package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.db.NutrientsDB
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.repository.NutrientsRepository
import ru.korostylev.easycalories.repository.NutrientsRepositoryImpl

class NutrientsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NutrientsRepository = NutrientsRepositoryImpl(NutrientsDB.getInstance(application).nutrientsDao)
    val liveDataNutrients = repository.getAll()

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

}