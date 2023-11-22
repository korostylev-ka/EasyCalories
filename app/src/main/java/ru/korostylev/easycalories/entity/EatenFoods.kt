package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class EatenFoods(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val daiId: Int,
    val time: Long,
    val name: String,
    val portionWeight: Double,
    val portionProteins: Double,
    val portionFats: Double,
    val portionCarbs: Double,
    val portionCalories: Double
)
