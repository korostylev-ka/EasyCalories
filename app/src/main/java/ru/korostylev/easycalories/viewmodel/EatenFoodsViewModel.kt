package ru.korostylev.easycalories.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.korostylev.easycalories.db.EatenFoodsDB
import ru.korostylev.easycalories.repository.EatenFoodsRepository
import ru.korostylev.easycalories.repository.EatenFoodsRepositoryImpl

class EatenFoodsViewModel(application: Application): AndroidViewModel(application) {
    val eatenFoodsRepository: EatenFoodsRepository = EatenFoodsRepositoryImpl(EatenFoodsDB.getInstance(application).eatenFoodsDao)
}