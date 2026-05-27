package com.exerovv.deadpixel.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = KotlinPurple,
    onPrimary = OnPrimaryWhite,
    primaryContainer = KotlinPurpleContainer,
    onPrimaryContainer = KotlinPurpleLight,
    secondary = KotlinOrange,
    onSecondary = Color(0xFF1C0C00),
    secondaryContainer = KotlinOrangeContainer,
    onSecondaryContainer = KotlinOrangeDim,
    background = DeadPixelBackground,
    onBackground = OnSurfaceLight,
    surface = DeadPixelSurface,
    onSurface = OnSurfaceLight,
    surfaceVariant = DeadPixelSurfaceVariant,
    onSurfaceVariant = OnSurfaceVariantGray,
    outline = DeadPixelOutline,
    error = ErrorRed,
    onError = Color(0xFF1C0009)
)

@Composable
fun DeadPixelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
