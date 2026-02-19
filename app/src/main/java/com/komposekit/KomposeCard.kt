package com.komposekit

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeCard — Beautiful card container
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

enum class CardStyle {
    Elevated, Outlined, Tonal, Gradient, Glass
}

/**
 * # KomposeCard
 *
 * A flexible card container with 5 visual styles and press animation.
 *
 * ```kotlin
 * KomposeCard(
 *     style = CardStyle.Glass,
 *     onClick = { ... }
 * ) {
 *     Text("Hello from KomposeKit!")
 * }
 * ```
 */
@Composable
fun KomposeCard(
    modifier: Modifier = Modifier,
    style: CardStyle = CardStyle.Elevated,
    color: Color? = null,
    cornerRadius: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.surfaceVariant
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )

    val shape = RoundedCornerShape(cornerRadius)

    val styledModifier = when (style) {
        CardStyle.Elevated -> modifier
            .scale(scale)
            .shadow(16.dp, shape, spotColor = Color.Black.copy(0.2f))
            .clip(shape)
            .background(primaryColor)

        CardStyle.Outlined -> modifier
            .scale(scale)
            .clip(shape)
            .background(theme.surface)
            .border(1.dp, Color.White.copy(0.1f), shape)

        CardStyle.Tonal -> modifier
            .scale(scale)
            .clip(shape)
            .background(theme.primary.copy(0.08f))
            .border(1.dp, theme.primary.copy(0.15f), shape)

        CardStyle.Gradient -> modifier
            .scale(scale)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(primaryColor, primaryColor.copy(0.7f))
                )
            )

        CardStyle.Glass -> modifier
            .scale(scale)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(Color.White.copy(0.12f), Color.White.copy(0.04f))
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(Color.White.copy(0.4f), Color.White.copy(0.1f))
                ),
                shape
            )
    }

    Column(
        modifier = styledModifier
            .then(
                if (onClick != null) Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = Color.White.copy(0.1f)),
                    onClick = onClick
                ) else Modifier
            )
            .padding(16.dp),
        content = content
    )
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeProgressBar — Animated progress
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeProgressBar
 *
 * A smooth animated progress bar with gradient and label support.
 *
 * ```kotlin
 * KomposeProgressBar(
 *     progress = 0.75f,
 *     label = "Uploading...",
 *     color = Color(0xFF6C63FF),
 *     showPercentage = true
 * )
 * ```
 */
@Composable
fun KomposeProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color? = null,
    trackColor: Color? = null,
    height: Dp = 10.dp,
    cornerRadius: Dp = 999.dp,
    label: String? = null,
    showPercentage: Boolean = false,
    animated: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val bg = trackColor ?: theme.surfaceVariant

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = if (animated) tween(800, easing = EaseOutCubic) else snap(),
        label = "progressAnim"
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (label != null || showPercentage) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                label?.let { Text(it, color = theme.textSecondary, fontSize = 12.sp) }
                if (showPercentage) {
                    Text(
                        "%.0f%%".format(animatedProgress * 100),
                        color = primaryColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(cornerRadius))
                .background(bg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(
                        Brush.horizontalGradient(
                            listOf(primaryColor.copy(0.85f), primaryColor)
                        )
                    )
            )
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeDivider — styled divider
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeDivider
 *
 * A styled divider with optional label.
 *
 * ```kotlin
 * KomposeDivider(label = "or continue with")
 * ```
 */
@Composable
fun KomposeDivider(
    modifier: Modifier = Modifier,
    color: Color? = null,
    label: String? = null,
    thickness: Dp = 1.dp,
) {
    val theme = KomposeKit.colors
    val dividerColor = color ?: Color.White.copy(0.1f)

    if (label != null) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(thickness)
                    .background(dividerColor)
            )
            Text(text = label, color = theme.textSecondary, fontSize = 12.sp)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(thickness)
                    .background(dividerColor)
            )
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(thickness)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, dividerColor, dividerColor, Color.Transparent)
                    )
                )
        )
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeTag — Status tag
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeTag
 *
 * A colorful non-interactive status tag/label.
 *
 * ```kotlin
 * KomposeTag("New", color = Color(0xFF4CAF50))
 * KomposeTag("Beta", color = Color(0xFFFF6584))
 * KomposeTag("Pro", color = Color(0xFFFFB300))
 * ```
 */
@Composable
fun KomposeTag(
    label: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textColor: Color = Color.White,
) {
    val theme = KomposeKit.colors
    val tagColor = color ?: theme.primary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(tagColor.copy(0.2f))
            .border(1.dp, tagColor.copy(0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.uppercase(),
            color = tagColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}