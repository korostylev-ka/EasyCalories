package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import ru.korostylev.easycalories.entity.NutrientsEntity

@Dao
interface NutrientsDao {
//    @Query("SELECT * FROM NutrientsEntity WHERE id = 0")
//    fun getLimitsLiveData(): LiveData<NutrientsEntity>
    @Query("SELECT * FROM NutrientsEntity")
    fun getAllLiveData(): LiveData<List<NutrientsEntity>>

    @Query("SELECT * FROM NutrientsEntity WHERE id = 0")
    fun getLimits(): NutrientsEntity

//    @Query("SELECT * FROM NutrientsEntity WHERE id = (:dayId)")
//    fun getTheNutrientsLiveData(dayId: Int): LiveData<NutrientsEntity>?

    @Query("SELECT * FROM NutrientsEntity WHERE id = (:dayId)")
    fun getTheNutrients(dayId: Int): NutrientsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nutrients: NutrientsEntity)
}