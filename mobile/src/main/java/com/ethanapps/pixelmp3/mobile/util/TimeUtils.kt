package com.ethanapps.pixelmp3.mobile.util

/**
 * Utility functions for formatting time durations
 */

/**
 * Formats a duration in milliseconds to a human-readable string (MM:SS)
 * @param durationMs Duration in milliseconds
 * @return Formatted string in the format "M:SS" or "MM:SS"
 */
fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${String.format("%02d", seconds)}"
}

/**
 * Extension function on Long to format duration
 */
fun Long.formatAsDuration(): String = formatDuration(this)
