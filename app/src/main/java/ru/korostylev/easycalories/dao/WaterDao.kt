package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.entity.WaterEntity

@Dao
interface WaterDao {

    @Query("SELECT * FROM WaterEntity")
    fun getAllWater(): LiveData<List<WaterEntity>>
    @Query("SELECT * FROM WaterEntity WHERE id = (:dayId)")
    fun getDayActualWater(dayId: Int): WaterEntity?

    @Query("SELECT * FROM WaterEntity WHERE id = 0")
    fun getLimit(): WaterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(waterEntity: WaterEntity)
}