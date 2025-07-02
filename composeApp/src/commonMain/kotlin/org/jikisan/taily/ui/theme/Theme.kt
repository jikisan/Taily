package com.vidspark.androidapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryColor,
    secondary = DarkPrimaryColor,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    background = DarkSurface
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = Secondary,
    surface = Surface,
    onSurface = OnSurface,
    background = Background

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
expect fun isSystemInDarkThemeCompat(): Boolean

@Composable
fun TailyTheme(
    darkTheme: Boolean = isSystemInDarkThemeCompat(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}