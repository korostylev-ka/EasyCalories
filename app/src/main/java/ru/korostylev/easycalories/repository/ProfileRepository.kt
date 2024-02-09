package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.entity.WeightEntity

interface ProfileRepository {
    fun getWeight(dateId: Int): Float?
    fun saveWeight(weightEntity: WeightEntity)
}