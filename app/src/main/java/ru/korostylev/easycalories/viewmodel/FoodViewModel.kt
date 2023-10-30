package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.repository.FoodRepository
import ru.korostylev.easycalories.repository.FoodRepositoryImpl

class FoodViewModel(application: Application): AndroidViewModel(application) {
    val repository: FoodRepository = FoodRepositoryImpl(FoodDB.getInstance(application).foodDao)
    var foodListLiveData = repository.getFoodList()

    fun getFoodItem(name: String): FoodItem {
        return repository.getFoodItem(name)
    }


    fun addItem(foodItem: FoodItem) {
        repository.addItem(foodItem)
    }

    fun deleteItem(name: String) {
        repository.deleteItem(name)
    }

    init {
        repository.getFoodList()
    }
}