package com.ethanapps.pixelmp3.wear.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for streaming sources on WearOS
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
    
    @Query("DELETE FROM streaming_sources")
    suspend fun deleteAllStreamingSources()
}

/**
 * DAO for cached audio files on WearOS
 */
@Dao
interface CachedAudioFileDao {
    
    @Query("SELECT * FROM cached_audio_files ORDER BY syncedAt DESC")
    fun getAllCachedAudioFiles(): Flow<List<CachedAudioFileEntity>>
    
    @Query("SELECT * FROM cached_audio_files WHERE id = :fileId")
    suspend fun getCachedAudioFileById(fileId: String): CachedAudioFileEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedAudioFile(file: CachedAudioFileEntity)
    
    @Delete
    suspend fun deleteCachedAudioFile(file: CachedAudioFileEntity)
    
    @Query("DELETE FROM cached_audio_files")
    suspend fun deleteAllCachedAudioFiles()
    
    @Query("SELECT COUNT(*) FROM cached_audio_files")
    suspend fun getCachedAudioFileCount(): Int
}

/**
 * DAO for user preferences on WearOS
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
