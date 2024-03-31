package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dao.EatenFoodsDao
import ru.korostylev.easycalories.entity.EatenFoodsEntity

class EatenFoodsRepositoryImpl(val eatenFoodsDao: EatenFoodsDao) : EatenFoodsRepository {
    override fun getFoodList(): LiveData<List<EatenFoodsEntity>> {
        return eatenFoodsDao.getAll()
    }

    override fun getEatenFoodsForDay(dayId: Int): LiveData<List<EatenFoodsEntity>> {
        return eatenFoodsDao.getEatenFoodsForDay(dayId)
    }

    override fun getFoodItem(name: String): EatenFoodsEntity {
        return eatenFoodsDao.getFoodItem(name)
    }

    override fun getFoodItemById(id: Int): EatenFoodsEntity {
        return eatenFoodsDao.getFoodItemById(id)
    }

    override fun addEatenFoodItem(eatenFoodsEntity: EatenFoodsEntity) {
        eatenFoodsDao.insert(eatenFoodsEntity)
    }

    override fun deleteEatenFoodItem(id: Int) {
        eatenFoodsDao.delete(id)
    }

}