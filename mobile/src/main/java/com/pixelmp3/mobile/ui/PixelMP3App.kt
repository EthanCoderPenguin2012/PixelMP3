package com.pixelmp3.mobile.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixelmp3.mobile.player.PlaybackManager
import com.pixelmp3.mobile.ui.animations.AnimatedCard
import com.pixelmp3.mobile.ui.animations.AnimationSpec
import com.pixelmp3.mobile.ui.animations.MorphingAnimation
import com.pixelmp3.mobile.ui.animations.SpinningIcon
import com.pixelmp3.mobile.ui.components.BouncingDotsLoader
import com.pixelmp3.mobile.ui.components.GradientCard
import com.pixelmp3.mobile.ui.components.GlassmorphicCard
import com.pixelmp3.mobile.ui.components.ShimmerEffect
import com.pixelmp3.mobile.ui.components.BouncingBadge
import com.pixelmp3.mobile.ui.components.ParticleEffect
import com.pixelmp3.mobile.util.formatDuration
import com.pixelmp3.mobile.viewmodel.AudioViewModel
import com.pixelmp3.shared.model.AudioFile
import com.pixelmp3.shared.model.PlaybackState
import kotlinx.coroutines.delay
import com.pixelmp3.mobile.ui.animations.MorphingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelMP3App() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Library", "Playlists", "Watch")
    
    val context = LocalContext.current
    val playbackManager = remember { PlaybackManager(context) }
    
    DisposableEffect(Unit) {
        playbackManager.initialize()
        onDispose {
            playbackManager.release()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Animated music note icon
                        SpinningIcon(
                            icon = {
                                Icon(
                                    Icons.Filled.LibraryMusic,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            isSpinning = false
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PixelMP3")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = {
                            // Animate icon when selected
                            val scale by animateFloatAsState(
                                targetValue = if (selectedTab == index) 1.2f else 1f,
                                animationSpec = AnimationSpec.fastBouncySpring,
                                label = "nav_icon_scale"
                            )
                            
                            Icon(
                                when (index) {
                                    0 -> Icons.Filled.LibraryMusic
                                    1 -> Icons.AutoMirrored.Filled.PlaylistPlay
                                    else -> Icons.Filled.Watch
                                },
                                contentDescription = title,
                                modifier = Modifier.scale(scale)
                            )
                        },
                        label = { Text(title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Animate content transitions
        AnimatedContent(
            targetState = selectedTab,
            modifier = Modifier.padding(paddingValues),
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            label = "screen_transition"
        ) { targetTab ->
            when (targetTab) {
                0 -> MusicLibraryScreen(playbackManager)
                1 -> PlaylistsScreen()
                2 -> WatchSyncScreen()
            }
        }
    }
}

@Composable
fun MusicLibraryScreen(
    playbackManager: PlaybackManager,
    viewModel: AudioViewModel = viewModel()
) {
    val audioFiles by viewModel.audioFiles.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BouncingDotsLoader()
            }
        } else if (audioFiles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    // Pulsing icon with particle effect
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        ParticleEffect(
                            modifier = Modifier.size(120.dp),
                            particleColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            particleCount = 6
                        )
                        
                        val infiniteTransition = rememberInfiniteTransition(label = "empty_pulse")
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "empty_icon_scale"
                        )
                        
                        Surface(
                            modifier = Modifier
                                .size(96.dp)
                                .scale(scale),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            tonalElevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.MusicNote,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "No audio files found",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add some music to your device to get started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(audioFiles) { index, audioFile ->
                    // Stagger animations for each item
                    var visible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        delay(index * 50L) // Stagger by 50ms per item
                        visible = true
                    }
                    
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(300)),
                        label = "item_$index"
                    ) {
                        AudioFileItem(
                            audioFile = audioFile,
                            onPlay = { playbackManager.play(audioFile) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioFileItem(audioFile: AudioFile, onPlay: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "item_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else if (isHovered) 4.dp else 2.dp,
        animationSpec = AnimationSpec.bouncyDpSpring(),
        label = "item_elevation"
    )
    
    // Animated gradient for the icon container
    val infiniteTransition = rememberInfiniteTransition(label = "icon_gradient")
    val iconGradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "icon_gradient_offset"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        onClick = { 
            isPressed = true
            onPlay()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated music icon with gradient
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 2.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer
                                ),
                                center = androidx.compose.ui.geometry.Offset(
                                    x = 28f + iconGradientOffset % 56f,
                                    y = 28f
                                )
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioFile.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = audioFile.artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatDuration(audioFile.duration),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Play indicator
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
    
    // Reset press state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(AnimationSpec.PRESS_ANIMATION_RESET_DELAY_MS)
            isPressed = false
        }
    }
}

// ...

@Composable
fun PlaylistsScreen() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400)),
            label = "playlists_content"
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                // Animated pulsing icon with gradient and particles
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    ParticleEffect(
                        modifier = Modifier.size(160.dp),
                        particleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                        particleCount = 8
                    )
                    
                    MorphingAnimation(
                        modifier = Modifier.size(120.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "No playlists yet",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create your first playlist to organize your music",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                var buttonPressed by remember { mutableStateOf(false) }
                val buttonScale by animateFloatAsState(
                    targetValue = if (buttonPressed) 0.92f else 1f,
                    animationSpec = AnimationSpec.fastBouncySpring,
                    label = "button_scale"
                )
                
                Button(
                    onClick = { 
                        buttonPressed = true
                        // TODO: Create playlist
                    },
                    modifier = Modifier.scale(buttonScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Playlist", style = MaterialTheme.typography.titleSmall)
                }
                
                LaunchedEffect(buttonPressed) {
                    if (buttonPressed) {
                        delay(AnimationSpec.PRESS_ANIMATION_RESET_DELAY_MS)
                        buttonPressed = false
                    }
                }
            }
        }
    }
}

