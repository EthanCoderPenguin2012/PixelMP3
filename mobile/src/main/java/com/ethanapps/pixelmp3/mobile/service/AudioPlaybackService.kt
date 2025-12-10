package com.ethanapps.pixelmp3.mobile.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Media playback service for mobile app
 */
class AudioPlaybackService : MediaSessionService() {
    
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer
    
    override fun onCreate() {
        super.onCreate()
        
        player = ExoPlayer.Builder(this).build()
        
        mediaSession = MediaSession.Builder(this, player)
            .build()
        
        createNotificationChannel()
    }
    
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows currently playing music"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "pixelmp3_playback"
    }
}
