package ru.korostylev.easycalories.dto

data class FoodItem(
    var foodId: Int = 0,
    var categoryId: Int = 0,
    var name: String = "",
    var glycemicIndex: Int = 0,
    var proteins: Float = 0F,
    var fats: Float = 0F,
    var carbs: Float = 0F,
    var calories: Float = 0F,
    var barcode: String? = null,
    var image: String? = null,
    var key: String? = null
)