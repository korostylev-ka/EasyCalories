package ru.korostylev.easycalories.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.model.PhotoModel
import ru.korostylev.easycalories.repository.FoodRepository
import ru.korostylev.easycalories.repository.FoodRepositoryImpl

class FoodViewModel(application: Application): AndroidViewModel(application) {
    private val repository: FoodRepository = FoodRepositoryImpl(FoodDB.getInstance(application).foodDao)
    val foodListLiveData = repository.liveDataFromDB
    val infoModel = repository.liveDataInfoModel
    var foodItem: FoodItemEntity? = null
    private val noPhoto = PhotoModel(null)
    //медиавложение(изображение)
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun getFoodList() = viewModelScope.launch(Dispatchers.IO) {
        val foodList = launch {
            repository.getFoodListFromAPI()
        }
        repository.getFoodList()
    }


//    fun getFoodItem(name: String): FoodItemEntity?  {
//        return repository.getFoodItem(name)
//    }

    suspend fun getFoodItem(name: String): FoodItemEntity? = viewModelScope.async{
        repository.getFoodItem(name)
    }.await()

//    fun getFoodItemById(foodId: Int): FoodItemEntity? {
//        return repository.getFoodItemById(foodId)
//    }

    suspend fun getFoodItemById(foodId: Int): FoodItemEntity? = viewModelScope.async{
        repository.getFoodItemById(foodId)
    }.await()

//    fun addItem(foodItemEntity: FoodItemEntity) {
//        repository.addItem(foodItemEntity)
//    }

    fun deleteItem(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFoodItemById(id)
    }

    fun update(foodItemEntity: FoodItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(foodItemEntity)
    }

//    fun saveToFirebase(foodItem: FoodItem): String? {
////        return repository.saveToAPI(foodItem)
//    }

    fun editToAPI(foodId: Int, foodItemEntity: FoodItemEntity) = viewModelScope.launch{
        repository.editToAPI(foodId, foodItemEntity)

    }

//    fun deleteFromFirebase(foodItem: FoodItem) {
//        repository.deleteFromAPI(foodItem)
//    }

    fun deleteByIdFromAPI(foodId: Int) = viewModelScope.launch {
        repository.deleteByIdFromAPI(foodId)
    }

    fun saveToApi(foodItemEntity: FoodItemEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveToAPI(foodItemEntity)
    }


    fun updateFromFirebase(list: List<FoodItem>) {
//        repository.updateFromAPI(list)
    }

    fun initAPI() = viewModelScope.launch(Dispatchers.IO) {
        repository.initAPI()
    }

    init {
        getFoodList()
    }
}