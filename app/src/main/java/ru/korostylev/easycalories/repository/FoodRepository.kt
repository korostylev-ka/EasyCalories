package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.dto.FoodItemFromDB
import ru.korostylev.easycalories.dto.InfoModel
import ru.korostylev.easycalories.entity.FoodItemEntity

interface FoodRepository {
    val liveDataFromDB: LiveData<List<FoodItemEntity>>
    val liveDataInfoModel: LiveData<InfoModel>
    suspend fun getFoodList()
    suspend fun getFoodItem(name: String): FoodItemEntity?
    suspend fun getFoodItemById(foodId: Int): FoodItemEntity?
    suspend fun saveToAPI(foodItemEntity: FoodItemEntity)
    suspend fun editToAPI(foodId: Int, foodItemEntity: FoodItemEntity): FoodItemEntity
    suspend fun getFoodListFromAPI()
    suspend fun updateFromAPI(list: List<FoodItemFromDB>?)
    suspend fun deleteByIdFromAPI(foodId: Int)
    suspend fun addItem(foodItemEntity: FoodItemEntity)
    suspend fun update(foodItemEntity: FoodItemEntity)
    suspend fun deleteFoodItemById(foodId: Int)
    fun changeItem(id: Long)
    suspend fun initAPI()
}