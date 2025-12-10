package com.ethanapps.pixelmp3.mobile.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ethanapps.pixelmp3.mobile.audio.StreamingManager
import com.ethanapps.pixelmp3.mobile.ui.animations.AnimationSpec
import com.ethanapps.pixelmp3.shared.model.StreamingSource
import com.ethanapps.pixelmp3.shared.model.StreamType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioScreen() {
    val context = LocalContext.current
    val streamingManager = remember { StreamingManager(context) }
    val scope = rememberCoroutineScope()
    
    var stations by remember { mutableStateOf<List<StreamingSource>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showPredefinedDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        streamingManager.getAllStreamingSources().collect {
            stations = it
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Radio Stations") },
                actions = {
                    IconButton(onClick = { showPredefinedDialog = true }) {
                        Icon(Icons.Default.LibraryAdd, contentDescription = "Browse stations")
                    }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add custom station")
                    }
                }
            )
        },
        floatingActionButton = {
            if (stations.isEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { showPredefinedDialog = true },
                    icon = { Icon(Icons.Default.Radio, contentDescription = null) },
                    text = { Text("Browse Stations") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (stations.isEmpty()) {
                EmptyRadioState(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(stations) { station ->
                        RadioStationItem(
                            station = station,
                            onPlay = {
                                // Play station
                            },
                            onDelete = {
                                scope.launch {
                                    streamingManager.deleteStreamingSource(station.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    
    if (showAddDialog) {
        AddCustomStationDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, url ->
                scope.launch {
                    streamingManager.addStreamingSource(name, url, StreamType.RADIO)
                    showAddDialog = false
                }
            }
        )
    }
    
    if (showPredefinedDialog) {
        PredefinedStationsDialog(
            streamingManager = streamingManager,
            onDismiss = { showPredefinedDialog = false }
        )
    }
}

@Composable
fun RadioStationItem(
    station: StreamingSource,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "station_scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onPlay()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Radio,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = station.metadata["genre"] ?: "Radio Station",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
            
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onDelete()
                        showMenu = false
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
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
fun AddCustomStationDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Station") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Station Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Stream URL") },
                    placeholder = { Text("http://...") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAdd(name, url) },
                enabled = name.isNotBlank() && url.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PredefinedStationsDialog(
    streamingManager: StreamingManager,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val predefinedStations = remember { streamingManager.getPredefinedRadioStations() }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Browse Stations") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(predefinedStations) { station ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    streamingManager.addPredefinedStation(station.id)
                                }
                            },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = station.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = station.metadata["genre"] ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

@Composable
fun EmptyRadioState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Radio,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Radio Stations",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Browse predefined stations or add custom URLs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
