package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.dao.WaterDao
import ru.korostylev.easycalories.entity.WaterEntity

class WaterRepositoryImpl(private val waterDao: WaterDao): WaterRepository {
    override fun getDayActualWater(dayId: Int): WaterEntity {
        return waterDao.getDayActualWater(dayId)
    }

    override fun addWater(waterEntity: WaterEntity) {
        waterDao.insert(waterEntity)
    }
}