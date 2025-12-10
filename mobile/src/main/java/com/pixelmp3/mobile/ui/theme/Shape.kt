package com.pixelmp3.mobile.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Expressive Material 3 shapes with a more playful and dynamic feel
val ExpressiveShapes = Shapes(
    extraSmall = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
    small = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)