package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.FoodItem

interface FoodRepository {
    fun getFoodList(): LiveData<List<FoodItem>>
    fun getFoodItem(id: Long): FoodItem
    fun addItem(foodItem: FoodItem)
    fun deleteItem(id: Long)
    fun changeItem(id: Long)
}