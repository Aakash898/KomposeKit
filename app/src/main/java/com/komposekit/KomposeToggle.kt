package com.komposekit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeToggle — 6 Visual Styles
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

enum class ToggleStyle {
    /** Classic pill-shaped toggle — smooth and clean */
    Pill,
    /** Squircle toggle with rounded-square thumb */
    Squircle,
    /** Neon glowing toggle with glow effect */
    Neon,
    /** Glassmorphism frosted-glass look */
    Glass,
    /** Outlined toggle with animated border */
    Outlined,
    /** Flat minimal toggle with icon morph */
    Minimal
}

/**
 * # KomposeToggle
 *
 * A highly customizable toggle/switch component with 6 beautiful styles,
 * smooth spring animations, haptic feedback, and icon support.
 *
 * ## Basic Usage
 * ```kotlin
 * var checked by remember { mutableStateOf(false) }
 * KomposeToggle(
 *     checked = checked,
 *     onCheckedChange = { checked = it }
 * )
 * ```
 *
 * ## With Style
 * ```kotlin
 * KomposeToggle(
 *     checked = checked,
 *     onCheckedChange = { checked = it },
 *     style = ToggleStyle.Neon,
 *     colorOn = Color(0xFF00E676),
 *     size = KomposeSize.Large
 * )
 * ```
 *
 * ## With Icons
 * ```kotlin
 * KomposeToggle(
 *     checked = checked,
 *     onCheckedChange = { checked = it },
 *     iconOn = Icons.Rounded.WbSunny,
 *     iconOff = Icons.Rounded.NightsStay
 * )
 * ```
 */
@Composable
fun KomposeToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    style: ToggleStyle = ToggleStyle.Pill,
    size: KomposeSize = KomposeSize.Medium,
    colorOn: Color? = null,
    colorOff: Color? = null,
    thumbColor: Color? = null,
    iconOn: ImageVector? = null,
    iconOff: ImageVector? = null,
    enabled: Boolean = true,
    hapticEnabled: Boolean = true,
    label: String? = null,
    labelPosition: LabelPosition = LabelPosition.End,
) {
    val theme = KomposeKit.colors
    val haptic = LocalHapticFeedback.current

    val activeColor = colorOn ?: theme.toggleTrackOn
    val inactiveColor = colorOff ?: theme.toggleTrackOff
    val thumbCol = thumbColor ?: theme.toggleThumb

    val (width, height, thumbSize) = when (size) {
        KomposeSize.Small -> Triple(48.dp, 26.dp, 20.dp)
        KomposeSize.Medium -> Triple(64.dp, 34.dp, 26.dp)
        KomposeSize.Large -> Triple(80.dp, 44.dp, 36.dp)
    }

    val interactionSource = remember { MutableInteractionSource() }

    // ── Animations ──────────────────────────────────
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "thumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) activeColor else inactiveColor,
        animationSpec = tween(300, easing = EaseInOutCubic),
        label = "trackColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (checked) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val alpha = if (enabled) 1f else 0.4f

    fun toggle() {
        if (!enabled) return
        if (hapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onCheckedChange(!checked)
    }

    val toggleContent = @Composable {
        Box(
            modifier = modifier
                .scale(scale)
                .graphicsLayer { this.alpha = alpha }
                .semantics {
                    this.role = Role.Switch
                    this.stateDescription = if (checked) "On" else "Off"
                    label?.let { contentDescription = it }
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = ::toggle
                )
        ) {
            when (style) {
                ToggleStyle.Pill -> PillToggle(
                    checked, thumbOffset, trackColor, thumbCol,
                    width, height, thumbSize, iconOn, iconOff, activeColor
                )
                ToggleStyle.Squircle -> SquircleToggle(
                    checked, thumbOffset, trackColor, thumbCol,
                    width, height, thumbSize, iconOn, iconOff, activeColor
                )
                ToggleStyle.Neon -> NeonToggle(
                    checked, thumbOffset, trackColor, thumbCol,
                    width, height, thumbSize, iconOn, iconOff, activeColor
                )
                ToggleStyle.Glass -> GlassToggle(
                    checked, thumbOffset, trackColor, thumbCol,
                    width, height, thumbSize, iconOn, iconOff, activeColor
                )
                ToggleStyle.Outlined -> OutlinedToggle(
                    checked, thumbOffset, trackColor, thumbCol,
                    width, height, thumbSize, iconOn, iconOff, activeColor
                )
                ToggleStyle.Minimal -> MinimalToggle(
                    checked, thumbOffset, trackColor, thumbCol,
                    width, height, thumbSize, iconOn, iconOff, activeColor
                )
            }
        }
    }

    if (label != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (labelPosition == LabelPosition.Start) {
                Text(text = label, color = theme.textPrimary, fontSize = 14.sp)
            }
            toggleContent()
            if (labelPosition == LabelPosition.End) {
                Text(text = label, color = theme.textPrimary, fontSize = 14.sp)
            }
        }
    } else {
        toggleContent()
    }
}

