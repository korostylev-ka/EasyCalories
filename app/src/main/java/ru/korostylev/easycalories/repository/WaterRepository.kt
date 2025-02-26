package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.entity.WaterEntity

interface WaterRepository {

    fun getDayActualWater(dayId: Int): WaterEntity

    fun addWater(waterEntity: WaterEntity)
}