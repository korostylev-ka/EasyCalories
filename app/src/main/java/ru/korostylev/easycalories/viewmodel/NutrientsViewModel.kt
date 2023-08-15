package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ru.korostylev.easycalories.repository.RepositoryNutrients

class NutrientsViewModel(application: Application): AndroidViewModel(application) {
    val repository = RepositoryNutrients()

    var limitsOfNutrients = repository.limitsOfNutrients
    var actualEatenNutrients = repository.actualEatenNutrients
    var data: List<Pair<Float, Float>> = listOf(limitsOfNutrients[0] to actualEatenNutrients[0], limitsOfNutrients[1] to actualEatenNutrients[1], limitsOfNutrients[2] to actualEatenNutrients[2])
    fun setId(id: Int) {
        repository.dayId = id

    }
    fun printId() {
        println("ID в репозитории ${repository.dayId}")
    }


}