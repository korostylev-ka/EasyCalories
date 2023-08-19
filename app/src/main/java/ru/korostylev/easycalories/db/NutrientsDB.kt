package ru.korostylev.easycalories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.korostylev.easycalories.dao.NutrientsDao
import ru.korostylev.easycalories.entity.NutrientsEntity

@Database(entities = [NutrientsEntity::class], version = 1)
abstract class NutrientsDB: RoomDatabase() {
    abstract val nutrientsDao: NutrientsDao

    companion object {
        private var instance: NutrientsDB? = null

        fun getInstance(context: Context): NutrientsDB {
            return  instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            NutrientsDB::class.java,
            "nutrients.db"
        )
            .allowMainThreadQueries()
            .build()
    }
}