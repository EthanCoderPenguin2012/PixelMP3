package com.ethanapps.pixelmp3.mobile.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Main Room database for PixelMP3
 */
@Database(
    entities = [
        PlaylistEntity::class,
        PlaylistTrackEntity::class,
        AudioEffectsEntity::class,
        StreamingSourceEntity::class,
        LyricsEntity::class,
        SearchHistoryEntity::class,
        UserPreferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(EffectsConverters::class)
abstract class PixelMP3Database : RoomDatabase() {
    
    abstract fun playlistDao(): PlaylistDao
    abstract fun audioEffectsDao(): AudioEffectsDao
    abstract fun streamingSourceDao(): StreamingSourceDao
    abstract fun lyricsDao(): LyricsDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    
    companion object {
        @Volatile
        private var INSTANCE: PixelMP3Database? = null
        
        fun getDatabase(context: Context): PixelMP3Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PixelMP3Database::class.java,
                    "pixelmp3_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
