package com.ethanapps.pixelmp3.mobile.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ethanapps.pixelmp3.mobile.audio.AudioEffectsManager
import com.ethanapps.pixelmp3.shared.model.AudioEffects
import com.ethanapps.pixelmp3.shared.model.EqualizerPreset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioEffectsScreen() {
    val context = LocalContext.current
    val effectsManager = remember { AudioEffectsManager(context) }
    val scope = rememberCoroutineScope()
    
    var effects by remember { 
        mutableStateOf(
            AudioEffects(
                equalizerEnabled = false,
                equalizerPreset = EqualizerPreset.NORMAL,
                bassBoost = 0,
                bassBoostEnabled = false,
                virtualizerStrength = 0,
                virtualizerEnabled = false
            )
        )
    }
    
    LaunchedEffect(Unit) {
        effectsManager.getAudioEffects().collect { loadedEffects ->
            if (loadedEffects != null) {
                effects = loadedEffects
            }
        }
    }
    
    fun saveEffects() {
        scope.launch {
            effectsManager.saveEffects(effects)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Effects") },
                actions = {
                    TextButton(onClick = {
                        effects = AudioEffects()
                        saveEffects()
                    }) {
                        Text("Reset")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Equalizer Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Equalizer",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Switch(
                            checked = effects.equalizerEnabled,
                            onCheckedChange = {
                                effects = effects.copy(equalizerEnabled = it)
                                saveEffects()
                            }
                        )
                    }
                    
                    if (effects.equalizerEnabled) {
                        // Preset selector
                        Text(
                            text = "Preset",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        var expanded by remember { mutableStateOf(false) }
                        
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = effects.equalizerPreset.name.replace("_", " "),
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                }
                            )
                            
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                EqualizerPreset.entries.forEach { preset ->
                                    DropdownMenuItem(
                                        text = { Text(preset.name.replace("_", " ")) },
                                        onClick = {
                                            effects = effects.copy(equalizerPreset = preset)
                                            saveEffects()
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Bass Boost Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speaker,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Bass Boost",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Switch(
                            checked = effects.bassBoostEnabled,
                            onCheckedChange = {
                                effects = effects.copy(bassBoostEnabled = it)
                                saveEffects()
                            }
                        )
                    }
                    
                    if (effects.bassBoostEnabled) {
                        Text(
                            text = "Strength: ${effects.bassBoost}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Slider(
                            value = effects.bassBoost.toFloat(),
                            onValueChange = {
                                effects = effects.copy(bassBoost = it.toInt())
                            },
                            onValueChangeFinished = {
                                saveEffects()
                            },
                            valueRange = 0f..1000f,
                            steps = 19
                        )
                    }
                }
            }
            
            // Virtualizer (Surround Sound) Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SurroundSound,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Surround Sound",
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = "Virtualizer for headphones",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = effects.virtualizerEnabled,
                            onCheckedChange = {
                                effects = effects.copy(virtualizerEnabled = it)
                                saveEffects()
                            }
                        )
                    }
                    
                    if (effects.virtualizerEnabled) {
                        Text(
                            text = "Strength: ${effects.virtualizerStrength}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Slider(
                            value = effects.virtualizerStrength.toFloat(),
                            onValueChange = {
                                effects = effects.copy(virtualizerStrength = it.toInt())
                            },
                            onValueChangeFinished = {
                                saveEffects()
                            },
                            valueRange = 0f..1000f,
                            steps = 19
                        )
                    }
                }
            }
            
            // Info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Effects are applied globally to all audio playback",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
