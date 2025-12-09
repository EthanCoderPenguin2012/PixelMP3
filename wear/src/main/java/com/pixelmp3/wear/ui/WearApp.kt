package com.pixelmp3.wear.ui

import androidx.compose.foundation.layout.*
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.pixelmp3.shared.model.AudioFile

@Composable
fun WearApp() {
    MaterialTheme {
        Scaffold(
            timeText = {
                TimeText()
            }
        ) {
            MusicListScreen()
        }
    }
}

@Composable
fun MusicListScreen() {
    // Sample data
    val sampleAudioFiles = remember {
        listOf(
            AudioFile(
                id = "1",
                title = "Sample Song 1",
                artist = "Sample Artist",
                album = "Sample Album",
                duration = 180000,
                filePath = "/data/file1.mp3"
            ),
            AudioFile(
                id = "2",
                title = "Sample Song 2",
                artist = "Another Artist",
                album = "Another Album",
                duration = 240000,
                filePath = "/data/file2.mp3"
            )
        )
    }
    
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 32.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ListHeader {
                Text(
                    text = "PixelMP3",
                    textAlign = TextAlign.Center
                )
            }
        }
        
        if (sampleAudioFiles.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No music",
                        style = MaterialTheme.typography.title3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sync from your phone",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(sampleAudioFiles) { audioFile ->
                WearAudioFileItem(audioFile)
            }
        }
        
        item {
            Chip(
                onClick = { /* TODO: Sync from phone */ },
                label = {
                    Text(
                        text = "Sync from Phone",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun WearAudioFileItem(audioFile: AudioFile) {
    TitleCard(
        onClick = { /* TODO: Play audio */ },
        title = {
            Text(
                text = audioFile.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = audioFile.artist,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun NowPlayingScreen(audioFile: AudioFile) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = audioFile.title,
            style = MaterialTheme.typography.title2,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = audioFile.artist,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* TODO: Previous */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.SkipPrevious,
                    contentDescription = "Previous"
                )
            }
            
            Button(
                onClick = { /* TODO: Play/Pause */ },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play"
                )
            }
            
            Button(
                onClick = { /* TODO: Next */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.SkipNext,
                    contentDescription = "Next"
                )
            }
        }
    }
}
