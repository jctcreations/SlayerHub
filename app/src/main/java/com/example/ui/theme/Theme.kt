package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SlayerColorScheme = darkColorScheme(
    primary = SlayerCrimson,
    onPrimary = Color.White,
    primaryContainer = SlayerDarkRed,
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = SlayerAmberGold,
    onSecondary = Color(0xFF261900),
    secondaryContainer = Color(0xFF4A3500),
    onSecondaryContainer = SlayerParchment,
    tertiary = SlayerWillowPurple,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF4A148C),
    onTertiaryContainer = Color(0xFFF3E5F5),
    background = SlayerNightDark,
    onBackground = TextPrimaryDark,
    surface = SlayerSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SlayerSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = SlayerCardBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SlayerColorScheme,
        typography = Typography,
        content = content
    )
}
