package ru.korostylev.easycalories.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.korostylev.easycalories.dao.ProfileDao
import ru.korostylev.easycalories.entity.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 1)
abstract class ProfileDB: RoomDatabase() {
    abstract val profileDao: ProfileDao

    companion object {
        private val instance: ProfileDB? = null

        fun getInstance(context: Context): ProfileDB {
            return  ProfileDB.instance ?: ProfileDB.buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            ProfileDB::class.java,
            "profile.db"
        )
            .allowMainThreadQueries()
            .build()

    }
}