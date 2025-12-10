package com.ethanapps.pixelmp3.mobile.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ethanapps.pixelmp3.mobile.player.PlaybackManager
import com.ethanapps.pixelmp3.mobile.ui.animations.AnimationSpec
import com.ethanapps.pixelmp3.mobile.ui.animations.SpinningIcon
import com.ethanapps.pixelmp3.mobile.ui.screens.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ethanapps.pixelmp3.mobile.viewmodel.AudioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelMP3App() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        Triple(0, "Library", Icons.Filled.LibraryMusic),
        Triple(1, "Albums", Icons.Filled.Album),
        Triple(2, "Artists", Icons.Filled.Person),
        Triple(3, "Playlists", Icons.AutoMirrored.Filled.PlaylistPlay),
        Triple(4, "Radio", Icons.Filled.Radio)
    )
    
    var showMoreMenu by remember { mutableStateOf(false) }
    
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
                actions = {
                    IconButton(onClick = { selectedTab = 200 }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { showMoreMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    
                    DropdownMenu(
                        expanded = showMoreMenu,
                        onDismissRequest = { showMoreMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Audio Effects") },
                            onClick = {
                                selectedTab = 100
                                showMoreMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.GraphicEq, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                selectedTab = 101
                                showMoreMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Settings, contentDescription = null)
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            if (selectedTab < 10) {
                NavigationBar {
                    tabs.forEach { (index, title, icon) ->
                        NavigationBarItem(
                            icon = {
                                val scale by animateFloatAsState(
                                    targetValue = if (selectedTab == index) 1.2f else 1f,
                                    animationSpec = AnimationSpec.fastBouncySpring,
                                    label = "nav_icon_scale_$index"
                                )
                                
                                Icon(
                                    icon,
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
        }
    ) { paddingValues ->
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
                1 -> AlbumsScreen()
                2 -> ArtistsScreen()
                3 -> PlaylistsScreen()
                4 -> RadioScreen()
                100 -> AudioEffectsScreen()
                101 -> SettingsScreenPlaceholder()
                200 -> SearchScreen()
                else -> MusicLibraryScreen(playbackManager)
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
    
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            audioFiles.isEmpty() -> {
                EmptyLibraryState(modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                // Show audio files list
                Text(
                    text = "${audioFiles.size} songs",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun EmptyLibraryState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Music Files",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Add music to your device to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsScreenPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Language, theme, and app preferences",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
