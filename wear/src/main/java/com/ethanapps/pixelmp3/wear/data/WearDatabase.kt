package com.ethanapps.pixelmp3.wear.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for WearOS
 */
@Database(
    entities = [
        StreamingSourceEntity::class,
        CachedAudioFileEntity::class,
        UserPreferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WearDatabase : RoomDatabase() {
    
    abstract fun streamingSourceDao(): StreamingSourceDao
    abstract fun cachedAudioFileDao(): CachedAudioFileDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    
    companion object {
        @Volatile
        private var INSTANCE: WearDatabase? = null
        
        fun getDatabase(context: Context): WearDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WearDatabase::class.java,
                    "pixelmp3_wear_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
