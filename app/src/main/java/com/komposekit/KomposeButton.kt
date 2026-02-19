package com.komposekit

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeButton Styles
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

enum class ButtonStyle {
    /** Filled gradient button */
    Gradient,
    /** Elevated button with shadow */
    Elevated,
    /** Ghost / outlined button */
    Ghost,
    /** Tonal flat button */
    Tonal,
    /** Neon glowing button */
    Neon,
    /** Glassmorphism button */
    Glass,
}

/**
 * # KomposeButton
 *
 * A beautiful, animated button with 6 visual styles.
 *
 * ```kotlin
 * KomposeButton(
 *     text = "Get Started",
 *     onClick = { ... },
 *     style = ButtonStyle.Gradient,
 *     icon = Icons.Rounded.ArrowForward,
 *     size = KomposeSize.Large
 * )
 * ```
 */
@Composable
fun KomposeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Gradient,
    size: KomposeSize = KomposeSize.Medium,
    color: Color? = null,
    textColor: Color? = null,
    icon: ImageVector? = null,
    iconEnd: ImageVector? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    cornerRadius: Dp? = null,
) {
    val theme = KomposeKit.colors
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val primaryColor = color ?: theme.primary

    val (height, hPad, fontSize) = when (size) {
        KomposeSize.Small -> Triple(36.dp, 16.dp, 13.sp)
        KomposeSize.Medium -> Triple(48.dp, 24.dp, 15.sp)
        KomposeSize.Large -> Triple(58.dp, 32.dp, 17.sp)
    }

    val radius = cornerRadius ?: when (size) {
        KomposeSize.Small -> 8.dp
        KomposeSize.Medium -> 14.dp
        KomposeSize.Large -> 18.dp
    }

    val shape = RoundedCornerShape(radius)
    val alpha = if (enabled) 1f else 0.45f

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btnScale"
    )

    // Infinite animation for Neon pulsing
    val infiniteTransition = rememberInfiniteTransition(label = "neonPulse")
    val neonGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = EaseInOutSine),
            RepeatMode.Reverse
        ),
        label = "neonGlow"
    )

    val btnModifier = modifier
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
        .height(height)
        .scale(scale)
        .graphicsLayer { this.alpha = alpha }

    val contentColor = textColor ?: Color.White

    val styledModifier = when (style) {
        ButtonStyle.Gradient -> btnModifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(primaryColor, primaryColor.copy(0.7f))
                )
            )
            .shadow(8.dp, shape, spotColor = primaryColor.copy(0.4f))

        ButtonStyle.Elevated -> btnModifier
            .shadow(12.dp, shape, spotColor = Color.Black.copy(0.3f))
            .clip(shape)
            .background(primaryColor)

        ButtonStyle.Ghost -> btnModifier
            .clip(shape)
            .background(Color.Transparent)
            .border(1.5.dp, primaryColor, shape)

        ButtonStyle.Tonal -> btnModifier
            .clip(shape)
            .background(primaryColor.copy(0.15f))

        ButtonStyle.Neon -> btnModifier
            .drawBehind {
                drawRoundRect(
                    color = primaryColor.copy(alpha = 0.4f * neonGlow),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius.toPx()),
                    style = Stroke(width = 8.dp.toPx())
                )
            }
            .clip(shape)
            .background(primaryColor.copy(0.2f))
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(listOf(primaryColor, primaryColor.copy(0.4f))),
                shape = shape
            )

        ButtonStyle.Glass -> btnModifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(Color.White.copy(0.2f), Color.White.copy(0.05f))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color.White.copy(0.5f), Color.White.copy(0.1f))
                ),
                shape = shape
            )
    }

    val labelColor = when (style) {
        ButtonStyle.Ghost, ButtonStyle.Tonal -> textColor ?: primaryColor
        ButtonStyle.Neon -> textColor ?: primaryColor
        else -> contentColor
    }

    Box(
        modifier = styledModifier
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = contentColor.copy(0.2f)),
                enabled = enabled && !loading,
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            )
            .padding(horizontal = hPad),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = labelColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = labelColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = text,
                    color = labelColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.3.sp
                )
                iconEnd?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = labelColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposePulseButton — FAB with pulse ring
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposePulseButton
 *
 * A floating action button with an infinite pulsing ring animation.
 * Great for primary CTAs.
 *
 * ```kotlin
 * KomposePulseButton(
 *     icon = Icons.Rounded.Add,
 *     onClick = { ... },
 *     color = Color(0xFF6C63FF)
 * )
 * ```
 */
@Composable
fun KomposePulseButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color? = null,
    size: Dp = 56.dp,
    pulseEnabled: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val haptic = LocalHapticFeedback.current

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = EaseOutCubic), RepeatMode.Restart
        ), label = "p1"
    )
    val pulseAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = EaseOutCubic), RepeatMode.Restart
        ), label = "a1"
    )
    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            tween(1800, delayMillis = 600, easing = EaseOutCubic), RepeatMode.Restart
        ), label = "p2"
    )
    val pulseAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(1800, delayMillis = 600, easing = EaseOutCubic), RepeatMode.Restart
        ), label = "a2"
    )

    Box(
        modifier = modifier.size(size * 2),
        contentAlignment = Alignment.Center
    ) {
        if (pulseEnabled) {
            Box(
                modifier = Modifier
                    .size(size)
                    .scale(pulseScale1)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = pulseAlpha1))
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .scale(pulseScale2)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = pulseAlpha2))
            )
        }
        Box(
            modifier = Modifier
                .size(size)
                .shadow(12.dp, CircleShape, spotColor = primaryColor.copy(0.5f))
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(primaryColor, primaryColor.copy(0.8f)))
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = Color.White.copy(0.3f)),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.45f)
            )
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeIconButton — Animated icon button
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeIconButton
 *
 * A stylish icon-only button with bounce animation.
 *
 * ```kotlin
 * KomposeIconButton(
 *     icon = Icons.Rounded.Favorite,
 *     onClick = { ... },
 *     style = ButtonStyle.Tonal
 * )
 * ```
 */
@Composable
fun KomposeIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Tonal,
    color: Color? = null,
    size: Dp = 48.dp,
    iconSize: Dp? = null,
    enabled: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "iconBtnScale"
    )

    val shape = CircleShape
    val effectiveIconSize = iconSize ?: (size * 0.45f)

    val bgModifier = when (style) {
        ButtonStyle.Gradient -> Modifier
            .clip(shape)
            .background(Brush.linearGradient(listOf(primaryColor, primaryColor.copy(0.7f))))
        ButtonStyle.Tonal -> Modifier
            .clip(shape)
            .background(primaryColor.copy(0.15f))
        ButtonStyle.Ghost -> Modifier
            .clip(shape)
            .border(1.5.dp, primaryColor, shape)
        ButtonStyle.Elevated -> Modifier
            .shadow(8.dp, shape)
            .clip(shape)
            .background(primaryColor)
        ButtonStyle.Neon -> Modifier
            .clip(shape)
            .background(primaryColor.copy(0.2f))
            .border(1.5.dp, primaryColor, shape)
        ButtonStyle.Glass -> Modifier
            .clip(shape)
            .background(Color.White.copy(0.15f))
            .border(1.dp, Color.White.copy(0.3f), shape)
    }

    val iconTint = when (style) {
        ButtonStyle.Ghost, ButtonStyle.Tonal, ButtonStyle.Neon -> primaryColor
        else -> Color.White
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .graphicsLayer { alpha = if (enabled) 1f else 0.45f }
            .then(bgModifier)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = iconTint.copy(0.3f)),
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(effectiveIconSize)
        )
    }
}