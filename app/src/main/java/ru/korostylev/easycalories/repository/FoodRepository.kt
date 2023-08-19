package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.entity.FoodItem

interface FoodRepository {
    fun getFoodList(): List<FoodItem>
    fun getFoodItem(id: Long): FoodItem
    fun addItem()
    fun deleteItem(id: Long)
    fun changeItem(id: Long)
}