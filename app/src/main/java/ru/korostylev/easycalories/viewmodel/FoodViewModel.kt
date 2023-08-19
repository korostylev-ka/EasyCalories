package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.repository.FoodRepository
import ru.korostylev.easycalories.repository.FoodRepositoryImpl

class FoodViewModel(application: Application): AndroidViewModel(application) {
    val repository: FoodRepository = FoodRepositoryImpl()
    val foodList = repository.getFoodList()
}