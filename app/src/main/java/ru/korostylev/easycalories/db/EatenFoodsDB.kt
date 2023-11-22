package ru.korostylev.easycalories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.korostylev.easycalories.dao.EatenFoodsDao
import ru.korostylev.easycalories.entity.EatenFoods


@Database(entities = [EatenFoods::class], version = 1)
abstract class EatenFoodsDB: RoomDatabase() {
    abstract val eatenFoodsDao: EatenFoodsDao

    companion object {
        private var instance: EatenFoodsDB? = null

        fun getInstance(context: Context): EatenFoodsDB {
            return  instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            EatenFoodsDB::class.java,
            "eatenFoods.db"
        )
            .allowMainThreadQueries()
            .build()
    }
}