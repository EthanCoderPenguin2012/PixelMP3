package com.pixelmp3.shared.model

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
    val album: String,
    val duration: Long, // in milliseconds
    val filePath: String,
    val albumArtPath: String? = null,
    val dateAdded: Long = System.currentTimeMillis()
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
    val updatedAt: Long = System.currentTimeMillis()
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
    val repeatMode: RepeatMode = RepeatMode.OFF
)

/**
 * Repeat mode options
 */
enum class RepeatMode {
    OFF,
    ONE,
    ALL
}
