package ru.korostylev.easycalories.interfaces

import ru.korostylev.easycalories.dto.FoodItem

interface APIListener {
    fun remove(foodId: Int)
}