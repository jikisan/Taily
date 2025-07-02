package com.vidspark.androidapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
actual fun isSystemInDarkThemeCompat(): Boolean = isSystemInDarkTheme()