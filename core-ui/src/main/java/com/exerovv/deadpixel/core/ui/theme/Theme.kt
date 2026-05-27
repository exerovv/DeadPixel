package com.exerovv.deadpixel.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    onPrimary = White,
    secondary = Purple,
    onSecondary = White,
    background = Grey500,
    onBackground = White,
    surface = Grey300,
    onSurface = Grey100,
    onSurfaceVariant = Grey100,
    error = ErrorRed,
    onError = White
)

@Composable
fun DeadPixelTheme(
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
