package com.pixelmp3.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pixelmp3.mobile.data.AudioRepository
import com.pixelmp3.shared.model.AudioFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing audio files and playback state
 */
class AudioViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = AudioRepository(application)
    
    private val _audioFiles = MutableStateFlow<List<AudioFile>>(emptyList())
    val audioFiles: StateFlow<List<AudioFile>> = _audioFiles.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadAudioFiles()
    }
    
    /**
     * Load audio files from device storage
     */
    fun loadAudioFiles() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val files = repository.getAllAudioFiles()
                _audioFiles.value = files
            } catch (e: Exception) {
                e.printStackTrace()
                _audioFiles.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Refresh audio files
     */
    fun refreshAudioFiles() {
        loadAudioFiles()
    }
}
