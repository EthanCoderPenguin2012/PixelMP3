package com.pixelmp3.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pixelmp3.shared.model.AudioFile
import com.pixelmp3.shared.model.PlaybackState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelMP3App() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Library", "Playlists", "Watch")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PixelMP3") },
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
                            Icon(
                                when (index) {
                                    0 -> Icons.Filled.LibraryMusic
                                    1 -> Icons.Filled.PlaylistPlay
                                    else -> Icons.Filled.Watch
                                },
                                contentDescription = title
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
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleAudioFiles) { audioFile ->
                    AudioFileItem(audioFile)
                }
            }
        }
    }
}

@Composable
fun AudioFileItem(audioFile: AudioFile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { /* TODO: Play audio */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioFile.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = audioFile.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { /* TODO: More options */ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More")
            }
        }
    }
}

@Composable
fun PlaylistsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.PlaylistPlay,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No playlists yet",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create your first playlist",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WatchSyncScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Watch,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Wear OS Sync",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Transfer music to your watch",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Start sync */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Sync, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sync Selected Music")
                }
            }
        }
        
        Text(
            text = "How it works:",
            style = MaterialTheme.typography.titleMedium
        )
        
        InfoCard(
            icon = Icons.Filled.CheckCircle,
            title = "Select Music",
            description = "Choose songs from your library to transfer"
        )
        
        InfoCard(
            icon = Icons.Filled.Watch,
            title = "Transfer to Watch",
            description = "Music is sent to your Wear OS device"
        )
        
        InfoCard(
            icon = Icons.Filled.HeadsetOff,
            title = "Play Offline",
            description = "Listen without your phone"
        )
    }
}

@Composable
fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
