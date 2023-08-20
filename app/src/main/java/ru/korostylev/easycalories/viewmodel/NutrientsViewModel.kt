package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.db.NutrientsDB
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.repository.NutrientsRepository
import ru.korostylev.easycalories.repository.NutrientsRepositoryImpl

class NutrientsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NutrientsRepository = NutrientsRepositoryImpl(NutrientsDB.getInstance(application).nutrientsDao)
    val emptyNutrients = NutrientsEntity(0, 0F, 0F, 0F, 0F)
    var limitsOfNutrients = repository.limitsOfNutrients
    val liveDataLimits = MutableLiveData(emptyNutrients)
    var actualEatenNutrients = repository.actualEatenNutrients
    //val liveDataDraw = MutableLiveData(repository.data)
    //var data: List<Pair<Float, Float>> = mutableListOf(limitsOfNutrients.proteins to actualEatenNutrients.proteins, limitsOfNutrients.fats to actualEatenNutrients.fats, limitsOfNutrients.carbs to actualEatenNutrients.carbs)
    var data = repository.data
    val liveDataDiagram = MutableLiveData(data)

    fun setId(id: Int) {
        repository.dayId = id

    }
    fun setLimit(limit: NutrientsEntity) {
        repository.setLimits(limit)
        //liveDataLimits.value = emptyNutrients
        //liveDataDiagram.value = data
    }
    fun printId() {
    }


}