package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity

interface FoodRepository {
    fun getFoodList(): LiveData<List<FoodItemEntity>>
    fun getFoodListFromFirebase(): LiveData<List<FoodItem>>
    fun getFoodItem(name: String): FoodItemEntity?
    fun getFoodItemById(foodId: Int): FoodItemEntity?
    fun saveToFirebase(foodItem: FoodItem): String?
    fun editToFirebase(foodItem: FoodItem)
    fun updateFromFirebase(list: List<FoodItem>)
    fun deleteFromFirebase(foodItem: FoodItem)
    fun addItem(foodItemEntity: FoodItemEntity)
    fun update(foodItemEntity: FoodItemEntity)
    fun deleteItem(id: Int)
    fun changeItem(id: Long)
}