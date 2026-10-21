package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PoseMateColorScheme =
  darkColorScheme(
      primary = PrimaryNeon,
      onPrimary = DarkBackground,
      secondary = SecondaryViolet,
      onSecondary = TextPrimary,
      tertiary = AccentPink,
      background = DarkBackground,
      onBackground = TextPrimary,
      surface = DarkSurface,
      onSurface = TextPrimary,
      surfaceVariant = CardSurface,
      onSurfaceVariant = TextSecondary,
  )

@Composable
fun PoseMateAITheme(
    content: @Composable () -> Unit,
) {
  MaterialTheme(
      colorScheme = PoseMateColorScheme,
      typography = Typography,
      content = content,
  )
}

// Alias for compatibility with template naming if needed
@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit,
) {
  PoseMateAITheme(content = content)
}
