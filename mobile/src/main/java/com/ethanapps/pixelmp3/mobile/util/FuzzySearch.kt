package com.ethanapps.pixelmp3.mobile.util

/**
 * Fuzzy search utilities for matching strings
 */
object FuzzySearch {
    
    /**
     * Calculate Levenshtein distance between two strings
     */
    fun levenshteinDistance(s1: String, s2: String): Int {
        val len1 = s1.length
        val len2 = s2.length
        
        val dp = Array(len1 + 1) { IntArray(len2 + 1) }
        
        for (i in 0..len1) {
            dp[i][0] = i
        }
        
        for (j in 0..len2) {
            dp[0][j] = j
        }
        
        for (i in 1..len1) {
            for (j in 1..len2) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,      // deletion
                    dp[i][j - 1] + 1,      // insertion
                    dp[i - 1][j - 1] + cost // substitution
                )
            }
        }
        
        return dp[len1][len2]
    }
    
    /**
     * Calculate similarity score (0.0 to 1.0) where 1.0 is exact match
     */
    fun similarityScore(s1: String, s2: String): Float {
        if (s1.isEmpty() && s2.isEmpty()) return 1.0f
        if (s1.isEmpty() || s2.isEmpty()) return 0.0f
        
        val distance = levenshteinDistance(s1.lowercase(), s2.lowercase())
        val maxLen = maxOf(s1.length, s2.length)
        
        return 1.0f - (distance.toFloat() / maxLen)
    }
    
    /**
     * Check if query matches text with fuzzy matching
     * @param threshold Minimum similarity score (0.0 to 1.0) required for match
     */
    fun fuzzyMatch(text: String, query: String, threshold: Float = 0.6f): Boolean {
        if (query.isEmpty()) return true
        if (text.isEmpty()) return false
        
        val textLower = text.lowercase()
        val queryLower = query.lowercase()
        
        // Exact substring match
        if (textLower.contains(queryLower)) return true
        
        // Check if query tokens match
        val queryTokens = queryLower.split(" ").filter { it.isNotEmpty() }
        val matchedTokens = queryTokens.count { token ->
            textLower.contains(token) || 
            textLower.split(" ").any { word ->
                similarityScore(word, token) >= threshold
            }
        }
        
        return matchedTokens.toFloat() / queryTokens.size >= threshold
    }
    
    /**
     * Get fuzzy match score for sorting
     */
    fun fuzzyMatchScore(text: String, query: String): Float {
        if (query.isEmpty()) return 0f
        if (text.isEmpty()) return 0f
        
        val textLower = text.lowercase()
        val queryLower = query.lowercase()
        
        // Exact match
        if (textLower == queryLower) return 1.0f
        
        // Starts with query
        if (textLower.startsWith(queryLower)) return 0.9f
        
        // Contains query
        if (textLower.contains(queryLower)) return 0.8f
        
        // Word boundary match
        val textWords = textLower.split(" ")
        val queryWords = queryLower.split(" ")
        
        var score = 0f
        var matchedWords = 0
        
        for (queryWord in queryWords) {
            val bestMatch = textWords.maxOfOrNull { textWord ->
                when {
                    textWord == queryWord -> 1.0f
                    textWord.startsWith(queryWord) -> 0.9f
                    textWord.contains(queryWord) -> 0.7f
                    else -> similarityScore(textWord, queryWord)
                }
            } ?: 0f
            
            if (bestMatch > 0.5f) {
                matchedWords++
                score += bestMatch
            }
        }
        
        if (matchedWords == 0) return 0f
        
        return (score / queryWords.size) * (matchedWords.toFloat() / queryWords.size)
    }
    
    /**
     * Highlight matched parts of text
     */
    fun highlightMatches(text: String, query: String): List<Pair<String, Boolean>> {
        if (query.isEmpty()) return listOf(Pair(text, false))
        
        val textLower = text.lowercase()
        val queryLower = query.lowercase()
        val result = mutableListOf<Pair<String, Boolean>>()
        
        var lastIndex = 0
        var index = textLower.indexOf(queryLower, lastIndex)
        
        while (index != -1) {
            // Add non-matching part
            if (index > lastIndex) {
                result.add(Pair(text.substring(lastIndex, index), false))
            }
            
            // Add matching part
            result.add(Pair(text.substring(index, index + query.length), true))
            
            lastIndex = index + query.length
            index = textLower.indexOf(queryLower, lastIndex)
        }
        
        // Add remaining text
        if (lastIndex < text.length) {
            result.add(Pair(text.substring(lastIndex), false))
        }
        
        return result
    }
}
