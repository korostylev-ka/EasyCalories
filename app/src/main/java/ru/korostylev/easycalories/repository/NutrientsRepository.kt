package ru.korostylev.easycalories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.korostylev.easycalories.entity.NutrientsEntity

interface NutrientsRepository {
    var dayId: Int
    val emptyNutrients: NutrientsEntity
    //var limitsOfNutrients: NutrientsEntity
    var limitsOfNutrientsLiveData: LiveData<NutrientsEntity>
    var actualEatenNutrients: NutrientsEntity
    var data: List<Pair<Float, Float>>
    var liveDataDiagram: MutableLiveData<List<Pair<Float, Float>>>

    fun setLimits(limits: NutrientsEntity)
    fun getLimits(): NutrientsEntity?
    fun getTheNutrients(dayId: Int): NutrientsEntity?
}