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
//    @Query("SELECT * FROM FoodItemEntity")
//    suspend fun getAll(): LiveData<List<FoodItemEntity>>
    @Query("SELECT * FROM FoodItemEntity")
    suspend fun getAll(): List<FoodItemEntity>?

    @Query("SELECT * FROM FoodItemEntity WHERE name = (:foodName)")
    suspend fun getFoodItem(foodName: String): FoodItemEntity?

    @Query("SELECT * FROM FoodItemEntity WHERE id = (:id)")
    suspend fun getFoodItemById(id: Int): FoodItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodItemEntity: FoodItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foods: List<FoodItemEntity>)

    @Update
    suspend fun update(foodItemEntity: FoodItemEntity)

    @Query("DELETE FROM FoodItemEntity WHERE foodId = :id")
    suspend fun delete(id: Int)
}