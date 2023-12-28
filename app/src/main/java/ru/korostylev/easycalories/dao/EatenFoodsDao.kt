package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korostylev.easycalories.entity.EatenFoodsEntity

@Dao
interface EatenFoodsDao {
    @Query("SELECT * FROM EatenFoodsEntity ORDER BY time DESC")
    fun getAll(): LiveData<List<EatenFoodsEntity>>

    @Query("SELECT * FROM EatenFoodsEntity WHERE dayId = (:dayId)")
    fun getEatenFoodsForDay(dayId: Int): LiveData<List<EatenFoodsEntity>>

    @Query("SELECT * FROM EatenFoodsEntity WHERE name = (:foodName)")
    fun getFoodItem(foodName: String): EatenFoodsEntity

    @Query("SELECT * FROM EatenFoodsEntity WHERE id = (:id)")
    fun getFoodItemById(id: Int): EatenFoodsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(eatenFoodsEntity: EatenFoodsEntity)

    @Query("DELETE FROM EatenFoodsEntity WHERE id = :id")
    fun delete(id: Int)
}