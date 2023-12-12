package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.NutrientsEntity

interface NutrientsRepository {
    //var limitsOfNutrientsLiveData: LiveData<NutrientsEntity>
    fun getAll(): LiveData<List<NutrientsEntity>>
    fun setLimits(limits: NutrientsEntity)
    fun getLimits(): NutrientsEntity
    fun getDayActualNutrients(dayId: Int): NutrientsEntity
    fun addNutrients(nutrients: NutrientsEntity)
    fun removeNutrients(eatenFoods: EatenFoods)
}