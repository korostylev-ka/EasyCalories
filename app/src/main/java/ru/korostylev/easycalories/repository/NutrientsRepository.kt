package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.entity.NutrientsEntity

interface NutrientsRepository {
    fun getAll(): LiveData<List<NutrientsEntity>>
    fun setLimits(limits: NutrientsEntity)
    fun getLimits(): NutrientsEntity
    fun getDayActualNutrients(dayId: Int): NutrientsEntity
    fun addNutrients(nutrients: NutrientsEntity)
    fun removeNutrients(eatenFoodsEntity: EatenFoodsEntity)
}