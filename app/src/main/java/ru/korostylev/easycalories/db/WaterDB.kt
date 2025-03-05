package ru.korostylev.easycalories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.korostylev.easycalories.dao.WaterDao
import ru.korostylev.easycalories.entity.NutrientsEntity
import ru.korostylev.easycalories.entity.WaterEntity

@Database(entities = [WaterEntity::class], version = 1, exportSchema = true)
abstract class WaterDB: RoomDatabase() {

    abstract val waterDao: WaterDao

    companion object {

        private var instance: WaterDB? = null

        fun getInstance(context: Context): WaterDB {
            return  instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            WaterDB::class.java,
            "water.db"
        )
            .allowMainThreadQueries()
            .build()
    }
}