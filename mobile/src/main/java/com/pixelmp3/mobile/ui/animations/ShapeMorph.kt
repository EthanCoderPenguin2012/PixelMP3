package com.pixelmp3.mobile.ui.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.toPath

@Composable
fun MorphingAnimation(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val progress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "progress"
    )

    val starPolygon = remember {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            innerRadius = 1f / 3f,
            rounding = CornerRounding(1f / 6f)
        )
    }

    val circlePolygon = remember {
        RoundedPolygon.circle(
            numVertices = 12
        )
    }

    val morph = remember {
        Morph(starPolygon, circlePolygon)
    }

    Canvas(modifier = modifier) {
        val matrix = android.graphics.Matrix()
        matrix.setScale(size.width, size.height)
        val androidPath = android.graphics.Path()
        morph.toPath(progress.value, androidPath)
        androidPath.transform(matrix)
        val composePath = androidPath.asComposePath()
        drawPath(composePath, color = color)
    }
}
