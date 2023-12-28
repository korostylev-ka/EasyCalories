package ru.korostylev.easycalories.repository

import android.util.Log
import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.api.API
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.dao.FoodDao
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity

class FoodRepositoryImpl(val foodDao: FoodDao, val API: API): FoodRepository {



    override fun getFoodList(): LiveData<List<FoodItemEntity>> {
        return foodDao.getAll()
    }


    override fun getFoodListFromFirebase(): LiveData<List<FoodItem>> {
        return API.getAll()
    }

    override fun getFoodItem(name: String): FoodItemEntity? {
        return foodDao.getFoodItem(name)
    }

    override fun getFoodItemById(foodId: Int): FoodItemEntity? {
        val food = foodDao.getFoodItemById(foodId)
        Log.d("daofood", food.toString())
        return foodDao.getFoodItemById(foodId)
    }

    override fun saveToFirebase(foodItemEntity: FoodItem): String? {
        return API.add(foodItemEntity)
    }

    override fun editToFirebase(foodItem: FoodItem) {
        API.edit(foodItem)
    }

    override fun addItem(foodItemEntity: FoodItemEntity) {
        foodDao.insert(foodItemEntity)
    }

    override fun update(foodItemEntity: FoodItemEntity) {
        foodDao.update(foodItemEntity)
    }

    override fun deleteItem(id: Int) {
        foodDao.delete(id)
    }

    override fun changeItem(id: Long) {
        TODO("Not yet implemented")
    }

    override fun updateFromFirebase(list: List<FoodItem>) {
        list.map { foodItem->
            val foodFromEntity = getFoodItem(foodItem.name)
            val foodFromFirebase = FoodItemEntity.fromFoodItem(foodItem)
            //if doesn't exist, add new food
            if (foodFromEntity == null) {
                addItem(foodFromFirebase)
            } else {
                //if item was changed
                if (foodFromEntity != foodFromFirebase) {
                    update(foodFromFirebase.copy(id = foodFromEntity.id, ownedByMe = foodFromEntity.ownedByMe))
                }
            }
        }


    }

    override fun deleteFromFirebase(foodItem: FoodItem) {
        API.delete(foodItem)
    }

    init {
        API.init()
    }
}