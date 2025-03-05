package ru.korostylev.easycalories.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
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