package ru.korostylev.easycalories.interfaces

import ru.korostylev.easycalories.entity.FoodItemEntity

interface FoodEntityListener {
    fun getFoodItem(id: Int): FoodItemEntity
    fun delete(id: Int)
}