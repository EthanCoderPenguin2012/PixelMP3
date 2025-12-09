package com.pixelmp3.mobile.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.pixelmp3.mobile.ui.animations.AnimatedCard
import com.pixelmp3.mobile.ui.animations.AnimationSpec
import com.pixelmp3.mobile.ui.animations.SpinningIcon
import com.pixelmp3.shared.model.AudioFile
import com.pixelmp3.shared.model.PlaybackState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelMP3App() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Library", "Playlists", "Watch")
    
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
                                    1 -> Icons.Filled.PlaylistPlay
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
                0 -> MusicLibraryScreen()
                1 -> PlaylistsScreen()
                2 -> WatchSyncScreen()
            }
        }
    }
}

@Composable
fun MusicLibraryScreen() {
    // Sample data for demonstration
    val sampleAudioFiles = remember {
        listOf(
            AudioFile(
                id = "1",
                title = "Sample Song 1",
                artist = "Sample Artist",
                album = "Sample Album",
                duration = 180000,
                filePath = "/path/to/file1.mp3"
            ),
            AudioFile(
                id = "2",
                title = "Sample Song 2",
                artist = "Another Artist",
                album = "Another Album",
                duration = 240000,
                filePath = "/path/to/file2.mp3"
            ),
            AudioFile(
                id = "3",
                title = "Epic Soundtrack",
                artist = "Composer",
                album = "OST",
                duration = 300000,
                filePath = "/path/to/file3.mp3"
            )
        )
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        if (sampleAudioFiles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No audio files found",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(sampleAudioFiles) { index, audioFile ->
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
                        AudioFileItem(audioFile)
                    }
                }
            }
        }
    }
}

@Composable
fun AudioFileItem(audioFile: AudioFile) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "item_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 2.dp,
        animationSpec = AnimationSpec.bouncyDpSpring(),
        label = "item_elevation"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        onClick = { 
            isPressed = true
            // TODO: Play audio 
        },
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated music icon
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
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
                Text(
                    text = audioFile.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${audioFile.duration / 1000 / 60}:${String.format("%02d", (audioFile.duration / 1000) % 60)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = { /* TODO: More options */ }
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    // Reset press state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

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
                // Animated pulsing icon
                val infiniteTransition = rememberInfiniteTransition(label = "playlist_pulse")
                
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.15f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "icon_pulse"
                )
                
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.PlaylistPlay,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "No playlists yet",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create your first playlist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                var buttonPressed by remember { mutableStateOf(false) }
                val buttonScale by animateFloatAsState(
                    targetValue = if (buttonPressed) 0.92f else 1f,
                    animationSpec = AnimationSpec.fastBouncySpring,
                    label = "button_scale"
                )
                
                FilledTonalButton(
                    onClick = { 
                        buttonPressed = true
                        // TODO: Create playlist
                    },
                    modifier = Modifier.scale(buttonScale)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Playlist")
                }
                
                LaunchedEffect(buttonPressed) {
                    if (buttonPressed) {
                        delay(100)
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
        // Main sync card with animation
        AnimatedVisibility(
            visible = cardsVisible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400)),
            label = "sync_card"
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
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
                            delay(100)
                            syncButtonPressed = false
                        }
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
    var isHovered by remember { mutableStateOf(false) }
    
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 6.dp else 2.dp,
        animationSpec = AnimationSpec.bouncyDpSpring(),
        label = "info_card_elevation"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
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
