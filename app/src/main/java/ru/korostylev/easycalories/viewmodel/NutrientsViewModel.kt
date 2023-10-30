package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.db.NutrientsDB
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.repository.NutrientsRepository
import ru.korostylev.easycalories.repository.NutrientsRepositoryImpl

class NutrientsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NutrientsRepository = NutrientsRepositoryImpl(NutrientsDB.getInstance(application).nutrientsDao)
    val emptyNutrients = NutrientsEntity(0, 0F, 0F, 0F, 0F)
    val liveDataLimits = repository.limitsOfNutrientsLiveData
    var limitsOfNutrients = repository.getLimits() ?: emptyNutrients
    var actualEatenNutrients = repository.actualEatenNutrients

    fun setId(id: Int) {
        //repository.dayId = id

    }
    fun setLimit(limit: NutrientsEntity) {
        repository.setLimits(limit)
    }

    fun addNutrients(id: Int, nutrients: NutrientsEntity) {
        try {
            val currentNutrients = repository.getTheNutrients(id)
            val changedNutrients = NutrientsEntity(id, currentNutrients!!.proteins + nutrients.proteins, currentNutrients!!.fats + nutrients.fats, currentNutrients!!.carbs + nutrients.carbs, currentNutrients!!.calories + nutrients.calories )
            repository.addNutrients(changedNutrients)
        } catch (e: Exception) {
            repository.addNutrients(nutrients)

        }

    }
    fun printId() {
    }


}