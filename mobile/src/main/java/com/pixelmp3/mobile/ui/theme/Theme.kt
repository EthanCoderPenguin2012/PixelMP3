package com.pixelmp3.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Vibrant Material 3 Expressive color schemes with massively enhanced colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7B5FFF),           // Vibrant purple
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE8DBFF), // Lighter purple container
    onPrimaryContainer = Color(0xFF2A0076),
    secondary = Color(0xFFFF6B9D),        // Vibrant pink
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFD9E5),
    onSecondaryContainer = Color(0xFF3E001E),
    tertiary = Color(0xFF00BFA5),         // Vibrant teal
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFA7F3E4),
    onTertiaryContainer = Color(0xFF003E33),
    error = Color(0xFFFF5449),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE6E0F0),
    onSurfaceVariant = Color(0xFF48454E),
    surfaceContainerHigh = Color(0xFFF5F0FA),  // Light purple tint
    outline = Color(0xFF79747E)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBBA4FF),           // Bright purple
    onPrimary = Color(0xFF3E2890),
    primaryContainer = Color(0xFF5B3FD6),  // Rich purple container
    onPrimaryContainer = Color(0xFFE8DBFF),
    secondary = Color(0xFFFFB1CC),        // Bright pink
    onSecondary = Color(0xFF5E1438),
    secondaryContainer = Color(0xFF7D2B4F),
    onSecondaryContainer = Color(0xFFFFD9E5),
    tertiary = Color(0xFF4FDAC6),         // Bright teal
    onTertiary = Color(0xFF00524A),
    tertiaryContainer = Color(0xFF007368),
    onTertiaryContainer = Color(0xFFA7F3E4),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF48454E),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceContainerHigh = Color(0xFF2A2830),  // Dark purple tint
    outline = Color(0xFF938F99)
)

@Composable
fun PixelMP3Theme(
    darkTheme: Boolean = false, // In a real app, use isSystemInDarkTheme()
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ExpressiveTypography,
        shapes = ExpressiveShapes,
        content = content
    )
}
