package com.ethanapps.pixelmp3.mobile.l10n

import android.content.Context
import com.ethanapps.pixelmp3.shared.l10n.Language
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Loads and manages translations from .lang files in assets
 */
class TranslationManager(private val context: Context) {
    
    private var currentLanguage: Language = Language.getDefault()
    private var translations: Map<String, String> = emptyMap()
    
    init {
        loadLanguage(currentLanguage)
    }
    
    /**
     * Load translations for a specific language
     */
    fun loadLanguage(language: Language) {
        currentLanguage = language
        translations = loadTranslationsFromAssets(language.code)
    }
    
    /**
     * Get translation for a key
     */
    fun getString(key: String, vararg args: Any): String {
        val translation = translations[key] ?: key
        return if (args.isEmpty()) {
            translation
        } else {
            String.format(translation, *args)
        }
    }
    
    /**
     * Get current language
     */
    fun getCurrentLanguage(): Language = currentLanguage
    
    /**
     * Check if RTL layout should be used
     */
    fun isRTL(): Boolean = currentLanguage.isRTL
    
    /**
     * Load translations from assets folder
     */
    private fun loadTranslationsFromAssets(languageCode: String): Map<String, String> {
        val fileName = "$languageCode.lang"
        val translations = mutableMapOf<String, String>()
        
        try {
            context.assets.open("lang/$fileName").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        line?.let { parseLine(it, translations) }
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback to English if file not found
            if (languageCode != "en-us") {
                return loadTranslationsFromAssets("en-us")
            }
        }
        
        return translations
    }
    
    /**
     * Parse a line from .lang file
     * Format: key=value
     * Lines starting with # are comments
     */
    private fun parseLine(line: String, translations: MutableMap<String, String>) {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return
        }
        
        val parts = trimmed.split("=", limit = 2)
        if (parts.size == 2) {
            val key = parts[0].trim()
            val value = parts[1].trim()
            translations[key] = value
        }
    }
    
    companion object {
        @Volatile
        private var instance: TranslationManager? = null
        
        fun getInstance(context: Context): TranslationManager {
            return instance ?: synchronized(this) {
                instance ?: TranslationManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
