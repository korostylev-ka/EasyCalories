package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.api.FirebaseDB
import ru.korostylev.easycalories.db.FoodDB
import ru.korostylev.easycalories.dto.FoodItem
import ru.korostylev.easycalories.entity.FoodItemEntity
import ru.korostylev.easycalories.repository.FoodRepository
import ru.korostylev.easycalories.repository.FoodRepositoryImpl

class FoodViewModel(application: Application): AndroidViewModel(application) {
    private val repository: FoodRepository = FoodRepositoryImpl(FoodDB.getInstance(application).foodDao, FirebaseDB().newInstance())
    val foodListLiveData = repository.getFoodList()
    val foodListLiveDataFirebase = repository.getFoodListFromFirebase()

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
        return repository.saveToFirebase(foodItem)
    }

    fun editToFirebase(foodItem: FoodItem) {
        repository.editToFirebase(foodItem)

    }

    fun deleteFromFirebase(foodItem: FoodItem) {
        repository.deleteFromFirebase(foodItem)
    }

    fun updateFromFirebase(list: List<FoodItem>) {
        repository.updateFromFirebase(list)
    }

    init {
        repository.getFoodList()

    }
}