package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.entity.WaterEntity

interface WaterRepository {

    fun getAllWaterLD(): LiveData<List<WaterEntity>>
    fun getDayActualWater(dayId: Int): WaterEntity

    fun addWater(waterEntity: WaterEntity)
}