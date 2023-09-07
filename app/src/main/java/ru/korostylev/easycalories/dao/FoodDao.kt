package ru.korostylev.easycalories.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.korostylev.easycalories.entity.FoodItem
import ru.korostylev.easycalories.entity.NutrientsEntity

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodItem")
    fun getAll(): LiveData<List<FoodItem>>

    @Query("SELECT * FROM FoodItem WHERE id = (:foodId)")
    fun getFoodItem(foodId: Int): FoodItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(foodItem: FoodItem)
}