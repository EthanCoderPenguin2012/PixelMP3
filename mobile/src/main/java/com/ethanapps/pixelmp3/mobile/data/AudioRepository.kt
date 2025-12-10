package com.ethanapps.pixelmp3.mobile.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import com.ethanapps.pixelmp3.shared.model.Album
import com.ethanapps.pixelmp3.shared.model.Artist
import com.ethanapps.pixelmp3.shared.model.AudioFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Repository for querying audio files, albums, and artists from device storage
 */
class AudioRepository(private val context: Context) {
    
    private val cacheDir = File(context.cacheDir, "album_art")
    
    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }
    
    /**
     * Query all audio files from MediaStore with complete metadata
     */
    suspend fun getAllAudioFiles(): List<AudioFile> = withContext(Dispatchers.IO) {
        val audioFiles = mutableListOf<AudioFile>()
        val contentResolver: ContentResolver = context.contentResolver
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.SIZE
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn).toString()
                val title = cursor.getString(titleColumn) ?: "Unknown"
                val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                val artistId = cursor.getLong(artistIdColumn).toString()
                val album = cursor.getString(albumColumn) ?: "Unknown Album"
                val albumId = cursor.getLong(albumIdColumn).toString()
                val duration = cursor.getLong(durationColumn)
                val filePath = cursor.getString(dataColumn) ?: ""
                val dateAdded = cursor.getLong(dateAddedColumn) * 1000
                val year = cursor.getInt(yearColumn)
                val track = cursor.getInt(trackColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val size = cursor.getLong(sizeColumn)
                
                // Get album art path
                val albumArtPath = getAlbumArtPath(albumId.toLong())
                
                audioFiles.add(
                    AudioFile(
                        id = id,
                        title = title,
                        artist = artist,
                        artistId = artistId,
                        album = album,
                        albumId = albumId,
                        duration = duration,
                        filePath = filePath,
                        albumArtPath = albumArtPath,
                        dateAdded = dateAdded,
                        year = if (year > 0) year else null,
                        trackNumber = if (track > 0) track else null,
                        mimeType = mimeType,
                        size = size
                    )
                )
            }
        }
        
        audioFiles
    }
    
    /**
     * Get all albums with their tracks
     */
    suspend fun getAllAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val albums = mutableListOf<Album>()
        val contentResolver: ContentResolver = context.contentResolver
        
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ARTIST_ID,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR,
            MediaStore.Audio.Albums.LAST_YEAR
        )
        
        val sortOrder = "${MediaStore.Audio.Albums.ALBUM} COLLATE NOCASE ASC"
        
        contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)
            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST_ID)
            val songsColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)
            val firstYearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn).toString()
                val name = cursor.getString(albumColumn) ?: "Unknown Album"
                val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                val artistId = cursor.getLong(artistIdColumn).toString()
                val trackCount = cursor.getInt(songsColumn)
                val year = cursor.getInt(firstYearColumn)
                
                val albumArtPath = getAlbumArtPath(id.toLong())
                
                albums.add(
                    Album(
                        id = id,
                        name = name,
                        artist = artist,
                        artistId = artistId,
                        albumArtPath = albumArtPath,
                        year = if (year > 0) year else null,
                        trackCount = trackCount
                    )
                )
            }
        }
        
        albums
    }
    
    /**
     * Get album by ID with all tracks
     */
    suspend fun getAlbumWithTracks(albumId: String): Album? = withContext(Dispatchers.IO) {
        val albums = getAllAlbums()
        val album = albums.find { it.id == albumId } ?: return@withContext null
        
        val tracks = getAllAudioFiles().filter { it.albumId == albumId }
        album.copy(tracks = tracks)
    }
    
    /**
     * Get all artists
     */
    suspend fun getAllArtists(): List<Artist> = withContext(Dispatchers.IO) {
        val artists = mutableListOf<Artist>()
        val contentResolver: ContentResolver = context.contentResolver
        
        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )
        
        val sortOrder = "${MediaStore.Audio.Artists.ARTIST} COLLATE NOCASE ASC"
        
        contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)
            val albumsColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            val tracksColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn).toString()
                val name = cursor.getString(artistColumn) ?: "Unknown Artist"
                val albumCount = cursor.getInt(albumsColumn)
                val trackCount = cursor.getInt(tracksColumn)
                
                artists.add(
                    Artist(
                        id = id,
                        name = name,
                        albumCount = albumCount,
                        trackCount = trackCount
                    )
                )
            }
        }
        
        artists
    }
    
    /**
     * Get artist by ID with all albums
     */
    suspend fun getArtistWithAlbums(artistId: String): Artist? = withContext(Dispatchers.IO) {
        val artists = getAllArtists()
        val artist = artists.find { it.id == artistId } ?: return@withContext null
        
        val albums = getAllAlbums().filter { it.artistId == artistId }
        artist.copy(albums = albums)
    }
    
    /**
     * Get album art path from MediaStore
     */
    private fun getAlbumArtPath(albumId: Long): String? {
        return try {
            val artworkUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
            )
            
            // Try to load the thumbnail to verify it exists
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(artworkUri, Size(512, 512), null)
                artworkUri.toString()
            } else {
                // For older Android versions, just return the URI
                artworkUri.toString()
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Load album art as Bitmap
     */
    suspend fun loadAlbumArt(albumArtPath: String?): Bitmap? = withContext(Dispatchers.IO) {
        if (albumArtPath == null) return@withContext null
        
        try {
            val uri = Uri.parse(albumArtPath)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Search audio files with fuzzy matching
     */
    suspend fun searchAudioFiles(query: String): List<AudioFile> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        
        val allFiles = getAllAudioFiles()
        val searchQuery = query.lowercase()
        
        allFiles.filter { file ->
            file.title.lowercase().contains(searchQuery) ||
            file.artist.lowercase().contains(searchQuery) ||
            file.album.lowercase().contains(searchQuery)
        }
    }
    
    /**
     * Search albums
     */
    suspend fun searchAlbums(query: String): List<Album> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        
        val allAlbums = getAllAlbums()
        val searchQuery = query.lowercase()
        
        allAlbums.filter { album ->
            album.name.lowercase().contains(searchQuery) ||
            album.artist.lowercase().contains(searchQuery)
        }
    }
    
    /**
     * Search artists
     */
    suspend fun searchArtists(query: String): List<Artist> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        
        val allArtists = getAllArtists()
        val searchQuery = query.lowercase()
        
        allArtists.filter { artist ->
            artist.name.lowercase().contains(searchQuery)
        }
    }
}
