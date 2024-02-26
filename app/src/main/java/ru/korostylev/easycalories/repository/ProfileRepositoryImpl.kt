package ru.korostylev.easycalories.repository

import ru.korostylev.easycalories.dao.ProfileDao
import ru.korostylev.easycalories.dao.WeightDao
import ru.korostylev.easycalories.entity.ProfileEntity
import ru.korostylev.easycalories.entity.WeightEntity

class ProfileRepositoryImpl(val weightDao: WeightDao, val profileDao: ProfileDao): ProfileRepository {
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

    override fun getProfile(): ProfileEntity? {
        return profileDao.getProfile()
    }

    override fun saveProfile(profileEntity: ProfileEntity) {
        profileDao.saveProfile(profileEntity)
    }
}