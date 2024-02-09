package ru.korostylev.easycalories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korostylev.easycalories.entity.WeightEntity

@Dao
interface WeightDao {
    @Query("SELECT * FROM WeightEntity WHERE dateId=(:dateId)")
    fun getWeight(dateId: Int): WeightEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeight(weightEntity: WeightEntity)
}