enum class LabelPosition { Start, End }

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  Style Implementations
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

@Composable
private fun PillToggle(
    checked: Boolean,
    thumbOffset: Float,
    trackColor: Color,
    thumbColor: Color,
    width: Dp, height: Dp, thumbSize: Dp,
    iconOn: ImageVector?, iconOff: ImageVector?,
    activeColor: Color
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(trackColor)
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val padding = (height - thumbSize) / 2
        val maxOffset = width - thumbSize - padding * 2
        Box(
            modifier = Modifier
                .offset(x = padding + maxOffset * thumbOffset)
                .size(thumbSize)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            val icon = if (checked) iconOn else iconOff
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(thumbSize * 0.55f),
                    tint = activeColor
                )
            }
        }
    }
}

@Composable
private fun SquircleToggle(
    checked: Boolean,
    thumbOffset: Float,
    trackColor: Color,
    thumbColor: Color,
    width: Dp, height: Dp, thumbSize: Dp,
    iconOn: ImageVector?, iconOff: ImageVector?,
    activeColor: Color
) {
    val thumbShape = RoundedCornerShape(6.dp)
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(10.dp))
            .background(trackColor)
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val padding = (height - thumbSize) / 2
        val maxOffset = width - thumbSize - padding * 2
        Box(
            modifier = Modifier
                .offset(x = padding + maxOffset * thumbOffset)
                .size(thumbSize)
                .shadow(4.dp, thumbShape)
                .clip(thumbShape)
                .background(thumbColor),
            contentAlignment = Alignment.Center
        ) {
            val icon = if (checked) iconOn else iconOff
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(thumbSize * 0.55f),
                    tint = activeColor
                )
            }
        }
    }
}

