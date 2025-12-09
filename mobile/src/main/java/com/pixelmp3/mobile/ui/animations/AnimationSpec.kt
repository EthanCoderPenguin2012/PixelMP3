package com.pixelmp3.mobile.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized animation specifications for consistent animations throughout the app
 */
object AnimationSpec {
    
    /**
     * Bouncy spring animation for UI elements
     */
    val bouncySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    /**
     * Smooth spring animation for subtle movements
     */
    val smoothSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    /**
     * Fast bouncy spring for interactive elements
     */
    val fastBouncySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    
    /**
     * Rotation animation spec for spinning elements
     */
    val rotationSpec = infiniteRepeatable<Float>(
        animation = tween(
            durationMillis = 3000,
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Restart
    )
    
    /**
     * Fast rotation for active playback
     */
    val fastRotationSpec = infiniteRepeatable<Float>(
        animation = tween(
            durationMillis = 1500,
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Restart
    )
    
    /**
     * Fade in animation
     */
    val fadeInSpec = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
    
    /**
     * Fade out animation
     */
    val fadeOutSpec = tween<Float>(
        durationMillis = 200,
        easing = FastOutLinearInEasing
    )
    
    /**
     * Scale animation for emphasis
     */
    val scaleSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    
    /**
     * Slide in from bottom animation
     */
    val slideInFromBottom = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        )
    ) + fadeIn(animationSpec = fadeInSpec)
    
    /**
     * Slide out to top animation
     */
    val slideOutToTop = slideOutVertically(
        targetOffsetY = { fullHeight -> -fullHeight },
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutLinearInEasing
        )
    ) + fadeOut(animationSpec = fadeOutSpec)
    
    /**
     * Spring-based Dp animation spec
     */
    fun bouncyDpSpring() = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    /**
     * Duration for press state animations before reset
     */
    const val PRESS_ANIMATION_RESET_DELAY_MS = 100L
}
