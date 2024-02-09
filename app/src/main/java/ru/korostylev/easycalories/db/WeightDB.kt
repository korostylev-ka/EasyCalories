package ru.korostylev.easycalories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.korostylev.easycalories.dao.WeightDao
import ru.korostylev.easycalories.entity.WeightEntity

@Database(entities = [WeightEntity::class], version = 1, exportSchema = true)
abstract class WeightDB: RoomDatabase() {
    abstract val weightDao: WeightDao

    companion object {
        private val instance: WeightDB? = null

        fun getInstance(context: Context): WeightDB {
            return  WeightDB.instance ?: WeightDB.buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            WeightDB::class.java,
            "weight.db"
        )
            .allowMainThreadQueries()
            .build()

    }

}