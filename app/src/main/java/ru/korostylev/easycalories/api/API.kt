package ru.korostylev.easycalories.api

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dto.FoodItem

interface API {
    fun init()
    fun getAll(): LiveData<List<FoodItem>>
    fun edit(foodItem: FoodItem)
    fun delete(foodItem: FoodItem)
    fun add(foodItem: FoodItem): String?
}