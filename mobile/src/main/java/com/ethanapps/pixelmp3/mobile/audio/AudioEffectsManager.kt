package com.ethanapps.pixelmp3.mobile.audio

import android.content.Context
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import com.ethanapps.pixelmp3.mobile.data.db.AudioEffectsEntity
import com.ethanapps.pixelmp3.mobile.data.db.PixelMP3Database
import com.ethanapps.pixelmp3.shared.model.AudioEffects
import com.ethanapps.pixelmp3.shared.model.EqualizerPreset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Manager for audio effects (equalizer, bass boost, virtualizer/surround sound)
 */
class AudioEffectsManager(context: Context) {
    
    private val database = PixelMP3Database.getDatabase(context)
    private val effectsDao = database.audioEffectsDao()
    
    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null
    
    private var currentAudioSessionId: Int = 0
    
    /**
     * Get audio effects as Flow
     */
    fun getAudioEffects(): Flow<AudioEffects?> {
        return effectsDao.getAudioEffects().map { entity ->
            entity?.toAudioEffects()
        }
    }
    
    /**
     * Initialize effects for an audio session
     */
    fun initializeEffects(audioSessionId: Int) {
        releaseEffects()
        
        currentAudioSessionId = audioSessionId
        
        try {
            equalizer = Equalizer(0, audioSessionId)
            bassBoost = BassBoost(0, audioSessionId)
            virtualizer = Virtualizer(0, audioSessionId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Apply saved effects to current audio session
     */
    suspend fun applyEffects() {
        val effects = effectsDao.getAudioEffectsOnce()?.toAudioEffects() ?: return
        
        // Apply equalizer
        equalizer?.let { eq ->
            eq.enabled = effects.equalizerEnabled
            if (effects.equalizerEnabled) {
                when (effects.equalizerPreset) {
                    EqualizerPreset.CUSTOM -> {
                        // Apply custom bands
                        effects.customBands.forEachIndexed { index, value ->
                            if (index < eq.numberOfBands) {
                                val level = (value * 100).toInt().toShort() // Convert to millibels
                                eq.setBandLevel(index.toShort(), level)
                            }
                        }
                    }
                    else -> {
                        // Apply preset
                        val presetIndex = getPresetIndex(effects.equalizerPreset, eq)
                        if (presetIndex >= 0) {
                            eq.usePreset(presetIndex.toShort())
                        }
                    }
                }
            }
        }
        
        // Apply bass boost
        bassBoost?.let { bb ->
            bb.enabled = effects.bassBoostEnabled
            if (effects.bassBoostEnabled) {
                bb.setStrength(effects.bassBoost.toShort())
            }
        }
        
        // Apply virtualizer (surround sound)
        virtualizer?.let { virt ->
            virt.enabled = effects.virtualizerEnabled
            if (effects.virtualizerEnabled) {
                virt.setStrength(effects.virtualizerStrength.toShort())
            }
        }
    }
    
    /**
     * Save audio effects settings
     */
    suspend fun saveEffects(effects: AudioEffects) {
        val entity = effects.toEntity()
        effectsDao.saveAudioEffects(entity)
        applyEffects()
    }
    
    /**
     * Get equalizer presets available on device
     */
    fun getAvailablePresets(): List<String> {
        val eq = equalizer ?: return emptyList()
        val presets = mutableListOf<String>()
        
        for (i in 0 until eq.numberOfPresets) {
            presets.add(eq.getPresetName(i.toShort()))
        }
        
        return presets
    }
    
    /**
     * Get equalizer band levels
     */
    fun getEqualizerBands(): List<Pair<Int, String>> {
        val eq = equalizer ?: return emptyList()
        val bands = mutableListOf<Pair<Int, String>>()
        
        for (i in 0 until eq.numberOfBands) {
            val frequency = eq.getCenterFreq(i.toShort()) / 1000 // Convert to kHz
            val label = if (frequency < 1000) {
                "${frequency}Hz"
            } else {
                "${frequency / 1000}kHz"
            }
            bands.add(Pair(i, label))
        }
        
        return bands
    }
    
    /**
     * Release audio effects
     */
    fun releaseEffects() {
        equalizer?.release()
        bassBoost?.release()
        virtualizer?.release()
        
        equalizer = null
        bassBoost = null
        virtualizer = null
    }
    
    /**
     * Map EqualizerPreset to device preset index
     */
    private fun getPresetIndex(preset: EqualizerPreset, eq: Equalizer): Int {
        val presetName = when (preset) {
            EqualizerPreset.NORMAL -> "Normal"
            EqualizerPreset.CLASSICAL -> "Classical"
            EqualizerPreset.DANCE -> "Dance"
            EqualizerPreset.FLAT -> "Flat"
            EqualizerPreset.FOLK -> "Folk"
            EqualizerPreset.HEAVY_METAL -> "Heavy Metal"
            EqualizerPreset.HIP_HOP -> "Hip Hop"
            EqualizerPreset.JAZZ -> "Jazz"
            EqualizerPreset.POP -> "Pop"
            EqualizerPreset.ROCK -> "Rock"
            else -> return -1
        }
        
        for (i in 0 until eq.numberOfPresets) {
            if (eq.getPresetName(i.toShort()).equals(presetName, ignoreCase = true)) {
                return i
            }
        }
        
        return -1
    }
}

/**
 * Extension functions for conversion
 */
private fun AudioEffectsEntity.toAudioEffects(): AudioEffects {
    val bands = if (customBands.isNotEmpty()) {
        customBands.split(",").mapNotNull { it.toFloatOrNull() }
    } else {
        emptyList()
    }
    
    return AudioEffects(
        equalizerEnabled = equalizerEnabled,
        equalizerPreset = equalizerPreset,
        customBands = bands,
        bassBoost = bassBoost,
        bassBoostEnabled = bassBoostEnabled,
        virtualizerStrength = virtualizerStrength,
        virtualizerEnabled = virtualizerEnabled
    )
}

private fun AudioEffects.toEntity(): AudioEffectsEntity {
    val bandsString = customBands.joinToString(",")
    
    return AudioEffectsEntity(
        id = 1,
        equalizerEnabled = equalizerEnabled,
        equalizerPreset = equalizerPreset,
        customBands = bandsString,
        bassBoost = bassBoost,
        bassBoostEnabled = bassBoostEnabled,
        virtualizerStrength = virtualizerStrength,
        virtualizerEnabled = virtualizerEnabled
    )
}
