package com.ethanapps.pixelmp3.mobile.audio

import com.ethanapps.pixelmp3.shared.model.LyricLine
import com.ethanapps.pixelmp3.shared.model.Lyrics
import java.io.File
import java.util.regex.Pattern

/**
 * Parser for LRC (synchronized lyrics) format
 */
object LyricsParser {
    
    private val TIME_TAG_PATTERN = Pattern.compile("\\[(\\d+):(\\d+)(?:\\.(\\d+))?\\]")
    
    /**
     * Parse LRC format lyrics from string
     * Format: [mm:ss.xx]Line text
     */
    fun parseLrc(lrcContent: String): Lyrics {
        val lines = lrcContent.split("\n")
        val lyricLines = mutableListOf<LyricLine>()
        val plainTextLines = mutableListOf<String>()
        
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue
            
            val matcher = TIME_TAG_PATTERN.matcher(trimmed)
            
            if (matcher.find()) {
                // Parse timestamp
                val minutes = matcher.group(1)?.toLongOrNull() ?: 0
                val seconds = matcher.group(2)?.toLongOrNull() ?: 0
                val centiseconds = matcher.group(3)?.padEnd(2, '0')?.take(2)?.toLongOrNull() ?: 0
                
                val timestamp = (minutes * 60 * 1000) + (seconds * 1000) + (centiseconds * 10)
                
                // Get text after timestamp
                val text = trimmed.substring(matcher.end()).trim()
                
                if (text.isNotEmpty()) {
                    lyricLines.add(LyricLine(timestamp, text))
                    plainTextLines.add(text)
                }
            } else {
                // Metadata or plain text
                if (!trimmed.startsWith("[")) {
                    plainTextLines.add(trimmed)
                }
            }
        }
        
        // Sort by timestamp
        lyricLines.sortBy { it.timestamp }
        
        return Lyrics(
            text = plainTextLines.joinToString("\n"),
            timestampedLines = lyricLines,
            isSynchronized = lyricLines.isNotEmpty()
        )
    }
    
    /**
     * Parse lyrics from file
     */
    fun parseLrcFile(file: File): Lyrics? {
        return try {
            val content = file.readText(Charsets.UTF_8)
            parseLrc(content)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Generate LRC format from Lyrics
     */
    fun toLrc(lyrics: Lyrics): String {
        if (!lyrics.isSynchronized || lyrics.timestampedLines.isEmpty()) {
            return lyrics.text
        }
        
        return lyrics.timestampedLines.joinToString("\n") { line ->
            val minutes = line.timestamp / 60000
            val seconds = (line.timestamp % 60000) / 1000
            val centiseconds = (line.timestamp % 1000) / 10
            
            String.format("[%02d:%02d.%02d]%s", minutes, seconds, centiseconds, line.text)
        }
    }
    
    /**
     * Get current lyric line for playback position
     */
    fun getCurrentLine(lyrics: Lyrics, positionMs: Long): LyricLine? {
        if (!lyrics.isSynchronized || lyrics.timestampedLines.isEmpty()) {
            return null
        }
        
        var currentLine: LyricLine? = null
        
        for (line in lyrics.timestampedLines) {
            if (line.timestamp <= positionMs) {
                currentLine = line
            } else {
                break
            }
        }
        
        return currentLine
    }
    
    /**
     * Get index of current line for scrolling
     */
    fun getCurrentLineIndex(lyrics: Lyrics, positionMs: Long): Int {
        if (!lyrics.isSynchronized || lyrics.timestampedLines.isEmpty()) {
            return -1
        }
        
        var index = -1
        
        for ((i, line) in lyrics.timestampedLines.withIndex()) {
            if (line.timestamp <= positionMs) {
                index = i
            } else {
                break
            }
        }
        
        return index
    }
    
    /**
     * Search for .lrc file in same directory as audio file
     */
    fun findLrcFile(audioFilePath: String): File? {
        val audioFile = File(audioFilePath)
        if (!audioFile.exists()) return null
        
        val directory = audioFile.parentFile ?: return null
        val baseName = audioFile.nameWithoutExtension
        
        // Try exact match
        val lrcFile = File(directory, "$baseName.lrc")
        if (lrcFile.exists()) return lrcFile
        
        // Try case-insensitive match
        val files = directory.listFiles() ?: return null
        return files.find { 
            it.extension.equals("lrc", ignoreCase = true) &&
            it.nameWithoutExtension.equals(baseName, ignoreCase = true)
        }
    }
}
