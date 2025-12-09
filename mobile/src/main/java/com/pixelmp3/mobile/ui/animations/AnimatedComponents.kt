package com.pixelmp3.mobile.ui.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * A card that bounces in when it appears
 */
@Composable
fun AnimatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = AnimationSpec.bouncySpring,
        label = "card_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = AnimationSpec.fadeInSpec,
        label = "card_alpha"
    )
    
    if (onClick != null) {
        Card(
            modifier = modifier
                .scale(scale)
                .graphicsLayer(alpha = alpha),
            onClick = onClick
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier
                .scale(scale)
                .graphicsLayer(alpha = alpha)
        ) {
            content()
        }
    }
}

/**
 * An icon that spins continuously
 */
@Composable
fun SpinningIcon(
    icon: @Composable () -> Unit,
    isSpinning: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin_transition")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isSpinning) 360f else 0f,
        animationSpec = AnimationSpec.rotationSpec,
        label = "rotation"
    )
    
    Box(
        modifier = modifier.rotate(if (isSpinning) rotation else 0f)
    ) {
        icon()
    }
}

/**
 * An icon that pulses (scales up and down)
 */
@Composable
fun PulsingIcon(
    icon: @Composable () -> Unit,
    isPulsing: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPulsing) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    Box(
        modifier = modifier.scale(if (isPulsing) scale else 1f)
    ) {
        icon()
    }
}

/**
 * A button with a bouncy press animation
 */
@Composable
fun BouncyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = AnimationSpec.fastBouncySpring,
        label = "button_scale"
    )
    
    Button(
        onClick = {
            isPressed = true
            onClick()
            // Reset after a short delay
            isPressed = false
        },
        modifier = modifier.scale(scale),
        enabled = enabled
    ) {
        content()
    }
}

/**
 * A floating action button with bounce animation on appearance
 */
@Composable
fun AnimatedFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        visible = true
    }
    
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = AnimationSpec.bouncySpring,
        label = "fab_scale"
    )
    
    if (scale > 0.01f) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier.scale(scale)
        ) {
            icon()
        }
    }
}

/**
 * Animated elevation change for interactive elements
 */
@Composable
fun rememberAnimatedElevation(
    defaultElevation: androidx.compose.ui.unit.Dp = 2.dp,
    pressedElevation: androidx.compose.ui.unit.Dp = 8.dp,
    isPressed: Boolean = false
): androidx.compose.ui.unit.Dp {
    return animateDpAsState(
        targetValue = if (isPressed) pressedElevation else defaultElevation,
        animationSpec = AnimationSpec.bouncyDpSpring(),
        label = "elevation"
    ).value
}
