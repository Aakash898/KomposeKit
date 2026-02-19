package com.komposekit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeKit · Theme Engine
//  github.com/yourhandle/KomposeKit
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

data class KomposeColors(
    // Brand
    val primary: Color = Color(0xFF6C63FF),
    val primaryVariant: Color = Color(0xFF4CAF50),
    val secondary: Color = Color(0xFFFF6584),
    val tertiary: Color = Color(0xFF43CFCF),

    // Neon accents
    val neonPurple: Color = Color(0xFFBB86FC),
    val neonGreen: Color = Color(0xFF00E676),
    val neonPink: Color = Color(0xFFFF4081),
    val neonCyan: Color = Color(0xFF18FFFF),
    val neonOrange: Color = Color(0xFFFF6D00),

    // Surfaces
    val surface: Color = Color(0xFF1E1E2E),
    val surfaceVariant: Color = Color(0xFF2A2A3E),
    val surfaceContainer: Color = Color(0xFF313145),
    val onSurface: Color = Color(0xFFE2E2FF),

    // State
    val success: Color = Color(0xFF4CAF50),
    val error: Color = Color(0xFFCF6679),
    val warning: Color = Color(0xFFFFB300),
    val info: Color = Color(0xFF2196F3),

    // Toggle specific
    val toggleTrackOn: Color = Color(0xFF6C63FF),
    val toggleTrackOff: Color = Color(0xFF3A3A5C),
    val toggleThumb: Color = Color.White,
    val toggleBorder: Color = Color(0xFF6C63FF),

    // Text
    val textPrimary: Color = Color(0xFFFFFFFF),
    val textSecondary: Color = Color(0xFFB0B0C8),
    val textDisabled: Color = Color(0xFF555570),
)

data class KomposeSizes(
    // Toggle
    val toggleWidthSmall: Dp = 48.dp,
    val toggleWidthMedium: Dp = 64.dp,
    val toggleWidthLarge: Dp = 80.dp,
    val toggleHeightSmall: Dp = 24.dp,
    val toggleHeightMedium: Dp = 32.dp,
    val toggleHeightLarge: Dp = 40.dp,

    // Thumb
    val thumbSizeSmall: Dp = 18.dp,
    val thumbSizeMedium: Dp = 24.dp,
    val thumbSizeLarge: Dp = 32.dp,

    // Chip
    val chipHeightSmall: Dp = 28.dp,
    val chipHeightMedium: Dp = 36.dp,
    val chipHeightLarge: Dp = 44.dp,

    // Button
    val buttonHeightSmall: Dp = 36.dp,
    val buttonHeightMedium: Dp = 48.dp,
    val buttonHeightLarge: Dp = 56.dp,

    // Corner radius
    val radiusSmall: Dp = 8.dp,
    val radiusMedium: Dp = 16.dp,
    val radiusLarge: Dp = 24.dp,
    val radiusFull: Dp = 999.dp,
)

data class KomposeTypography(
    val labelSmall: TextUnit = 10.sp,
    val labelMedium: TextUnit = 12.sp,
    val labelLarge: TextUnit = 14.sp,
    val bodySmall: TextUnit = 12.sp,
    val bodyMedium: TextUnit = 14.sp,
    val bodyLarge: TextUnit = 16.sp,
    val titleSmall: TextUnit = 18.sp,
    val titleMedium: TextUnit = 22.sp,
    val titleLarge: TextUnit = 28.sp,
)

data class KomposeThemeData(
    val colors: KomposeColors = KomposeColors(),
    val sizes: KomposeSizes = KomposeSizes(),
    val typography: KomposeTypography = KomposeTypography(),
)

val LocalKomposeTheme = staticCompositionLocalOf { KomposeThemeData() }

/**
 * KomposeKit Theme wrapper.
 *
 * Usage:
 * ```kotlin
 * KomposeTheme {
 *     KomposeToggle(checked = true, onCheckedChange = {})
 * }
 * ```
 *
 * Custom theme:
 * ```kotlin
 * KomposeTheme(
 *     theme = KomposeThemeData(
 *         colors = KomposeColors(primary = Color.Red)
 *     )
 * ) { ... }
 * ```
 */
@Composable
fun KomposeTheme(
    theme: KomposeThemeData = KomposeThemeData(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalKomposeTheme provides theme) {
        content()
    }
}

object KomposeKit {
    val theme: KomposeThemeData
        @Composable get() = LocalKomposeTheme.current

    val colors: KomposeColors
        @Composable get() = LocalKomposeTheme.current.colors

    val sizes: KomposeSizes
        @Composable get() = LocalKomposeTheme.current.sizes

    val typography: KomposeTypography
        @Composable get() = LocalKomposeTheme.current.typography

    // Preset themes
    val DarkTheme = KomposeThemeData()

    val LightTheme = KomposeThemeData(
        colors = KomposeColors(
            primary = Color(0xFF6C63FF),
            surface = Color(0xFFF5F5FF),
            surfaceVariant = Color(0xFFECECFF),
            surfaceContainer = Color(0xFFE0E0FF),
            onSurface = Color(0xFF1A1A2E),
            textPrimary = Color(0xFF1A1A2E),
            textSecondary = Color(0xFF555570),
            toggleTrackOff = Color(0xFFCCCCEE),
        )
    )

    val CyberTheme = KomposeThemeData(
        colors = KomposeColors(
            primary = Color(0xFF00E676),
            secondary = Color(0xFF18FFFF),
            surface = Color(0xFF0A0A0F),
            surfaceVariant = Color(0xFF0F1923),
            toggleTrackOn = Color(0xFF00E676),
            toggleBorder = Color(0xFF00E676),
        )
    )

    val SunsetTheme = KomposeThemeData(
        colors = KomposeColors(
            primary = Color(0xFFFF6584),
            secondary = Color(0xFFFFB347),
            surface = Color(0xFF1A0A14),
            surfaceVariant = Color(0xFF2A1020),
            toggleTrackOn = Color(0xFFFF6584),
            toggleBorder = Color(0xFFFFB347),
        )
    )

    val OceanTheme = KomposeThemeData(
        colors = KomposeColors(
            primary = Color(0xFF43CFCF),
            secondary = Color(0xFF2196F3),
            surface = Color(0xFF0A1628),
            surfaceVariant = Color(0xFF0F2040),
            toggleTrackOn = Color(0xFF43CFCF),
            toggleBorder = Color(0xFF43CFCF),
        )
    )
}

// Size enum used across components
enum class KomposeSize { Small, Medium, Large }