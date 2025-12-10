package com.ethanapps.pixelmp3.shared.communication

/**
 * Constants for Wear Data Layer communication
 */
object WearConstants {
    // Paths
    const val AUDIO_FILE_PATH = "/audio_file"
    const val AUDIO_FILE_REQUEST_PATH = "/audio_file_request"
    const val AUDIO_FILE_LIST_PATH = "/audio_file_list"
    const val PLAYBACK_CONTROL_PATH = "/playback_control"
    const val SYNC_REQUEST_PATH = "/sync_request"
    
    // Capabilities
    const val MOBILE_CAPABILITY = "pixelmp3_mobile"
    const val WEAR_CAPABILITY = "pixelmp3_wear"
    
    // Data keys
    const val KEY_AUDIO_FILE_ID = "audio_file_id"
    const val KEY_AUDIO_FILE_TITLE = "audio_file_title"
    const val KEY_AUDIO_FILE_ARTIST = "audio_file_artist"
    const val KEY_AUDIO_FILE_ALBUM = "audio_file_album"
    const val KEY_AUDIO_FILE_DURATION = "audio_file_duration"
    const val KEY_AUDIO_FILE_DATA = "audio_file_data"
    const val KEY_ACTION = "action"
    const val KEY_POSITION = "position"
    
    // Actions
    const val ACTION_PLAY = "play"
    const val ACTION_PAUSE = "pause"
    const val ACTION_NEXT = "next"
    const val ACTION_PREVIOUS = "previous"
    const val ACTION_SEEK = "seek"
}
