package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem

interface EatenFoodsRepository {
    fun getFoodList(): LiveData<List<EatenFoods>>
    fun getEatenFoodsForDay(dayId: Int): LiveData<List<EatenFoods>>
    fun getFoodItem(name: String): EatenFoods
    fun getFoodItemById(id: Int): EatenFoods
    fun addEatenFoodItem(eatenFoods: EatenFoods)
    fun deleteEatenFoodItem(id: Int)
}