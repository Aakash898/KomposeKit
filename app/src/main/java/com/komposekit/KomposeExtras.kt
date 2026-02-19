package com.komposekit

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeSlider — Custom animated slider
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeSlider
 *
 * A highly customizable slider with smooth dragging, thumb label, and step snapping.
 *
 * ```kotlin
 * var volume by remember { mutableStateOf(0.5f) }
 * KomposeSlider(
 *     value = volume,
 *     onValueChange = { volume = it },
 *     label = "Volume",
 *     color = Color(0xFF6C63FF),
 *     showLabel = true,
 *     steps = 10
 * )
 * ```
 */
@Composable
fun KomposeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    color: Color? = null,
    trackColor: Color? = null,
    showThumbLabel: Boolean = false,
    showValueDisplay: Boolean = false,
    thumbSize: Dp = 22.dp,
    trackHeight: Dp = 6.dp,
    enabled: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val bgTrackColor = trackColor ?: theme.surfaceVariant
    val haptic = LocalHapticFeedback.current

    var sliderWidth by remember { mutableStateOf(0) }
    var isDragging by remember { mutableStateOf(false) }

    val thumbScale by animateFloatAsState(
        targetValue = if (isDragging) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "thumbScale"
    )

    val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start))
        .coerceIn(0f, 1f)

    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = if (isDragging) snap() else spring(stiffness = Spring.StiffnessHigh),
        label = "sliderFraction"
    )

    val displayValue = when {
        valueRange.endInclusive <= 1f -> "%.0f%%".format(value * 100)
        value >= 100 -> "%.0f".format(value)
        else -> "%.1f".format(value)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (label != null || showValueDisplay) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                label?.let {
                    Text(text = it, color = theme.textSecondary, fontSize = 13.sp)
                }
                if (showValueDisplay) {
                    Text(
                        text = displayValue,
                        color = primaryColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbSize + 8.dp)
                .onSizeChanged { sliderWidth = it.width }
                .graphicsLayer { alpha = if (enabled) 1f else 0.4f }
                .pointerInput(enabled, sliderWidth, valueRange, steps) {
                    if (!enabled) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false }
                    ) { _, dragAmount ->
                        if (sliderWidth == 0) return@detectHorizontalDragGestures
                        val delta = dragAmount / sliderWidth.toFloat()
                        val range = valueRange.endInclusive - valueRange.start
                        var newValue = (value + delta * range).coerceIn(valueRange)
                        if (steps > 0) {
                            val step = range / steps
                            newValue = (newValue / step).roundToInt() * step
                            newValue = newValue.coerceIn(valueRange)
                        }
                        if (newValue != value) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onValueChange(newValue)
                        }
                    }
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // Track background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight)
                    .clip(CircleShape)
                    .background(bgTrackColor)
            ) {
                // Active track
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedFraction)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(primaryColor.copy(0.8f), primaryColor)
                            )
                        )
                )
            }

            // Step markers
            if (steps > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(steps + 1) { i ->
                        val stepFraction = i / steps.toFloat()
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (stepFraction <= fraction) Color.White.copy(0.8f)
                                    else primaryColor.copy(0.4f)
                                )
                        )
                    }
                }
            }

            // Thumb
            val thumbPadding = thumbSize / 2
            val availableWidth = sliderWidth.dp - thumbSize
            Box(
                modifier = Modifier
                    .offset(x = (availableWidth * animatedFraction))
                    .size(thumbSize)
                    .scale(thumbScale)
                    .shadow(8.dp, CircleShape, spotColor = primaryColor.copy(0.4f))
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color.White, Color.White.copy(0.9f))
                        )
                    )
                    .border(2.dp, primaryColor.copy(0.3f), CircleShape)
            ) {
                if (showThumbLabel && isDragging) {
                    // Thumb label shown above during drag
                }
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeRating — Star rating component
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeRating
 *
 * An interactive star rating component with half-star support and animations.
 *
 * ```kotlin
 * var rating by remember { mutableStateOf(3.5f) }
 * KomposeRating(
 *     rating = rating,
 *     onRatingChange = { rating = it },
 *     maxStars = 5,
 *     allowHalf = true,
 *     color = Color(0xFFFFB300)
 * )
 * ```
 */
@Composable
fun KomposeRating(
    rating: Float,
    onRatingChange: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    allowHalf: Boolean = false,
    color: Color = Color(0xFFFFB300),
    size: Dp = 32.dp,
    spacing: Dp = 4.dp,
    showLabel: Boolean = false,
    readOnly: Boolean = onRatingChange == null,
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            repeat(maxStars) { index ->
                val starValue = index + 1f
                val halfValue = index + 0.5f

                val fillLevel = when {
                    rating >= starValue -> 1f
                    allowHalf && rating >= halfValue -> 0.5f
                    else -> 0f
                }

                val animFill by animateFloatAsState(
                    targetValue = fillLevel,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "starFill_$index"
                )

                val starScale by animateFloatAsState(
                    targetValue = if (fillLevel > 0f) 1f else 0.9f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "starScale_$index"
                )

                Box(
                    modifier = Modifier
                        .size(size)
                        .scale(starScale)
                        .then(
                            if (!readOnly) Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val newRating = if (allowHalf && rating == starValue) halfValue
                                    else starValue
                                    onRatingChange?.invoke(newRating)
                                }
                            ) else Modifier
                        )
                        .drawBehind {
                            val starPath = createStarPath(size.toPx(), size.toPx())

                            // Background star (empty)
                            drawPath(
                                path = starPath,
                                color = color.copy(alpha = 0.2f),
                                style = androidx.compose.ui.graphics.drawscope.Fill
                            )

                            // Filled star
                            if (animFill > 0f) {
                                drawPath(
                                    path = starPath,
                                    color = color,
                                    alpha = animFill,
                                    style = androidx.compose.ui.graphics.drawscope.Fill
                                )
                            }
                        }
                )
            }
        }

        if (showLabel) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "%.1f / $maxStars".format(rating),
                color = color,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun createStarPath(width: Float, height: Float): Path {
    val path = Path()
    val cx = width / 2f
    val cy = height / 2f
    val outerR = width * 0.45f
    val innerR = width * 0.18f
    val totalPoints = 5

    for (i in 0 until totalPoints * 2) {
        val angle = Math.PI * i / totalPoints - Math.PI / 2
        val r = if (i % 2 == 0) outerR else innerR
        val x = cx + (r * Math.cos(angle)).toFloat()
        val y = cy + (r * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeBadge — Notification badge
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeBadge
 *
 * An animated notification badge that pops into view.
 *
 * ```kotlin
 * KomposeBadge(count = 5) {
 *     Icon(Icons.Rounded.Notifications, contentDescription = null)
 * }
 * ```
 */
@Composable
fun KomposeBadge(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    maxCount: Int = 99,
    content: @Composable () -> Unit,
) {
    val theme = KomposeKit.colors
    val badgeColor = color ?: theme.error

    val scale by animateFloatAsState(
        targetValue = if (count > 0) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "badgeScale"
    )

    Box(modifier = modifier) {
        content()
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .scale(scale)
                    .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
                    .background(badgeColor, RoundedCornerShape(999.dp))
                    .border(1.5.dp, Color.Black.copy(0.2f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > maxCount) "$maxCount+" else count.toString(),
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}