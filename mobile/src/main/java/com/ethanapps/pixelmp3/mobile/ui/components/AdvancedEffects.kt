package com.ethanapps.pixelmp3.mobile.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Confetti celebration effect
 */
@Composable
fun ConfettiEffect(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFF7B5FFF),
        Color(0xFFFF6B9D),
        Color(0xFF00BFA5),
        Color(0xFFFFD700),
        Color(0xFFFF6B6B)
    ),
    particleCount: Int = 30
) {
    if (!isActive) return
    
    val particles = remember {
        List(particleCount) {
            ConfettiParticle(
                color = colors.random(),
                startX = Random.nextFloat(),
                startY = -0.1f,
                velocityX = (Random.nextFloat() - 0.5f) * 2f,
                velocityY = Random.nextFloat() * 0.5f + 0.5f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 10f,
                size = Random.nextFloat() * 8f + 4f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        particles.forEach { particle ->
            val x = width * (particle.startX + particle.velocityX * time)
            val y = height * (particle.startY + particle.velocityY * time + 0.5f * time * time)
            val rotation = particle.rotation + particle.rotationSpeed * time * 360f
            
            if (y < height + 50f && x > -50f && x < width + 50f) {
                rotate(rotation, Offset(x, y)) {
                    drawRect(
                        color = particle.color,
                        topLeft = Offset(x - particle.size / 2, y - particle.size / 2),
                        size = androidx.compose.ui.geometry.Size(particle.size, particle.size)
                    )
                }
            }
        }
    }
}

private data class ConfettiParticle(
    val color: Color,
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val size: Float
)

/**
 * Heart burst effect for likes/favorites
 */
@Composable
fun HeartBurstEffect(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF6B9D)
) {
    if (!isActive) return
    
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "heart_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 0f else 1f,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "heart_alpha"
    )
    
    Canvas(
        modifier = modifier.size(80.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        repeat(8) { index ->
            val angle = (index * 45f) * (Math.PI / 180f).toFloat()
            val distance = 30f * scale
            val x = centerX + cos(angle) * distance
            val y = centerY + sin(angle) * distance
            
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = 4f,
                center = Offset(x, y)
            )
        }
    }
}

/**
 * Ripple wave effect
 */
@Composable
fun RippleWaveEffect(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF7B5FFF)
) {
    if (!isActive) return
    
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave1"
    )
    
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave2"
    )
    
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave3"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = minOf(size.width, size.height) / 2
        
        listOf(wave1, wave2, wave3).forEach { wave ->
            val radius = maxRadius * wave
            val alpha = (1f - wave).coerceIn(0f, 1f)
            
            drawCircle(
                color = color.copy(alpha = alpha * 0.3f),
                radius = radius,
                center = Offset(centerX, centerY)
            )
        }
    }
}

/**
 * Sparkle effect for special moments
 */
@Composable
fun SparkleEffect(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFFFFD700),
        Color(0xFFFFFFFF),
        Color(0xFFFFFACD)
    ),
    sparkleCount: Int = 12
) {
    if (!isActive) return
    
    val sparkles = remember {
        List(sparkleCount) {
            SparkleParticle(
                angle = (it * (360f / sparkleCount)),
                distance = Random.nextFloat() * 40f + 20f,
                duration = Random.nextInt(500, 1000),
                delay = Random.nextInt(0, 500)
            )
        }
    }
    
    Canvas(modifier = modifier.size(100.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        sparkles.forEachIndexed { index, sparkle ->
            val color = colors[index % colors.size]
            val angle = sparkle.angle * (Math.PI / 180f).toFloat()
            val x = centerX + cos(angle) * sparkle.distance
            val y = centerY + sin(angle) * sparkle.distance
            
            // Draw sparkle as a small star shape
            val starPath = Path().apply {
                val numPoints = 4
                val outerRadius = 3f
                val innerRadius = 1.5f
                
                for (i in 0 until numPoints * 2) {
                    val pointAngle = angle + (i * Math.PI / numPoints).toFloat()
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    val px = x + cos(pointAngle) * radius
                    val py = y + sin(pointAngle) * radius
                    
                    if (i == 0) moveTo(px, py)
                    else lineTo(px, py)
                }
                close()
            }
            
            drawPath(
                path = starPath,
                color = color
            )
        }
    }
}

private data class SparkleParticle(
    val angle: Float,
    val distance: Float,
    val duration: Int,
    val delay: Int
)

/**
 * Pulsing glow effect
 */
@Composable
fun PulsingGlowEffect(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF7B5FFF)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val baseRadius = minOf(size.width, size.height) / 2
        
        // Draw multiple layers for glow effect
        for (i in 3 downTo 1) {
            drawCircle(
                color = color.copy(alpha = alpha / (4 - i)),
                radius = baseRadius * scale * (1 + i * 0.15f),
                center = Offset(centerX, centerY)
            )
        }
    }
}

/**
 * Success checkmark animation
 */
@Composable
fun SuccessCheckmarkAnimation(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF00BFA5)
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkmark_scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "checkmark_alpha"
    )
    
    Canvas(
        modifier = modifier.size(80.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        // Draw circle background
        drawCircle(
            color = color.copy(alpha = alpha * 0.2f),
            radius = 35f * scale,
            center = Offset(centerX, centerY)
        )
        
        // Draw checkmark
        val checkPath = Path().apply {
            moveTo(centerX - 15f * scale, centerY)
            lineTo(centerX - 5f * scale, centerY + 10f * scale)
            lineTo(centerX + 15f * scale, centerY - 10f * scale)
        }
        
        drawPath(
            path = checkPath,
            color = color.copy(alpha = alpha),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 4f
            )
        )
    }
}
