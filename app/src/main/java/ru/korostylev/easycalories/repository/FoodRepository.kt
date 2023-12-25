package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.FoodItem

interface FoodRepository {
    fun getFoodList(): LiveData<List<FoodItem>>
    fun getFoodListFromFirebase(): LiveData<List<FoodItem>>
    fun getFoodItem(name: String): FoodItem?
    fun saveToFirebase(foodItem: FoodItem): String?
    fun editToFirebase(foodItem: FoodItem)
    fun updateFromFirebase(list: List<FoodItem>)
    fun addItem(foodItem: FoodItem)
    fun update(foodItem: FoodItem)
    fun deleteItem(name: String)
    fun changeItem(id: Long)
}