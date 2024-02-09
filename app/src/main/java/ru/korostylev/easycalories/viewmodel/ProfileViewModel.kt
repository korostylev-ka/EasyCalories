package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.db.WeightDB
import ru.korostylev.easycalories.entity.WeightEntity
import ru.korostylev.easycalories.repository.ProfileRepository
import ru.korostylev.easycalories.repository.ProfileRepositoryImpl

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    val repository: ProfileRepository = ProfileRepositoryImpl(WeightDB.getInstance(application).weightDao)

    fun getWeight(dateId: Int) = repository.getWeight(dateId)

    fun saveWeight(weightEntity: WeightEntity) = repository.saveWeight(weightEntity)
}