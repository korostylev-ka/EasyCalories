package ru.korostylev.easycalories.dto

data class FoodItemFromDB(
    var id: Int = 0,
    var user: Int = 1,
    var category_id: Int = 0,
    var name: String = "",
    var GI: Int? = null,
    var proteins: Float = 0.0F,
    var fats: Float = 0.0F,
    var carbs: Float = 0.0F,
    var calories: Float = 0.0F,
    var barcode: String? = null,
    var image: String? = null,
    var key: String? = null
)