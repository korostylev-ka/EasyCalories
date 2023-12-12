package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EatenFoods(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dayId: Int,
    val time: Long,
//    val time: String,
    val name: String,
    val portionWeight: Float,
    val portionProteins: Float,
    val portionFats: Float,
    val portionCarbs: Float,
    val portionCalories: Float
)
