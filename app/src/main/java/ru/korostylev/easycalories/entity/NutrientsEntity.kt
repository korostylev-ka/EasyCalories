package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NutrientsEntity(
    @PrimaryKey
    val id: Int,
    val proteins: Float,
    val fats: Float,
    val carbs: Float,
    val calories: Float
)