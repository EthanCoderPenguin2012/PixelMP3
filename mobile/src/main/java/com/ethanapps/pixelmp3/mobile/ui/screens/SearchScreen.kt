package com.ethanapps.pixelmp3.mobile.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethanapps.pixelmp3.mobile.data.AudioRepository
import com.ethanapps.pixelmp3.mobile.ui.animations.AnimationSpec
import com.ethanapps.pixelmp3.shared.model.Album
import com.ethanapps.pixelmp3.shared.model.Artist
import com.ethanapps.pixelmp3.shared.model.AudioFile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    val context = LocalContext.current
    val repository = remember { AudioRepository(context) }
    val scope = rememberCoroutineScope()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Songs", "Albums", "Artists")
    
    var songResults by remember { mutableStateOf<List<AudioFile>>(emptyList()) }
    var albumResults by remember { mutableStateOf<List<Album>>(emptyList()) }
    var artistResults by remember { mutableStateOf<List<Artist>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    
    fun performSearch(query: String) {
        if (query.isEmpty()) {
            songResults = emptyList()
            albumResults = emptyList()
            artistResults = emptyList()
            return
        }
        
        isSearching = true
        scope.launch {
            songResults = repository.searchAudioFiles(query)
            albumResults = repository.searchAlbums(query)
            artistResults = repository.searchArtists(query)
            isSearching = false
        }
    }
    
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Search") }
                )
                
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        performSearch(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search songs, albums, artists...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                performSearch("")
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
                
                // Tab row
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                val count = when (index) {
                                    0 -> songResults.size
                                    1 -> albumResults.size
                                    else -> artistResults.size
                                }
                                Text("$title ($count)")
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (searchQuery.isEmpty()) {
                EmptySearchState(modifier = Modifier.align(Alignment.Center))
            } else {
                when (selectedTab) {
                    0 -> SearchResultsList(
                        items = songResults,
                        emptyMessage = "No songs found"
                    ) { song ->
                        SearchResultItem(
                            title = song.title,
                            subtitle = "${song.artist} • ${song.album}",
                            icon = Icons.Default.MusicNote
                        )
                    }
                    1 -> SearchResultsList(
                        items = albumResults,
                        emptyMessage = "No albums found"
                    ) { album ->
                        SearchResultItem(
                            title = album.name,
                            subtitle = "${album.artist} • ${album.trackCount} tracks",
                            icon = Icons.Default.Album
                        )
                    }
                    2 -> SearchResultsList(
                        items = artistResults,
                        emptyMessage = "No artists found"
                    ) { artist ->
                        SearchResultItem(
                            title = artist.name,
                            subtitle = "${artist.albumCount} albums • ${artist.trackCount} tracks",
                            icon = Icons.Default.Person
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun <T> SearchResultsList(
    items: List<T>,
    emptyMessage: String,
    itemContent: @Composable (T) -> Unit
) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                itemContent(item)
            }
        }
    }
}

@Composable
fun SearchResultItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "result_scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { isPressed = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
fun EmptySearchState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for Music",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Find songs, albums, and artists",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
