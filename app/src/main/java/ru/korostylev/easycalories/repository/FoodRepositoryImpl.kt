package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.dao.FoodDao
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.entity.FoodItem

class FoodRepositoryImpl(val foodDao: FoodDao): FoodRepository {
    private val emptyList = emptyList<FoodItem>()
    override fun getFoodList(): LiveData<List<FoodItem>> {
        return foodDao.getAll()

        //println("В репозитории ${getFoodList().value?.size} элементов")
    }

    override fun getFoodItem(name: String): FoodItem {
        return foodDao.getFoodItem(name)
    }

    override fun addItem(foodItem: FoodItem) {
        foodDao.insert(foodItem)
    }

    override fun deleteItem(name: String) {
        foodDao.delete(name)
    }

    override fun changeItem(id: Long) {
        TODO("Not yet implemented")
    }
}