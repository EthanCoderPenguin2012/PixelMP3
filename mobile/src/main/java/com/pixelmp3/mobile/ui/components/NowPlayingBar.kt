package com.pixelmp3.mobile.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.pixelmp3.mobile.ui.animations.AnimationSpec
import com.pixelmp3.mobile.ui.animations.SpinningIcon
import com.pixelmp3.shared.model.AudioFile
import com.pixelmp3.shared.model.PlaybackState

/**
 * Expressive now playing bar with animations and gradients
 */
@Composable
fun NowPlayingBar(
    currentTrack: AudioFile?,
    playbackState: PlaybackState,
    currentPosition: Long,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(currentTrack) {
        isVisible = currentTrack != null
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(400)),
        exit = shrinkVertically(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(400)),
        modifier = modifier
    ) {
        currentTrack?.let { track ->
            NowPlayingBarContent(
                track = track,
                playbackState = playbackState,
                currentPosition = currentPosition,
                onPlayPauseClick = onPlayPauseClick,
                onNextClick = onNextClick,
                onPreviousClick = onPreviousClick,
                onExpandClick = onExpandClick
            )
        }
    }
}

@Composable
private fun NowPlayingBarContent(
    track: AudioFile,
    playbackState: PlaybackState,
    currentPosition: Long,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onExpandClick: () -> Unit
) {
    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "now_playing_gradient")
    
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )
    
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.8f)
    )
    
    val gradient = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(gradientOffset, gradientOffset),
        end = Offset(gradientOffset + 800f, gradientOffset + 800f)
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onExpandClick
            )
    ) {
        // Progress bar
        if (track.duration > 0) {
            val progress = (currentPosition.toFloat() / track.duration.toFloat()).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Track info with animation
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Animated album art placeholder
                val isPlaying = playbackState == PlaybackState.PLAYING
                
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primary,
                    tonalElevation = 4.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (isPlaying) {
                            SpinningIcon(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Album,
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                },
                                isSpinning = true
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Album,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
                
                // Track details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = track.artist,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Playback controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous button
                BouncyIconButton(
                    onClick = onPreviousClick,
                    imageVector = Icons.Filled.SkipPrevious
                )
                
                // Play/Pause button
                BouncyIconButton(
                    onClick = onPlayPauseClick,
                    imageVector = if (playbackState == PlaybackState.PLAYING) {
                        Icons.Filled.Pause
                    } else {
                        Icons.Filled.PlayArrow
                    },
                    size = 48.dp,
                    iconSize = 28.dp,
                    isPrimary = true
                )
                
                // Next button
                BouncyIconButton(
                    onClick = onNextClick,
                    imageVector = Icons.Filled.SkipNext
                )
            }
        }
    }
}

@Composable
private fun BouncyIconButton(
    onClick: () -> Unit,
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 40.dp,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp,
    isPrimary: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "icon_button_scale"
    )
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(AnimationSpec.PRESS_ANIMATION_RESET_DELAY_MS)
            isPressed = false
        }
    }
    
    Surface(
        modifier = modifier
            .size(size)
            .scale(scale),
        shape = MaterialTheme.shapes.medium,
        color = if (isPrimary) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        onClick = {
            isPressed = true
            onClick()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (isPrimary) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

/**
 * Expanded now playing screen
 */
@Composable
fun ExpandedNowPlaying(
    track: AudioFile,
    playbackState: PlaybackState,
    currentPosition: Long,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onSeek: (Long) -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "expanded_gradient")
    
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )
    
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    )
    
    val gradient = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(gradientOffset, gradientOffset),
        end = Offset(gradientOffset + 1500f, gradientOffset + 1500f)
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Close button
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Large album art
                val isPlaying = playbackState == PlaybackState.PLAYING
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = if (isPlaying) 360f else 0f,
                    animationSpec = if (isPlaying) {
                        infiniteRepeatable(
                            animation = tween(8000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    } else {
                        infiniteRepeatable(
                            animation = tween(0),
                            repeatMode = RepeatMode.Restart
                        )
                    },
                    label = "album_rotation"
                )
                
                Surface(
                    modifier = Modifier
                        .size(280.dp)
                        .rotate(rotation),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 8.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Album,
                            contentDescription = null,
                            modifier = Modifier.size(140.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                // Track info
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = track.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = track.artist,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Seek bar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val progress = if (track.duration > 0) {
                        (currentPosition.toFloat() / track.duration.toFloat()).coerceIn(0f, 1f)
                    } else {
                        0f
                    }
                    
                    Slider(
                        value = progress,
                        onValueChange = { newValue ->
                            onSeek((newValue * track.duration).toLong())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(currentPosition),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatTime(track.duration),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Playback controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onShuffleClick) {
                        Icon(
                            imageVector = Icons.Filled.Shuffle,
                            contentDescription = "Shuffle",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    BouncyIconButton(
                        onClick = onPreviousClick,
                        imageVector = Icons.Filled.SkipPrevious,
                        size = 56.dp,
                        iconSize = 32.dp
                    )
                    
                    BouncyIconButton(
                        onClick = onPlayPauseClick,
                        imageVector = if (playbackState == PlaybackState.PLAYING) {
                            Icons.Filled.Pause
                        } else {
                            Icons.Filled.PlayArrow
                        },
                        size = 72.dp,
                        iconSize = 40.dp,
                        isPrimary = true
                    )
                    
                    BouncyIconButton(
                        onClick = onNextClick,
                        imageVector = Icons.Filled.SkipNext,
                        size = 56.dp,
                        iconSize = 32.dp
                    )
                    
                    IconButton(onClick = onRepeatClick) {
                        Icon(
                            imageVector = Icons.Filled.Repeat,
                            contentDescription = "Repeat",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}
