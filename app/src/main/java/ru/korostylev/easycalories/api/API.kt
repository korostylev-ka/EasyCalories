package ru.korostylev.easycalories.api

import androidx.lifecycle.LiveData
import retrofit2.Response
import retrofit2.http.GET
import ru.korostylev.easycalories.dto.FoodItem

interface API {
    suspend fun init()
    fun getAll(): LiveData<List<FoodItem>>
    fun edit(foodItem: FoodItem)
    fun delete(foodItem: FoodItem)
    fun add(foodItem: FoodItem): String?
}