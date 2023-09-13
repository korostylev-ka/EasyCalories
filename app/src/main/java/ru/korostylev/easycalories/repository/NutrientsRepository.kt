package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.entity.NutrientsEntity

interface NutrientsRepository {
    var limitsOfNutrientsLiveData: LiveData<NutrientsEntity>
    var actualEatenNutrients: LiveData<NutrientsEntity>

    fun setLimits(limits: NutrientsEntity)
    fun getLimits(): NutrientsEntity?
    fun getTheNutrients(dayId: Int): NutrientsEntity?
}