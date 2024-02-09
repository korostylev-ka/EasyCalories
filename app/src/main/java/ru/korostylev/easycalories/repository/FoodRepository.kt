package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity

interface FoodRepository {
    val liveDataFromDB: LiveData<List<FoodItemEntity>>
//    fun getFoodList(): LiveData<List<FoodItemEntity>>
//    suspend fun getFoodList(): LiveData<List<FoodItemEntity>?>
    suspend fun getFoodList()
    fun getFoodListFromAPI(): LiveData<List<FoodItem>>
    fun getFoodItem(name: String): FoodItemEntity?
    fun getFoodItemById(foodId: Int): FoodItemEntity?
    fun saveToAPI(foodItem: FoodItem): String?
    fun editToAPI(foodItem: FoodItem)
    fun updateFromAPI(list: List<FoodItem>?)
    fun deleteFromAPI(foodItem: FoodItem)
    fun addItem(foodItemEntity: FoodItemEntity)
    fun update(foodItemEntity: FoodItemEntity)
    fun deleteItem(id: Int)
    fun changeItem(id: Long)
    suspend fun initAPI()
}