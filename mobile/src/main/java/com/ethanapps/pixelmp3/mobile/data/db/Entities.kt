package com.ethanapps.pixelmp3.mobile.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.ethanapps.pixelmp3.shared.model.AudioEffects
import com.ethanapps.pixelmp3.shared.model.EqualizerPreset

/**
 * Playlist entity for Room database
 */
@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val coverArtPath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Playlist track entity for Room database (junction table)
 */
@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlistId", "audioFileId", "position"]
)
data class PlaylistTrackEntity(
    val playlistId: String,
    val audioFileId: String,
    val position: Int
)

/**
 * Audio effects settings entity
 */
@Entity(tableName = "audio_effects")
@TypeConverters(EffectsConverters::class)
data class AudioEffectsEntity(
    @PrimaryKey
    val id: Int = 1, // Single row for global settings
    val equalizerEnabled: Boolean = false,
    val equalizerPreset: EqualizerPreset = EqualizerPreset.NORMAL,
    val customBands: String = "", // Comma-separated values
    val bassBoost: Int = 0,
    val bassBoostEnabled: Boolean = false,
    val virtualizerStrength: Int = 0,
    val virtualizerEnabled: Boolean = false
)

/**
 * Streaming source entity
 */
@Entity(tableName = "streaming_sources")
data class StreamingSourceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
    val type: String = "HTTP"
)

/**
 * Lyrics entity
 */
@Entity(tableName = "lyrics")
data class LyricsEntity(
    @PrimaryKey
    val audioFileId: String,
    val text: String,
    val timestampedLines: String? = null, // JSON format
    val isSynchronized: Boolean = false
)

/**
 * Search history entity
 */
@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * User preferences entity
 */
@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val id: Int = 1, // Single row
    val languageCode: String = "en-us",
    val dynamicColorEnabled: Boolean = true,
    val darkThemeEnabled: Boolean = false
)

/**
 * Type converters for Room
 */
class EffectsConverters {
    @TypeConverter
    fun fromEqualizerPreset(value: EqualizerPreset): String = value.name
    
    @TypeConverter
    fun toEqualizerPreset(value: String): EqualizerPreset =
        EqualizerPreset.valueOf(value)
}
