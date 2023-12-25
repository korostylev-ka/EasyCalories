package ru.korostylev.easycalories.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.repository.FoodRepository
import ru.korostylev.easycalories.repository.FoodRepositoryImpl

class FoodViewModel(application: Application): AndroidViewModel(application) {
    private val repository: FoodRepository = FoodRepositoryImpl(FoodDB.getInstance(application).foodDao, FirebaseDB().newInstance())
    val foodListLiveData = repository.getFoodList()
    val foodListLiveDataFirebase = repository.getFoodListFromFirebase()

    fun getFoodItem(name: String): FoodItem? {
        return repository.getFoodItem(name)
    }

    fun addItem(foodItem: FoodItem) {
        repository.addItem(foodItem)
    }

    fun deleteItem(name: String) {
        repository.deleteItem(name)
    }

    fun update(foodItem: FoodItem) {
        repository.update(foodItem)
    }

    fun saveToFirebase(foodItem: FoodItem): String? {
        return repository.saveToFirebase(foodItem)
    }

    fun editToFirebase(foodItem: FoodItem) {

    }

    fun updateFromFirebase(list: List<FoodItem>) {
        repository.updateFromFirebase(list)
    }

    init {
        repository.getFoodList()

    }
}