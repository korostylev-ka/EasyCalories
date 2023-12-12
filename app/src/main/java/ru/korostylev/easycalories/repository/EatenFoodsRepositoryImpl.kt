package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dao.EatenFoodsDao
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem

class EatenFoodsRepositoryImpl(val eatenFoodsDao: EatenFoodsDao) : EatenFoodsRepository {
    override fun getFoodList(): LiveData<List<EatenFoods>> {
        return eatenFoodsDao.getAll()
    }

    override fun getEatenFoodsForDay(dayId: Int): LiveData<List<EatenFoods>> {
        return eatenFoodsDao.getEatenFoodsForDay(dayId)
    }

    override fun getFoodItem(name: String): EatenFoods {
        return eatenFoodsDao.getFoodItem(name)
    }

    override fun getFoodItemById(id: Int): EatenFoods {
        return eatenFoodsDao.getFoodItemById(id)
    }

    override fun addEatenFoodItem(eatenFoods: EatenFoods) {
        eatenFoodsDao.insert(eatenFoods)
    }

    override fun deleteEatenFoodItem(id: Int) {
        eatenFoodsDao.delete(id)
    }

    init {
        //addFoodItem(EatenFoods(0, 25112023, 54039, "name", 120.0,2.0, 3.0, 4.0, 5.0))
    }

}