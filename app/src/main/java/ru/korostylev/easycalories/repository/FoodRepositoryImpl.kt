package ru.korostylev.easycalories.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Response
import ru.korostylev.easycalories.R
import ru.korostylev.easycalories.api.API
import ru.korostylev.easycalories.api.APIService
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.api.FoodsApi
import ru.korostylev.easycalories.dao.FoodDao
import ru.korostylev.easycalories.dao.WeightDao
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.dto.FoodItemFromDB
import ru.korostylev.easycalories.dto.InfoModel
import ru.korostylev.easycalories.entity.FoodItemEntity

class FoodRepositoryImpl(val foodDao: FoodDao): FoodRepository {

//    override val liveDataFromDB: LiveData<List<FoodItemEntity>>
//        get() = foodDao.getAll()
    val emptyList: List<FoodItemEntity> = emptyList()

    val infoModel = InfoModel()
    private val data = MutableLiveData(emptyList)
    private val dataInfoModel = MutableLiveData<InfoModel>()
    override val liveDataFromDB: LiveData<List<FoodItemEntity>>
        get()  = data
    override val liveDataInfoModel: LiveData<InfoModel>
        get() = dataInfoModel

    override suspend fun getFoodList() {
        withContext(Dispatchers.IO) {
            val list = async {
                foodDao.getAll()
            }.await()
            data.postValue(list ?: emptyList)
        }
    }

    override suspend fun getFoodListFromAPI() {
        try {
            dataInfoModel.postValue(infoModel.copy(loading = true))
            val response = FoodsApi.service.getAll()
            if (response.isSuccessful) {
                val list = response.body()
                updateFromAPI(list)
            } else {
                dataInfoModel.postValue(infoModel.copy(successResponse = false, responseCode = "${response.code()}"))
            }

            dataInfoModel.postValue(infoModel.copy(loading = false))
        } catch (e: java.net.SocketTimeoutException) {
            dataInfoModel.postValue(infoModel.copy(isError = true))
        }
    }

    override suspend fun getFoodItem(name: String): FoodItemEntity? {
        return foodDao.getFoodItem(name)
    }

    override suspend fun getFoodItemById(foodId: Int): FoodItemEntity? {
        val food = foodDao.getFoodItemById(foodId)
        Log.d("daofood", food.toString())
        return foodDao.getFoodItemById(foodId)
    }

    override suspend fun saveToAPI(foodItemEntity: FoodItemEntity) {
        try {
            val foodForDb = foodItemEntity.toFoodItemFromDB()
            val response = FoodsApi.service.save(foodForDb)
            if (response.isSuccessful) {
                val newFoodEntity = FoodItemEntity.fromFoodItemFromDB(response.body()!!).copy(ownedByMe = true)
                foodDao.insert(newFoodEntity)
                getFoodList()
            } else {
                dataInfoModel.postValue(infoModel.copy(successResponse = false, responseCode = "${response.code()}"))

                return
            }
        } catch (e: Exception) {
            dataInfoModel.postValue(infoModel.copy(isError = true, errorResponse = e.message ?: ""))
        }
    }

    override suspend fun editToAPI(foodId: Int, foodItemEntity: FoodItemEntity): FoodItemEntity {
        try {
            val response = FoodsApi.service.edit(foodId, foodItemEntity.toFoodItemFromDB())
            val body = FoodItemEntity.fromFoodItemFromDB(response.body()!!)

            if (response.isSuccessful) {
                update(foodItemEntity)
                getFoodList()
            } else {
                dataInfoModel.postValue(infoModel.copy(successResponse = false, responseCode = "${response.code()}"))
            }
            return body
        } catch (e: Exception) {
            dataInfoModel.postValue(infoModel.copy(isError = true, errorResponse = e.message ?: ""))
            throw e
        }
    }

    override suspend fun addItem(foodItemEntity: FoodItemEntity) {
        foodDao.insert(foodItemEntity)
    }

    override suspend fun update(foodItemEntity: FoodItemEntity) {
        foodDao.update(foodItemEntity)
    }

    override suspend fun deleteFoodItemById(foodId: Int) {
        foodDao.delete(foodId)
    }

    override fun changeItem(id: Long) {
        TODO("Not yet implemented")
    }

//    override fun updateFromAPI(list: List<FoodItem>?) {
//        list?.map { foodItem->
//            val foodFromEntity = getFoodItem(foodItem.name)
//            val foodFromAPI = FoodItemEntity.fromFoodItem(foodItem)
//            //if doesn't exist, add new food
//            if (foodFromEntity == null) {
//                addItem(foodFromAPI)
//            } else {
//                //if item was changed
//                if (foodFromEntity != foodFromAPI) {
//                    update(foodFromAPI.copy(id = foodFromEntity.id, ownedByMe = foodFromEntity.ownedByMe))
//                }
//            }
//        }
//    }

    override suspend fun updateFromAPI(list: List<FoodItemFromDB>?) {
        list?.map { foodItem->
            val foodFromEntity = getFoodItem(foodItem.name)
            val foodFromAPI = FoodItemEntity.fromFoodItemFromDB(foodItem)
            //if doesn't exist, add new food
            if (foodFromEntity == null) {
                addItem(foodFromAPI)
            } else {
                //if item was changed
                if (foodFromEntity != foodFromAPI) {
                    update(foodFromAPI.copy(id = foodFromEntity.id, ownedByMe = foodFromEntity.ownedByMe))
                }
            }
        }
        data.postValue(foodDao.getAll())
    }

    override suspend fun deleteByIdFromAPI(foodId: Int) {
        try {
            val response = FoodsApi.service.removeFoodById(foodId)
            Log.d("body", "${response.code()}, ${response.isSuccessful}")
            if ((response.isSuccessful) || (response.code() == 404)) {
                foodDao.delete(foodId)
                getFoodList()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun initAPI() {
        withContext(Dispatchers.IO) {
            getFoodListFromAPI()
            getFoodList()
        }
    }

    init {
//        API.init()
    }
}