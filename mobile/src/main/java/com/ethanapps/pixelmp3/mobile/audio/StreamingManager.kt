package com.ethanapps.pixelmp3.mobile.audio

import android.content.Context
import android.net.Uri
import com.ethanapps.pixelmp3.mobile.data.db.PixelMP3Database
import com.ethanapps.pixelmp3.mobile.data.db.StreamingSourceEntity
import com.ethanapps.pixelmp3.shared.model.StreamingSource
import com.ethanapps.pixelmp3.shared.model.StreamType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * Manager for network streaming sources (HTTP/HTTPS/Radio)
 */
class StreamingManager(context: Context) {
    
    private val database = PixelMP3Database.getDatabase(context)
    private val streamingDao = database.streamingSourceDao()
    
    /**
     * Get all streaming sources as Flow
     */
    fun getAllStreamingSources(): Flow<List<StreamingSource>> {
        return streamingDao.getAllStreamingSources().map { entities ->
            entities.map { it.toStreamingSource() }
        }
    }
    
    /**
     * Get streaming source by ID
     */
    suspend fun getStreamingSourceById(sourceId: String): StreamingSource? {
        return streamingDao.getStreamingSourceById(sourceId)?.toStreamingSource()
    }
    
    /**
     * Add a new streaming source
     */
    suspend fun addStreamingSource(
        name: String,
        url: String,
        type: StreamType = StreamType.HTTP
    ): StreamingSource {
        val source = StreamingSource(
            id = UUID.randomUUID().toString(),
            name = name,
            url = url,
            type = type
        )
        
        streamingDao.insertStreamingSource(source.toEntity())
        return source
    }
    
    /**
     * Update streaming source
     */
    suspend fun updateStreamingSource(source: StreamingSource) {
        streamingDao.insertStreamingSource(source.toEntity())
    }
    
    /**
     * Delete streaming source
     */
    suspend fun deleteStreamingSource(sourceId: String) {
        val source = streamingDao.getStreamingSourceById(sourceId)
        if (source != null) {
            streamingDao.deleteStreamingSource(source)
        }
    }
    
    /**
     * Validate streaming URL
     */
    fun validateUrl(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            val scheme = uri.scheme?.lowercase()
            scheme == "http" || scheme == "https"
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get predefined radio stations
     */
    fun getPredefinedRadioStations(): List<StreamingSource> {
        return listOf(
            StreamingSource(
                id = "soma_fm_groove_salad",
                name = "SomaFM - Groove Salad",
                url = "http://ice1.somafm.com/groovesalad-128-mp3",
                type = StreamType.RADIO,
                metadata = mapOf(
                    "genre" to "Ambient/Downtempo",
                    "bitrate" to "128"
                )
            ),
            StreamingSource(
                id = "soma_fm_drone_zone",
                name = "SomaFM - Drone Zone",
                url = "http://ice1.somafm.com/dronezone-128-mp3",
                type = StreamType.RADIO,
                metadata = mapOf(
                    "genre" to "Ambient",
                    "bitrate" to "128"
                )
            ),
            StreamingSource(
                id = "soma_fm_def_con",
                name = "SomaFM - DEF CON Radio",
                url = "http://ice1.somafm.com/defcon-128-mp3",
                type = StreamType.RADIO,
                metadata = mapOf(
                    "genre" to "Hacker/Tech",
                    "bitrate" to "128"
                )
            ),
            StreamingSource(
                id = "soma_fm_metal",
                name = "SomaFM - Metal Detector",
                url = "http://ice1.somafm.com/metal-128-mp3",
                type = StreamType.RADIO,
                metadata = mapOf(
                    "genre" to "Metal",
                    "bitrate" to "128"
                )
            ),
            StreamingSource(
                id = "soma_fm_indie_pop",
                name = "SomaFM - Indie Pop Rocks",
                url = "http://ice1.somafm.com/indiepop-128-mp3",
                type = StreamType.RADIO,
                metadata = mapOf(
                    "genre" to "Indie Pop",
                    "bitrate" to "128"
                )
            )
        )
    }
    
    /**
     * Add predefined radio station to saved sources
     */
    suspend fun addPredefinedStation(stationId: String) {
        val station = getPredefinedRadioStations().find { it.id == stationId }
        if (station != null) {
            streamingDao.insertStreamingSource(station.toEntity())
        }
    }
}

/**
 * Extension functions for conversion
 */
private fun StreamingSourceEntity.toStreamingSource(): StreamingSource {
    return StreamingSource(
        id = id,
        name = name,
        url = url,
        type = StreamType.valueOf(type)
    )
}

private fun StreamingSource.toEntity(): StreamingSourceEntity {
    return StreamingSourceEntity(
        id = id,
        name = name,
        url = url,
        type = type.name
    )
}
