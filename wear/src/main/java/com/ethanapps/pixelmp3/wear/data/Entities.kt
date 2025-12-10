package com.ethanapps.pixelmp3.wear.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Streaming source entity for WearOS
 */
@Entity(tableName = "streaming_sources")
data class StreamingSourceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
    val type: String = "RADIO"
)

/**
 * Cached audio file entity for WearOS (synced from mobile)
 */
@Entity(tableName = "cached_audio_files")
data class CachedAudioFileEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val localPath: String, // Path to cached file on watch
    val albumArtPath: String? = null,
    val syncedAt: Long = System.currentTimeMillis()
)

/**
 * User preferences for WearOS
 */
@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val id: Int = 1,
    val volume: Float = 0.5f,
    val lastPlayedSourceId: String? = null,
    val lastPlayedType: String = "LOCAL" // LOCAL or RADIO
)
