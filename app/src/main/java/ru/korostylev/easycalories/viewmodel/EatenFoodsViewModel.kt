package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.korostylev.easycalories.db.EatenFoodsDB
import ru.korostylev.easycalories.entity.EatenFoodsEntity
import ru.korostylev.easycalories.repository.EatenFoodsRepository
import ru.korostylev.easycalories.repository.EatenFoodsRepositoryImpl

class EatenFoodsViewModel(application: Application): AndroidViewModel(application) {
    private val eatenFoodsRepository: EatenFoodsRepository = EatenFoodsRepositoryImpl(EatenFoodsDB.getInstance(application).eatenFoodsDao)

    fun getEatenFoodItemForDay(dayId: Int): LiveData<List<EatenFoodsEntity>> {
        return eatenFoodsRepository.getEatenFoodsForDay(dayId)
    }

    fun getEatenFoodItemById(id: Int): EatenFoodsEntity {
        return eatenFoodsRepository.getFoodItemById(id)
    }

    fun addEatenFood(eatenFood: EatenFoodsEntity) {
        eatenFoodsRepository.addEatenFoodItem(eatenFood)
    }

    fun deleteEatenFood(id: Int) {
        eatenFoodsRepository.deleteEatenFoodItem(id)
    }
}