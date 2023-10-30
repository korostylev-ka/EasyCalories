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

    @Query("SELECT * FROM FoodItem WHERE name = (:foodName)")
    fun getFoodItem(foodName: String): FoodItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(foodItem: FoodItem)

    @Query("DELETE FROM FoodItem WHERE name = :name")
    fun delete(name: String)
}