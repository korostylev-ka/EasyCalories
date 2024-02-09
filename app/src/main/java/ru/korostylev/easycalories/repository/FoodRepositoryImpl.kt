package ru.korostylev.easycalories.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.korostylev.easycalories.api.API
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.dao.FoodDao
import ru.korostylev.easycalories.dao.WeightDao
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity

class FoodRepositoryImpl(val foodDao: FoodDao, val API: API): FoodRepository {

//    override val liveDataFromDB: LiveData<List<FoodItemEntity>>
//        get() = foodDao.getAll()
    val emptyList: List<FoodItemEntity> = emptyList()
    private val data = MutableLiveData(emptyList)
    override val liveDataFromDB: LiveData<List<FoodItemEntity>>
        get()  = data


//    override suspend fun getFoodList(): LiveData<List<FoodItemEntity>>? {
//        return foodDao.getAll()
//    }

    override suspend fun getFoodList() {        withContext(Dispatchers.IO) {

            val list = async {
                foodDao.getAll()
            }.await()
            data.postValue(list ?: emptyList)
        }
    }


    override fun getFoodListFromAPI(): LiveData<List<FoodItem>> {
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

    override fun saveToAPI(foodItemEntity: FoodItem): String? {
        return API.add(foodItemEntity)
    }

    override fun editToAPI(foodItem: FoodItem) {
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

    override fun updateFromAPI(list: List<FoodItem>?) {
        list?.map { foodItem->
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

    override fun deleteFromAPI(foodItem: FoodItem) {
        API.delete(foodItem)
    }

    override suspend fun initAPI() {
        withContext(Dispatchers.IO) {
            API.init()
        }
    }

    init {
//        API.init()
    }
}