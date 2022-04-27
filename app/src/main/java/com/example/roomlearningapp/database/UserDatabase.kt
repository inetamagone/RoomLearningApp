package com.example.roomlearningapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomlearningapp.HomeFragment
import com.example.roomlearningapp.model.UserModel

@Database(
    entities = [UserModel::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        var INSTANCE: UserDatabase? = null

        fun createDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_datab"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}