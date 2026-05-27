package com.exerovv.deadpixel.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    onPrimary = White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = Color(0xFFBEA9FF),
    secondary = Orange,
    onSecondary = Color(0xFF1C0C00),
    secondaryContainer = OrangeContainer,
    onSecondaryContainer = Color(0xFFFFB74D),
    background = Grey500,
    onBackground = White,
    surface = Grey300,
    onSurface = White,
    onSurfaceVariant = Grey100,
    outline = Color(0xFF605C6B),
    error = ErrorRed,
    onError = Color(0xFF1C0009)
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
