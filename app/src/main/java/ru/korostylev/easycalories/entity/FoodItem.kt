package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val portionWeight: Float,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
    val calories: Float
)