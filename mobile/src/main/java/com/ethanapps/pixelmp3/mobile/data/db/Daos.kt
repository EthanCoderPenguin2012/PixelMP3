package com.ethanapps.pixelmp3.mobile.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for playlists
 */
@Dao
interface PlaylistDao {
    
    @Query("SELECT * FROM playlists ORDER BY updatedAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: String): PlaylistEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    
    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: String)
    
    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY position")
    suspend fun getPlaylistTracks(playlistId: String): List<PlaylistTrackEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(track: PlaylistTrackEntity)
    
    @Delete
    suspend fun deletePlaylistTrack(track: PlaylistTrackEntity)
    
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun deleteAllPlaylistTracks(playlistId: String)
    
    @Query("UPDATE playlist_tracks SET position = :newPosition WHERE playlistId = :playlistId AND audioFileId = :audioFileId")
    suspend fun updateTrackPosition(playlistId: String, audioFileId: String, newPosition: Int)
}

/**
 * DAO for audio effects
 */
@Dao
interface AudioEffectsDao {
    
    @Query("SELECT * FROM audio_effects WHERE id = 1")
    fun getAudioEffects(): Flow<AudioEffectsEntity?>
    
    @Query("SELECT * FROM audio_effects WHERE id = 1")
    suspend fun getAudioEffectsOnce(): AudioEffectsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAudioEffects(effects: AudioEffectsEntity)
}

/**
 * DAO for streaming sources
 */
@Dao
interface StreamingSourceDao {
    
    @Query("SELECT * FROM streaming_sources ORDER BY name")
    fun getAllStreamingSources(): Flow<List<StreamingSourceEntity>>
    
    @Query("SELECT * FROM streaming_sources WHERE id = :sourceId")
    suspend fun getStreamingSourceById(sourceId: String): StreamingSourceEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStreamingSource(source: StreamingSourceEntity)
    
    @Delete
    suspend fun deleteStreamingSource(source: StreamingSourceEntity)
}

/**
 * DAO for lyrics
 */
@Dao
interface LyricsDao {
    
    @Query("SELECT * FROM lyrics WHERE audioFileId = :audioFileId")
    suspend fun getLyrics(audioFileId: String): LyricsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLyrics(lyrics: LyricsEntity)
    
    @Delete
    suspend fun deleteLyrics(lyrics: LyricsEntity)
}

/**
 * DAO for search history
 */
@Dao
interface SearchHistoryDao {
    
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 20")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)
    
    @Query("DELETE FROM search_history WHERE timestamp < :cutoffTime")
    suspend fun deleteOldSearches(cutoffTime: Long)
    
    @Query("DELETE FROM search_history")
    suspend fun clearSearchHistory()
}

/**
 * DAO for user preferences
 */
@Dao
interface UserPreferencesDao {
    
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>
    
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun getUserPreferencesOnce(): UserPreferencesEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(preferences: UserPreferencesEntity)
}
