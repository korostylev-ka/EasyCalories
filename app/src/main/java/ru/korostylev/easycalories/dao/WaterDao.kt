package ru.korostylev.easycalories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.entity.WaterEntity

@Dao
interface WaterDao {

    @Query("SELECT * FROM WaterEntity WHERE id = (:dayId)")
    fun getDayActualWater(dayId: Int): WaterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(waterEntity: WaterEntity)
}