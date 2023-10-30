package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.FoodItem

interface FoodRepository {
    fun getFoodList(): LiveData<List<FoodItem>>
    fun getFoodItem(name: String): FoodItem
    fun addItem(foodItem: FoodItem)
    fun deleteItem(name: String)
    fun changeItem(id: Long)
}