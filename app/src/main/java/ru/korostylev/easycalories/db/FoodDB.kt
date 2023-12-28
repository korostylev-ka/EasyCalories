package ru.korostylev.easycalories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.korostylev.easycalories.dao.FoodDao
import ru.korostylev.easycalories.entity.FoodItemEntity

@Database(entities = [FoodItemEntity::class], version = 1)
abstract class FoodDB: RoomDatabase() {
    abstract val foodDao: FoodDao

    companion object {
        private var instance: FoodDB? = null

        fun getInstance(context: Context): FoodDB {
            return  instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            FoodDB::class.java,
            "foods.db"
        )
            .allowMainThreadQueries()
            .build()
    }

}