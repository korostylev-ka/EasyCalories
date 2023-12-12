package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.db.EatenFoodsDB
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.repository.EatenFoodsRepository
import ru.korostylev.easycalories.repository.EatenFoodsRepositoryImpl

class EatenFoodsViewModel(application: Application): AndroidViewModel(application) {
    val eatenFoodsRepository: EatenFoodsRepository = EatenFoodsRepositoryImpl(EatenFoodsDB.getInstance(application).eatenFoodsDao)
    val eatenFoodsLiveData = eatenFoodsRepository.getFoodList()


    fun getEatenFoodItemForDay(dayId: Int): LiveData<List<EatenFoods>> {
        return eatenFoodsRepository.getEatenFoodsForDay(dayId)
    }

    fun getEatenFoodItemById(id: Int): EatenFoods {
        return eatenFoodsRepository.getFoodItemById(id)
    }

    fun addEatenFood(eatenFood: EatenFoods) {
        eatenFoodsRepository.addEatenFoodItem(eatenFood)
    }

    fun deleteEatenFood(id: Int) {
        eatenFoodsRepository.deleteEatenFoodItem(id)
    }
}