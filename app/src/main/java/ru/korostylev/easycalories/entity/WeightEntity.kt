package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeightEntity(
    @PrimaryKey(autoGenerate = true)
    val dateId: Int,
    val weight: Float
        )
