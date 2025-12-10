package com.ethanapps.pixelmp3.mobile.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ethanapps.pixelmp3.mobile.data.AudioRepository
import com.ethanapps.pixelmp3.mobile.ui.animations.AnimationSpec
import com.ethanapps.pixelmp3.shared.model.Album
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    onAlbumClick: (Album) -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { AudioRepository(context) }
    val scope = rememberCoroutineScope()
    
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isGridView by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        albums = repository.getAllAlbums()
        isLoading = false
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Albums") },
                actions = {
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = "Toggle view"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                albums.isEmpty() -> {
                    EmptyAlbumsState(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    AnimatedContent(
                        targetState = isGridView,
                        label = "view_mode"
                    ) { gridView ->
                        if (gridView) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(150.dp),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(albums) { index, album ->
                                    var visible by remember { mutableStateOf(false) }
                                    
                                    LaunchedEffect(Unit) {
                                        delay(index * 30L)
                                        visible = true
                                    }
                                    
                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = fadeIn() + scaleIn(initialScale = 0.8f)
                                    ) {
                                        AlbumGridItem(
                                            album = album,
                                            onClick = { onAlbumClick(album) }
                                        )
                                    }
                                }
                            }
                        } else {
                            AlbumListView(
                                albums = albums,
                                onAlbumClick = onAlbumClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumGridItem(
    album: Album,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "album_scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Album art
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
            ) {
                if (album.albumArtPath != null) {
                    AsyncImage(
                        model = album.albumArtPath,
                        contentDescription = "Album art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Album,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                    )
                }
            }
            
            // Album info
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = album.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (album.year != null) {
                    Text(
                        text = album.year.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }
}

@Composable
fun AlbumListView(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit
) {
    // Implementation similar to grid but in list format
    Text("List view - implement similarly to grid")
}

@Composable
fun EmptyAlbumsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Album,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Albums Found",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Add music to see albums",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
