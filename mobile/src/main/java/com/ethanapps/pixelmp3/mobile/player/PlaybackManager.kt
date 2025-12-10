package com.ethanapps.pixelmp3.mobile.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.ethanapps.pixelmp3.mobile.service.AudioPlaybackService
import com.ethanapps.pixelmp3.shared.model.AudioFile
import java.io.File

/**
 * Manager for audio playback operations
 */
class PlaybackManager(private val context: Context) {
    
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null
    
    /**
     * Initialize the media controller
     */
    fun initialize() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, AudioPlaybackService::class.java)
        )
        
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener(
            {
                try {
                    controller = controllerFuture?.get()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            MoreExecutors.directExecutor()
        )
    }
    
    /**
     * Play an audio file
     */
    fun play(audioFile: AudioFile) {
        controller?.let {
            val uri = Uri.fromFile(File(audioFile.filePath))
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setMediaId(audioFile.id)
                .build()
            
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
        }
    }
    
    /**
     * Play a list of audio files starting from a specific index
     */
    fun playList(audioFiles: List<AudioFile>, startIndex: Int = 0) {
        controller?.let {
            val mediaItems = audioFiles.map { audioFile ->
                val uri = Uri.fromFile(File(audioFile.filePath))
                MediaItem.Builder()
                    .setUri(uri)
                    .setMediaId(audioFile.id)
                    .build()
            }
            
            it.setMediaItems(mediaItems, startIndex, 0)
            it.prepare()
            it.play()
        }
    }
    
    /**
     * Pause playback
     */
    fun pause() {
        controller?.pause()
    }
    
    /**
     * Resume playback
     */
    fun resume() {
        controller?.play()
    }
    
    /**
     * Stop playback
     */
    fun stop() {
        controller?.stop()
    }
    
    /**
     * Skip to next track
     */
    fun skipToNext() {
        controller?.seekToNext()
    }
    
    /**
     * Skip to previous track
     */
    fun skipToPrevious() {
        controller?.seekToPrevious()
    }
    
    /**
     * Release resources
     */
    fun release() {
        controller?.release()
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controller = null
        controllerFuture = null
    }
}
