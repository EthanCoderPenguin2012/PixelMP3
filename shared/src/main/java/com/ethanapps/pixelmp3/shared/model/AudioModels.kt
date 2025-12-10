package com.ethanapps.pixelmp3.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents an audio file in the PixelMP3 app
 */
@Parcelize
data class AudioFile(
    val id: String,
    val title: String,
    val artist: String,
    val artistId: String? = null,
    val album: String,
    val albumId: String? = null,
    val duration: Long, // in milliseconds
    val filePath: String,
    val albumArtPath: String? = null,
    val dateAdded: Long = System.currentTimeMillis(),
    val genre: String? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val lyrics: String? = null,
    val mimeType: String? = null,
    val size: Long = 0L,
    val bitrate: Int? = null,
    val sampleRate: Int? = null
) : Parcelable

/**
 * Represents an album
 */
@Parcelize
data class Album(
    val id: String,
    val name: String,
    val artist: String,
    val artistId: String? = null,
    val albumArtPath: String? = null,
    val year: Int? = null,
    val genre: String? = null,
    val trackCount: Int = 0,
    val tracks: List<AudioFile> = emptyList()
) : Parcelable

/**
 * Represents an artist
 */
@Parcelize
data class Artist(
    val id: String,
    val name: String,
    val albumCount: Int = 0,
    val trackCount: Int = 0,
    val albums: List<Album> = emptyList(),
    val imageUrl: String? = null
) : Parcelable

/**
 * Represents a playlist
 */
@Parcelize
data class Playlist(
    val id: String,
    val name: String,
    val audioFiles: List<AudioFile>,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val description: String? = null,
    val coverArtPath: String? = null
) : Parcelable

/**
 * Represents lyrics with optional timestamps (LRC format)
 */
@Parcelize
data class Lyrics(
    val text: String,
    val timestampedLines: List<LyricLine> = emptyList(),
    val isSynchronized: Boolean = false
) : Parcelable

@Parcelize
data class LyricLine(
    val timestamp: Long, // milliseconds
    val text: String
) : Parcelable

/**
 * Playback state
 */
enum class PlaybackState {
    IDLE,
    PLAYING,
    PAUSED,
    STOPPED,
    BUFFERING,
    ERROR
}

/**
 * Represents current playback information
 */
data class PlaybackInfo(
    val currentAudioFile: AudioFile?,
    val state: PlaybackState,
    val position: Long = 0L,
    val duration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val currentPlaylist: Playlist? = null,
    val currentQueueIndex: Int = -1
)

/**
 * Repeat mode options
 */
enum class RepeatMode {
    OFF,
    ONE,
    ALL
}

/**
 * Audio effect settings
 */
@Parcelize
data class AudioEffects(
    val equalizerEnabled: Boolean = false,
    val equalizerPreset: EqualizerPreset = EqualizerPreset.NORMAL,
    val customBands: List<Float> = emptyList(), // -12dB to +12dB
    val bassBoost: Int = 0, // 0-1000
    val bassBoostEnabled: Boolean = false,
    val virtualizerStrength: Int = 0, // 0-1000 (surround sound)
    val virtualizerEnabled: Boolean = false
) : Parcelable

enum class EqualizerPreset {
    NORMAL,
    CLASSICAL,
    DANCE,
    FLAT,
    FOLK,
    HEAVY_METAL,
    HIP_HOP,
    JAZZ,
    POP,
    ROCK,
    CUSTOM
}

/**
 * Streaming source
 */
@Parcelize
data class StreamingSource(
    val id: String,
    val name: String,
    val url: String,
    val type: StreamType = StreamType.HTTP,
    val metadata: Map<String, String> = emptyMap()
) : Parcelable

enum class StreamType {
    HTTP,
    HTTPS,
    RADIO
}
