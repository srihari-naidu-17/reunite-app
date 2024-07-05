package com.example.reuniteapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.reuniteapp.models.UserProfile
import com.example.reuniteapp.models.Items

@Database(entities = [UserProfile::class, Items::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun itemsDao(): ItemsDao

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
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_profiles ADD COLUMN profilePic TEXT DEFAULT '' NOT NULL")
                db.execSQL("CREATE TABLE IF NOT EXISTS `items` (`itemId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `foundBy` TEXT NOT NULL, `found` INTEGER NOT NULL, `itemImage` TEXT NOT NULL, `itemTitle` TEXT NOT NULL, `location` TEXT NOT NULL, `date` TEXT NOT NULL, `time` TEXT NOT NULL, `itemDescription` TEXT NOT NULL, `itemCategory` TEXT NOT NULL, `userEmail` TEXT NOT NULL, `contactNumber` TEXT NOT NULL, FOREIGN KEY(`foundBy`) REFERENCES `user_profiles`(`username`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            }
        }
    }
}
