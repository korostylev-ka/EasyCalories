package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WaterEntity(
    @PrimaryKey
    val id: Int,
    val waterVolume: Int
)