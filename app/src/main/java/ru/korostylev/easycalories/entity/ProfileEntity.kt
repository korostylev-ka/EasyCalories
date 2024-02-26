package ru.korostylev.easycalories.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String?,
    val gender: String?,
    val age: Int?,
    val weight: Float?,
    val height: Int?,
    val chest: Float?,
    val waist: Float?,
    val hip: Float?,
    val neck: Float?,
    val bmi: Float?

)