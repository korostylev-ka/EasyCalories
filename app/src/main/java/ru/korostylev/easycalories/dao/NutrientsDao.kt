package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import ru.korostylev.easycalories.entity.NutrientsEntity

@Dao
interface NutrientsDao {
    @Query("SELECT * FROM NutrientsEntity")
    fun getLimits(): LiveData<NutrientsEntity>

    @Query("SELECT * FROM NutrientsEntity WHERE id = (:id)")
    fun getTheNutrients(id: Int): NutrientsEntity

    @Insert
    fun insert(nutrients: NutrientsEntity)
}