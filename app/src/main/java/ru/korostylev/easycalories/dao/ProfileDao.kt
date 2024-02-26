package ru.korostylev.easycalories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import retrofit2.http.GET
import ru.korostylev.easycalories.entity.ProfileEntity
import ru.korostylev.easycalories.entity.WeightEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM ProfileEntity WHERE id = 0")
    fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProfile(profileEntity: ProfileEntity)

}