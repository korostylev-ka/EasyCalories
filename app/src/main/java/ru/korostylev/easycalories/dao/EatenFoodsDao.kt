package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korostylev.easycalories.entity.EatenFoods
import ru.korostylev.easycalories.entity.FoodItem

@Dao
interface EatenFoodsDao {
    @Query("SELECT * FROM EatenFoods")
    fun getAll(): LiveData<List<EatenFoods>>

    @Query("SELECT * FROM EatenFoods WHERE dayId = (:dayId)")
    fun getEatenFoodsForDay(dayId: Int): LiveData<List<EatenFoods>>

    @Query("SELECT * FROM EatenFoods WHERE name = (:foodName)")
    fun getFoodItem(foodName: String): EatenFoods

    @Query("SELECT * FROM EatenFoods WHERE id = (:id)")
    fun getFoodItemById(id: Int): EatenFoods

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(eatenFoods: EatenFoods)

    @Query("DELETE FROM EatenFoods WHERE id = :id")
    fun delete(id: Int)
}