@Composable
private fun NeonToggle(
    checked: Boolean,
    thumbOffset: Float,
    trackColor: Color,
    thumbColor: Color,
    width: Dp, height: Dp, thumbSize: Dp,
    iconOn: ImageVector?, iconOff: ImageVector?,
    activeColor: Color
) {
    val glowAlpha by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(400),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .drawBehind {
                if (glowAlpha > 0f) {
                    val glowColor = activeColor.copy(alpha = 0.4f * glowAlpha)
                    drawRoundRect(
                        color = glowColor,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2),
                        style = Stroke(width = 8.dp.toPx())
                    )
                }
            }
            .clip(CircleShape)
            .background(
                if (checked)
                    Brush.horizontalGradient(listOf(activeColor.copy(0.8f), activeColor))
                else
                    Brush.horizontalGradient(listOf(trackColor, trackColor))
            )
            .border(
                width = 1.5.dp,
                brush = if (checked)
                    Brush.horizontalGradient(listOf(activeColor, activeColor.copy(0.5f)))
                else
                    Brush.horizontalGradient(listOf(Color.White.copy(0.1f), Color.White.copy(0.05f))),
                shape = CircleShape
            )
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val padding = (height - thumbSize) / 2
        val maxOffset = width - thumbSize - padding * 2
        Box(
            modifier = Modifier
                .offset(x = padding + maxOffset * thumbOffset)
                .size(thumbSize)
                .shadow(if (checked) 12.dp else 4.dp, CircleShape, spotColor = activeColor)
                .clip(CircleShape)
                .background(
                    if (checked)
                        Brush.radialGradient(listOf(Color.White, thumbColor.copy(0.9f)))
                    else
                        Brush.radialGradient(listOf(thumbColor, thumbColor.copy(0.8f)))
                ),
            contentAlignment = Alignment.Center
        ) {
            val icon = if (checked) iconOn else iconOff
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(thumbSize * 0.55f),
                    tint = if (checked) activeColor else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun GlassToggle(
    checked: Boolean,
    thumbOffset: Float,
    trackColor: Color,
    thumbColor: Color,
    width: Dp, height: Dp, thumbSize: Dp,
    iconOn: ImageVector?, iconOff: ImageVector?,
    activeColor: Color
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = if (checked) {
                        listOf(activeColor.copy(0.5f), activeColor.copy(0.25f))
                    } else {
                        listOf(Color.White.copy(0.15f), Color.White.copy(0.05f))
                    }
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(0.4f), Color.White.copy(0.1f))
                ),
                shape = CircleShape
            )
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val padding = (height - thumbSize) / 2
        val maxOffset = width - thumbSize - padding * 2
        Box(
            modifier = Modifier
                .offset(x = padding + maxOffset * thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color.White.copy(0.9f), Color.White.copy(0.6f))
                    )
                )
                .border(
                    width = 0.5.dp,
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(0.8f), Color.White.copy(0.2f))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val icon = if (checked) iconOn else iconOff
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(thumbSize * 0.55f),
                    tint = if (checked) activeColor else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun OutlinedToggle(
    checked: Boolean,
    thumbOffset: Float,
    trackColor: Color,
    thumbColor: Color,
    width: Dp, height: Dp, thumbSize: Dp,
    iconOn: ImageVector?, iconOff: ImageVector?,
    activeColor: Color
) {
    val borderColor by animateColorAsState(
        targetValue = if (checked) activeColor else Color.Gray.copy(0.5f),
        animationSpec = tween(300),
        label = "border"
    )

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(Color.Transparent)
            .border(2.dp, borderColor, CircleShape)
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val padding = (height - thumbSize) / 2
        val maxOffset = width - thumbSize - padding * 2
        Box(
            modifier = Modifier
                .offset(x = padding + maxOffset * thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(if (checked) activeColor else Color.Gray.copy(0.4f)),
            contentAlignment = Alignment.Center
        ) {
            val icon = if (checked) iconOn else iconOff
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(thumbSize * 0.55f),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun MinimalToggle(
    checked: Boolean,
    thumbOffset: Float,
    trackColor: Color,
    thumbColor: Color,
    width: Dp, height: Dp, thumbSize: Dp,
    iconOn: ImageVector?, iconOff: ImageVector?,
    activeColor: Color
) {
    // Thin track with floating thumb
    val thinHeight = height / 2
    Box(
        modifier = Modifier
            .width(width)
            .height(height),
        contentAlignment = Alignment.Center
    ) {
        // Track
        Box(
            modifier = Modifier
                .width(width)
                .height(4.dp)
                .clip(CircleShape)
                .background(trackColor)
        )
        // Thumb
        val padding = thumbSize / 2
        val maxOffset = width - thumbSize
        Box(
            modifier = Modifier
                .offset(x = -width / 2 + padding + maxOffset * thumbOffset)
                .size(thumbSize)
                .shadow(8.dp, CircleShape, spotColor = if (checked) activeColor else Color.Black)
                .clip(CircleShape)
                .background(if (checked) activeColor else thumbColor),
            contentAlignment = Alignment.Center
        ) {
            val icon = if (checked) iconOn else iconOff
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(thumbSize * 0.5f),
                    tint = if (checked) Color.White else Color.Gray
                )
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  ToggleGroup — Segmented Control
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeToggleGroup
 *
 * A segmented control that lets users pick one option from a group.
 *
 * ```kotlin
 * var selected by remember { mutableStateOf(0) }
 * KomposeToggleGroup(
 *     options = listOf("Day", "Week", "Month"),
 *     selectedIndex = selected,
 *     onOptionSelected = { selected = it }
 * )
 * ```
 */
@Composable
fun KomposeToggleGroup(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colorSelected: Color? = null,
    colorUnselected: Color? = null,
    size: KomposeSize = KomposeSize.Medium,
    icons: List<ImageVector?>? = null,
) {
    val theme = KomposeKit.colors
    val activeColor = colorSelected ?: theme.primary
    val inactiveColor = colorUnselected ?: theme.surfaceVariant

    val height = when (size) {
        KomposeSize.Small -> 36.dp
        KomposeSize.Medium -> 44.dp
        KomposeSize.Large -> 54.dp
    }

    val fontSize = when (size) {
        KomposeSize.Small -> 12.sp
        KomposeSize.Medium -> 14.sp
        KomposeSize.Large -> 16.sp
    }

    Box(
        modifier = modifier
            .height(height)
            .clip(CircleShape)
            .background(inactiveColor)
            .border(1.dp, Color.White.copy(0.05f), CircleShape)
    ) {
        Row(modifier = Modifier.height(height)) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) activeColor else Color.Transparent,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "segmentBg"
                )

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else theme.textSecondary,
                    animationSpec = tween(200),
                    label = "segmentText"
                )

                val scaleAnim by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.95f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "segmentScale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = activeColor.copy(0.3f)),
                            onClick = { onOptionSelected(index) }
                        )
                        .padding(horizontal = 20.dp)
                        .scale(scaleAnim),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        icons?.getOrNull(index)?.let { icon ->
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = textColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = option,
                            color = textColor,
                            fontSize = fontSize,
                        )
                    }
                }
            }
        }
    }
}