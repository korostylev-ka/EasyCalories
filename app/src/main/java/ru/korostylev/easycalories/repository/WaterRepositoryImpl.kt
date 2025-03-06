package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.dao.WaterDao
import ru.korostylev.easycalories.entity.WaterEntity

class WaterRepositoryImpl(private val waterDao: WaterDao): WaterRepository {

    private var _waterVolumeLD = MutableLiveData<List<WaterEntity>>()
    val waterVolumeLD: LiveData<List<WaterEntity>>
        get() = _waterVolumeLD

    override fun getAllWaterLD(): LiveData<List<WaterEntity>> {
        return waterDao.getAllWater()
    }

    override fun getDayActualWater(dayId: Int): WaterEntity {
        return waterDao.getDayActualWater(dayId) ?: WaterEntity(dayId, 0)
    }

    override fun getLimit(): WaterEntity {
        return waterDao.getLimit() ?: WaterEntity(WATER_LIMIT_ID, WATER_LIMIT_VOLUME_DEFAULT)
    }

    override fun setLimit(waterVolume: Int) {
        waterDao.insert(WaterEntity(WATER_LIMIT_ID, waterVolume))
    }

    override fun addWater(waterEntity: WaterEntity) {
        val currentVolume = getDayActualWater(waterEntity.id)
        waterDao.insert(waterEntity.copy(waterVolume = currentVolume.waterVolume + waterEntity.waterVolume))
        _waterVolumeLD.value = waterDao.getAllWater().value
    }

    companion object {
        private const val WATER_LIMIT_ID = 0
        private const val WATER_LIMIT_VOLUME_DEFAULT = 1500
    }


}