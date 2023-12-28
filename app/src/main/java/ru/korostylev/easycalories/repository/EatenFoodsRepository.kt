package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.EatenFoodsEntity

interface EatenFoodsRepository {
    fun getFoodList(): LiveData<List<EatenFoodsEntity>>
    fun getEatenFoodsForDay(dayId: Int): LiveData<List<EatenFoodsEntity>>
    fun getFoodItem(name: String): EatenFoodsEntity
    fun getFoodItemById(id: Int): EatenFoodsEntity
    fun addEatenFoodItem(eatenFoodsEntity: EatenFoodsEntity)
    fun deleteEatenFoodItem(id: Int)
}