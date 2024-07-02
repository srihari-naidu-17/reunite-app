package com.example.reuniteapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.reuniteapp.models.Items
import com.example.reuniteapp.models.UserProfile

@Database(entities = [UserProfile::class,Items::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reunite_app_database"
                )
                .fallbackToDestructiveMigration() // Add this line to handle migrations
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}