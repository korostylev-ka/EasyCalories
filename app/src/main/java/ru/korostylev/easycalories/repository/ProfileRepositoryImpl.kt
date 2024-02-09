package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.dao.WeightDao
import ru.korostylev.easycalories.entity.WeightEntity

class ProfileRepositoryImpl(val weightDao: WeightDao): ProfileRepository {
    override fun getWeight(dateId: Int): Float? {
        try {
            val weight = weightDao.getWeight(dateId)
            return weight?.weight
        } catch (e: Exception) {
            TODO()
        }
    }

    override fun saveWeight(weightEntity: WeightEntity) {
        weightDao.saveWeight(weightEntity)
    }
}