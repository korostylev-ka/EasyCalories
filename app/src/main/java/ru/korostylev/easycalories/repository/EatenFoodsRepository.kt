package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem

interface EatenFoodsRepository {
    fun getFoodList(): LiveData<List<EatenFoods>>
    fun getFoodItem(name: String): FoodItem
}