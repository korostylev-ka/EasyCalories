package ru.korostylev.easycalories.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.currentCoroutineContext
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.dao.FoodDao
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.entity.FoodItem

class FoodRepositoryImpl(val foodDao: FoodDao, val firebaseDB: FirebaseDB): FoodRepository {

    override fun getFoodList(): LiveData<List<FoodItem>> {
        return foodDao.getAll()
    }


    override fun getFoodListFromFirebase(): LiveData<List<FoodItem>> {
        return firebaseDB.liveDataFoods
    }

    override fun getFoodItem(name: String): FoodItem? {
        return foodDao.getFoodItem(name)
    }

    override fun saveToFirebase(foodItem: FoodItem): String? {
        return firebaseDB.saveFoodItem(foodItem)
    }

    override fun editToFirebase(foodItem: FoodItem) {
        firebaseDB.editFoodItem(foodItem)
    }

    override fun addItem(foodItem: FoodItem) {
        foodDao.insert(foodItem)
    }

    override fun update(foodItem: FoodItem) {
        foodDao.update(foodItem)
    }

    override fun deleteItem(name: String) {
        foodDao.delete(name)
    }

    override fun changeItem(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateFromFirebase(list: List<FoodItem>) {
        list.map { foodItem->
            val currentFoodItem = getFoodItem(foodItem.name)
            //if doesn't exist, add new food
            if (currentFoodItem == null) {
                addItem(foodItem)
            } else {
                //if item was changed
                if (currentFoodItem != foodItem) {
                    update(foodItem.copy(id = currentFoodItem.id))
                }
            }
        }


    }

    init {
        firebaseDB.readFoodItems()
    }
}