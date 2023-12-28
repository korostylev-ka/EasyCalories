package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.korostylev.easycalories.entity.FoodItemEntity

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodItemEntity")
    fun getAll(): LiveData<List<FoodItemEntity>>

    @Query("SELECT * FROM FoodItemEntity WHERE name = (:foodName)")
    fun getFoodItem(foodName: String): FoodItemEntity?

    @Query("SELECT * FROM FoodItemEntity WHERE id = (:id)")
    fun getFoodItemById(id: Int): FoodItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(foodItemEntity: FoodItemEntity)

    @Update
    fun update(foodItemEntity: FoodItemEntity)

    @Query("DELETE FROM FoodItemEntity WHERE id = :id")
    fun delete(id: Int)
}