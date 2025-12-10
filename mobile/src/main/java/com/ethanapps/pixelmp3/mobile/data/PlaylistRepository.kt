package com.ethanapps.pixelmp3.mobile.data

import android.content.Context
import com.ethanapps.pixelmp3.mobile.data.db.PixelMP3Database
import com.ethanapps.pixelmp3.mobile.data.db.PlaylistEntity
import com.ethanapps.pixelmp3.mobile.data.db.PlaylistTrackEntity
import com.ethanapps.pixelmp3.shared.model.AudioFile
import com.ethanapps.pixelmp3.shared.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Repository for managing playlists
 */
class PlaylistRepository(context: Context) {
    
    private val database = PixelMP3Database.getDatabase(context)
    private val playlistDao = database.playlistDao()
    private val audioRepository = AudioRepository(context)
    
    /**
     * Get all playlists as Flow
     */
    fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                val tracks = getPlaylistTracks(entity.id)
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverArtPath = entity.coverArtPath,
                    audioFiles = tracks,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt
                )
            }
        }
    }
    
    /**
     * Get playlist by ID
     */
    suspend fun getPlaylistById(playlistId: String): Playlist? {
        val entity = playlistDao.getPlaylistById(playlistId) ?: return null
        val tracks = getPlaylistTracks(playlistId)
        return Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            coverArtPath = entity.coverArtPath,
            audioFiles = tracks,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    /**
     * Create a new playlist
     */
    suspend fun createPlaylist(name: String, description: String? = null): Playlist {
        val playlist = PlaylistEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description
        )
        playlistDao.insertPlaylist(playlist)
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            audioFiles = emptyList(),
            createdAt = playlist.createdAt,
            updatedAt = playlist.updatedAt
        )
    }
    
    /**
     * Update playlist
     */
    suspend fun updatePlaylist(playlist: Playlist) {
        val entity = PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverArtPath = playlist.coverArtPath,
            createdAt = playlist.createdAt,
            updatedAt = System.currentTimeMillis()
        )
        playlistDao.updatePlaylist(entity)
    }
    
    /**
     * Delete playlist
     */
    suspend fun deletePlaylist(playlistId: String) {
        playlistDao.deletePlaylistById(playlistId)
        playlistDao.deleteAllPlaylistTracks(playlistId)
    }
    
    /**
     * Add track to playlist
     */
    suspend fun addTrackToPlaylist(playlistId: String, audioFile: AudioFile) {
        val currentTracks = playlistDao.getPlaylistTracks(playlistId)
        val position = currentTracks.size
        
        val track = PlaylistTrackEntity(
            playlistId = playlistId,
            audioFileId = audioFile.id,
            position = position
        )
        playlistDao.insertPlaylistTrack(track)
        
        // Update playlist timestamp
        playlistDao.getPlaylistById(playlistId)?.let { entity ->
            playlistDao.updatePlaylist(entity.copy(updatedAt = System.currentTimeMillis()))
        }
    }
    
    /**
     * Remove track from playlist
     */
    suspend fun removeTrackFromPlaylist(playlistId: String, audioFileId: String) {
        val tracks = playlistDao.getPlaylistTracks(playlistId)
        val trackToRemove = tracks.find { it.audioFileId == audioFileId } ?: return
        
        playlistDao.deletePlaylistTrack(trackToRemove)
        
        // Reorder remaining tracks
        tracks.filter { it.position > trackToRemove.position }.forEach { track ->
            playlistDao.updateTrackPosition(playlistId, track.audioFileId, track.position - 1)
        }
        
        // Update playlist timestamp
        playlistDao.getPlaylistById(playlistId)?.let { entity ->
            playlistDao.updatePlaylist(entity.copy(updatedAt = System.currentTimeMillis()))
        }
    }
    
    /**
     * Get tracks for a playlist
     */
    private suspend fun getPlaylistTracks(playlistId: String): List<AudioFile> {
        val trackEntities = playlistDao.getPlaylistTracks(playlistId)
        val allAudioFiles = audioRepository.getAllAudioFiles()
        
        return trackEntities.mapNotNull { trackEntity ->
            allAudioFiles.find { it.id == trackEntity.audioFileId }
        }
    }
    
    /**
     * Reorder tracks in playlist
     */
    suspend fun reorderPlaylistTracks(playlistId: String, fromPosition: Int, toPosition: Int) {
        val tracks = playlistDao.getPlaylistTracks(playlistId).sortedBy { it.position }
        
        if (fromPosition == toPosition || fromPosition !in tracks.indices || toPosition !in tracks.indices) {
            return
        }
        
        val track = tracks[fromPosition]
        
        if (fromPosition < toPosition) {
            // Moving down
            tracks.subList(fromPosition + 1, toPosition + 1).forEach {
                playlistDao.updateTrackPosition(playlistId, it.audioFileId, it.position - 1)
            }
        } else {
            // Moving up
            tracks.subList(toPosition, fromPosition).forEach {
                playlistDao.updateTrackPosition(playlistId, it.audioFileId, it.position + 1)
            }
        }
        
        playlistDao.updateTrackPosition(playlistId, track.audioFileId, toPosition)
        
        // Update playlist timestamp
        playlistDao.getPlaylistById(playlistId)?.let { entity ->
            playlistDao.updatePlaylist(entity.copy(updatedAt = System.currentTimeMillis()))
        }
    }
}
