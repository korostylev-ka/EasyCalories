package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.db.NutrientsDB
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.repository.RepositoryNutrients

class NutrientsViewModel(application: Application): AndroidViewModel(application) {
    val repository = RepositoryNutrients(NutrientsDB.getInstance(application).nutrientsDao)

    var limitsOfNutrients = repository.limitsOfNutrients
    val liveDataLimits = MutableLiveData(limitsOfNutrients)
    var actualEatenNutrients = repository.actualEatenNutrients
    //val liveDataDraw = MutableLiveData(repository.data)
    //var data: List<Pair<Float, Float>> = mutableListOf(limitsOfNutrients.proteins to actualEatenNutrients.proteins, limitsOfNutrients.fats to actualEatenNutrients.fats, limitsOfNutrients.carbs to actualEatenNutrients.carbs)
    var data = repository.data
    fun setId(id: Int) {
        repository.dayId = id

    }
    fun setLimit(limit: NutrientsEntity) {

        repository.addLimits(limit)
        liveDataLimits.value = limit
        println("Во view model ${liveDataLimits.value}")
    }
    fun printId() {
        println("ID в репозитории ${repository.dayId}")
    }


}