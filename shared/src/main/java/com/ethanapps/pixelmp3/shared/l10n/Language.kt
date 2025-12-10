package com.ethanapps.pixelmp3.shared.l10n

/**
 * Supported languages in PixelMP3
 */
enum class Language(val code: String, val displayName: String, val isRTL: Boolean = false) {
    ENGLISH_US("en-us", "English (United States)", false),
    ENGLISH_UK("en-uk", "English (United Kingdom)", false),
    FRENCH("fr-fr", "Français (France)", false),
    SPANISH("es-es", "Español (España)", false),
    GERMAN("de-de", "Deutsch (Deutschland)", false),
    ITALIAN("it-it", "Italiano (Italia)", false),
    PORTUGUESE("pt-br", "Português (Brasil)", false),
    JAPANESE("ja-jp", "日本語 (日本)", false),
    KOREAN("ko-kr", "한국어 (대한민국)", false),
    CHINESE_SIMPLIFIED("zh-cn", "简体中文 (中国)", false),
    CHINESE_TRADITIONAL("zh-tw", "繁體中文 (台灣)", false),
    ARABIC("ar-sa", "العربية (السعودية)", true),
    HEBREW("he-il", "עברית (ישראל)", true),
    HINDI("hi-in", "हिन्दी (भारत)", false),
    RUSSIAN("ru-ru", "Русский (Россия)", false);

    companion object {
        fun fromCode(code: String): Language? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
        
        fun getDefault(): Language = ENGLISH_US
    }
}

/**
 * Translation keys used throughout the app
 */
object TranslationKeys {
    // App
    const val APP_NAME = "app_name"
    
    // Navigation
    const val NAV_LIBRARY = "nav_library"
    const val NAV_ALBUMS = "nav_albums"
    const val NAV_ARTISTS = "nav_artists"
    const val NAV_PLAYLISTS = "nav_playlists"
    const val NAV_SEARCH = "nav_search"
    const val NAV_SETTINGS = "nav_settings"
    const val NAV_WATCH = "nav_watch"
    
    // Library
    const val LIBRARY_TITLE = "library_title"
    const val LIBRARY_ALL_SONGS = "library_all_songs"
    const val LIBRARY_EMPTY = "library_empty"
    const val LIBRARY_EMPTY_DESC = "library_empty_desc"
    
    // Albums
    const val ALBUMS_TITLE = "albums_title"
    const val ALBUMS_EMPTY = "albums_empty"
    const val ALBUM_TRACKS = "album_tracks"
    
    // Artists
    const val ARTISTS_TITLE = "artists_title"
    const val ARTISTS_EMPTY = "artists_empty"
    const val ARTIST_ALBUMS = "artist_albums"
    const val ARTIST_TRACKS = "artist_tracks"
    
    // Playlists
    const val PLAYLISTS_TITLE = "playlists_title"
    const val PLAYLIST_CREATE = "playlist_create"
    const val PLAYLIST_NEW = "playlist_new"
    const val PLAYLIST_EDIT = "playlist_edit"
    const val PLAYLIST_DELETE = "playlist_delete"
    const val PLAYLIST_NAME = "playlist_name"
    const val PLAYLIST_EMPTY = "playlist_empty"
    const val PLAYLIST_EMPTY_DESC = "playlist_empty_desc"
    
    // Playback
    const val PLAYBACK_PLAY = "playback_play"
    const val PLAYBACK_PAUSE = "playback_pause"
    const val PLAYBACK_NEXT = "playback_next"
    const val PLAYBACK_PREVIOUS = "playback_previous"
    const val PLAYBACK_SHUFFLE = "playback_shuffle"
    const val PLAYBACK_REPEAT = "playback_repeat"
    const val PLAYBACK_NOW_PLAYING = "playback_now_playing"
    
    // Search
    const val SEARCH_TITLE = "search_title"
    const val SEARCH_HINT = "search_hint"
    const val SEARCH_SONGS = "search_songs"
    const val SEARCH_ALBUMS = "search_albums"
    const val SEARCH_ARTISTS = "search_artists"
    const val SEARCH_PLAYLISTS = "search_playlists"
    const val SEARCH_NO_RESULTS = "search_no_results"
    
    // Audio Effects
    const val EFFECTS_TITLE = "effects_title"
    const val EFFECTS_EQUALIZER = "effects_equalizer"
    const val EFFECTS_BASS_BOOST = "effects_bass_boost"
    const val EFFECTS_SURROUND = "effects_surround"
    const val EFFECTS_PRESET = "effects_preset"
    const val EFFECTS_CUSTOM = "effects_custom"
    
    // Lyrics
    const val LYRICS_TITLE = "lyrics_title"
    const val LYRICS_NONE = "lyrics_none"
    const val LYRICS_EDIT = "lyrics_edit"
    const val LYRICS_SAVE = "lyrics_save"
    
    // Streaming
    const val STREAMING_TITLE = "streaming_title"
    const val STREAMING_URL = "streaming_url"
    const val STREAMING_ADD = "streaming_add"
    const val STREAMING_CONNECT = "streaming_connect"
    
    // Wear OS
    const val WEAR_SYNC = "wear_sync"
    const val WEAR_SYNC_DESC = "wear_sync_desc"
    const val WEAR_TRANSFER = "wear_transfer"
    const val WEAR_STANDALONE = "wear_standalone"
    
    // Settings
    const val SETTINGS_TITLE = "settings_title"
    const val SETTINGS_LANGUAGE = "settings_language"
    const val SETTINGS_THEME = "settings_theme"
    const val SETTINGS_DYNAMIC_COLOR = "settings_dynamic_color"
    const val SETTINGS_AUDIO_QUALITY = "settings_audio_quality"
    
    // Common
    const val COMMON_OK = "common_ok"
    const val COMMON_CANCEL = "common_cancel"
    const val COMMON_DELETE = "common_delete"
    const val COMMON_EDIT = "common_edit"
    const val COMMON_SAVE = "common_save"
    const val COMMON_ADD = "common_add"
    const val COMMON_CLOSE = "common_close"
    const val COMMON_LOADING = "common_loading"
    const val COMMON_ERROR = "common_error"
    
    // Metadata
    const val METADATA_TITLE = "metadata_title"
    const val METADATA_ARTIST = "metadata_artist"
    const val METADATA_ALBUM = "metadata_album"
    const val METADATA_YEAR = "metadata_year"
    const val METADATA_GENRE = "metadata_genre"
    const val METADATA_TRACK = "metadata_track"
    
    // Permissions
    const val PERMISSION_REQUIRED = "permission_required"
    const val PERMISSION_AUDIO = "permission_audio"
    const val PERMISSION_GRANT = "permission_grant"
    
    // Unknown
    const val UNKNOWN_ARTIST = "unknown_artist"
    const val UNKNOWN_ALBUM = "unknown_album"
    const val UNKNOWN_GENRE = "unknown_genre"
}
