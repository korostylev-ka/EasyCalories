package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.dao.EatenFoodsDao
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem

class EatenFoodsRepositoryImpl(val eatenFoodsDao: EatenFoodsDao) : EatenFoodsRepository {
    override fun getFoodList(): LiveData<List<EatenFoods>> {
        TODO("Not yet implemented")
    }

    override fun getFoodItem(name: String): FoodItem {
        TODO("Not yet implemented")
    }

}