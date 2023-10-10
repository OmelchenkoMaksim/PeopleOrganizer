package com.instagirlsorgainzer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GirlEntity::class], version = 1, exportSchema = false)
abstract class GirlDatabase : RoomDatabase() {

    abstract fun girlDao(): GirlDao

    companion object {
        @Volatile
        private var INSTANCE: GirlDatabase? = null

        fun getInstance(context: Context): GirlDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GirlDatabase::class.java,
                    "girl_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