@Composable
fun WatchSyncScreen() {
    var cardsVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(150)
        cardsVisible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main sync card with animation and gradient
        AnimatedVisibility(
            visible = cardsVisible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400)),
            label = "sync_card"
        ) {
            GradientCard(
                modifier = Modifier.fillMaxWidth(),
                colors = listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Spinning watch icon
                    SpinningIcon(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Watch,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        isSpinning = false
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Wear OS Sync",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Transfer music to your watch",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                
                var syncButtonPressed by remember { mutableStateOf(false) }
                val syncButtonScale by animateFloatAsState(
                    targetValue = if (syncButtonPressed) 0.95f else 1f,
                    animationSpec = AnimationSpec.fastBouncySpring,
                    label = "sync_button_scale"
                )
                
                Button(
                    onClick = { 
                        syncButtonPressed = true
                        // TODO: Start sync 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(syncButtonScale),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Filled.Sync, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sync Selected Music")
                }
                
                LaunchedEffect(syncButtonPressed) {
                    if (syncButtonPressed) {
                        delay(AnimationSpec.PRESS_ANIMATION_RESET_DELAY_MS)
                        syncButtonPressed = false
                    }
                }
            }
        }
        
        AnimatedVisibility(
            visible = cardsVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(400, delayMillis = 100, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400, delayMillis = 100)),
            label = "title"
        ) {
            Text(
                text = "How it works:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        // Animated info cards
        val infoItems = listOf(
            Triple(Icons.Filled.CheckCircle, "Select Music", "Choose songs from your library to transfer"),
            Triple(Icons.Filled.Watch, "Transfer to Watch", "Music is sent to your Wear OS device"),
            Triple(Icons.Filled.HeadsetOff, "Play Offline", "Listen without your phone")
        )
        
        infoItems.forEachIndexed { index, (icon, title, description) ->
            var itemVisible by remember { mutableStateOf(false) }
            
            LaunchedEffect(cardsVisible) {
                if (cardsVisible) {
                    delay(200L + index * 100L)
                    itemVisible = true
                }
            }
            
            AnimatedVisibility(
                visible = itemVisible,
                enter = slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(400)),
                label = "info_card_$index"
            ) {
                InfoCard(icon, title, description)
            }
        }
    }
}

@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 4.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.tertiaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
