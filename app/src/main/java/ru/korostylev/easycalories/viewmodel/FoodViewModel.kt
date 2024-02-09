package ru.korostylev.easycalories.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.repository.FoodRepository
import ru.korostylev.easycalories.repository.FoodRepositoryImpl

class FoodViewModel(application: Application): AndroidViewModel(application) {
    private val repository: FoodRepository = FoodRepositoryImpl(FoodDB.getInstance(application).foodDao, FirebaseDB().newInstance())
//    release
//    val foodListLiveData = repository.getFoodList()
    val foodListLiveData = repository.liveDataFromDB
    val foodListLiveDataFirebase = repository.getFoodListFromAPI()

    fun getFoodList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getFoodList()
    }


    fun getFoodItem(name: String): FoodItemEntity? {
        return repository.getFoodItem(name)
    }

    fun getFoodItemById(foodId: Int): FoodItemEntity? {
        return repository.getFoodItemById(foodId)
    }

    fun addItem(foodItemEntity: FoodItemEntity) {
        repository.addItem(foodItemEntity)
    }

    fun deleteItem(id: Int) {
        repository.deleteItem(id)
    }

    fun update(foodItemEntity: FoodItemEntity) {
        repository.update(foodItemEntity)
    }

    fun saveToFirebase(foodItem: FoodItem): String? {
        return repository.saveToAPI(foodItem)
    }

    fun editToFirebase(foodItem: FoodItem) {
        repository.editToAPI(foodItem)

    }

    fun deleteFromFirebase(foodItem: FoodItem) {
        repository.deleteFromAPI(foodItem)
    }

    fun updateFromFirebase(list: List<FoodItem>) {
        repository.updateFromAPI(list)
    }

    fun initAPI() = viewModelScope.launch(Dispatchers.IO) {
        repository.initAPI()
    }

    init {
        initAPI()
        getFoodList()
//        repository.getFoodList()
    }
}