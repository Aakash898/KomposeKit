package com.komposekit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeChip — Filter & Action Chips
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

enum class ChipStyle {
    Filled, Outlined, Tonal, Gradient
}

/**
 * # KomposeChip
 *
 * A toggleable chip component with smooth selection animations.
 *
 * ```kotlin
 * var selected by remember { mutableStateOf(false) }
 * KomposeChip(
 *     label = "Trending",
 *     selected = selected,
 *     onSelectionChange = { selected = it },
 *     leadingIcon = Icons.Rounded.Whatshot,
 *     style = ChipStyle.Tonal
 * )
 * ```
 */
@Composable
fun KomposeChip(
    label: String,
    selected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    style: ChipStyle = ChipStyle.Tonal,
    color: Color? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    size: KomposeSize = KomposeSize.Medium,
    enabled: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (height, hPad, fs) = when (size) {
        KomposeSize.Small -> Triple(28.dp, 10.dp, 11.sp)
        KomposeSize.Medium -> Triple(36.dp, 14.dp, 13.sp)
        KomposeSize.Large -> Triple(44.dp, 18.dp, 15.sp)
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chipScale"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            !selected -> Color.Transparent
            style == ChipStyle.Filled || style == ChipStyle.Gradient -> primaryColor
            style == ChipStyle.Tonal -> primaryColor.copy(0.2f)
            else -> Color.Transparent
        },
        animationSpec = tween(200),
        label = "chipBg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) primaryColor else Color.Gray.copy(0.3f),
        animationSpec = tween(200),
        label = "chipBorder"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            selected && (style == ChipStyle.Filled || style == ChipStyle.Gradient) -> Color.White
            selected -> primaryColor
            else -> theme.textSecondary
        },
        animationSpec = tween(200),
        label = "chipText"
    )

    val shape = RoundedCornerShape(999.dp)

    val bgModifier = when (style) {
        ChipStyle.Gradient -> if (selected) Modifier.background(
            Brush.linearGradient(listOf(primaryColor, primaryColor.copy(0.7f))), shape
        ) else Modifier.background(theme.surfaceVariant, shape)

        else -> Modifier
            .background(bgColor, shape)
            .border(1.dp, borderColor, shape)
    }

    Row(
        modifier = modifier
            .height(height)
            .scale(scale)
            .graphicsLayer { alpha = if (enabled) 1f else 0.45f }
            .then(bgModifier)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = primaryColor.copy(0.2f)),
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSelectionChange(!selected)
                }
            )
            .padding(horizontal = hPad),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AnimatedVisibility(
            visible = leadingIcon != null,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Text(
            text = label,
            color = textColor,
            fontSize = fs,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )

        AnimatedVisibility(
            visible = trailingIcon != null,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            trailingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeCheckbox — Animated Checkboxes
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

enum class CheckboxStyle {
    /** Classic square checkbox with animated check */
    Classic,
    /** Circular checkbox */
    Circle,
    /** Squircle checkbox */
    Squircle,
    /** Soft filled checkbox */
    Soft
}

/**
 * # KomposeCheckbox
 *
 * A beautifully animated checkbox with 4 styles.
 *
 * ```kotlin
 * var checked by remember { mutableStateOf(false) }
 * KomposeCheckbox(
 *     checked = checked,
 *     onCheckedChange = { checked = it },
 *     label = "I agree to the terms",
 *     style = CheckboxStyle.Squircle,
 *     color = Color(0xFF6C63FF)
 * )
 * ```
 */
@Composable
fun KomposeCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    style: CheckboxStyle = CheckboxStyle.Classic,
    color: Color? = null,
    size: KomposeSize = KomposeSize.Medium,
    enabled: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    val boxSize = when (size) {
        KomposeSize.Small -> 18.dp
        KomposeSize.Medium -> 24.dp
        KomposeSize.Large -> 30.dp
    }

    val shape = when (style) {
        CheckboxStyle.Classic -> RoundedCornerShape(4.dp)
        CheckboxStyle.Circle -> CircleShape
        CheckboxStyle.Squircle -> RoundedCornerShape(8.dp)
        CheckboxStyle.Soft -> RoundedCornerShape(6.dp)
    }

    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkProgress"
    )

    val bgColor by animateColorAsState(
        targetValue = if (checked) primaryColor else Color.Transparent,
        animationSpec = tween(200),
        label = "checkBg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) primaryColor else Color.Gray.copy(0.5f),
        animationSpec = tween(200),
        label = "checkBorder"
    )

    val scale by animateFloatAsState(
        targetValue = if (checked) 1f else 0.9f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "checkScale"
    )

    val row = @Composable {
        Row(
            modifier = modifier
                .graphicsLayer { alpha = if (enabled) 1f else 0.45f }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCheckedChange(!checked)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .scale(scale)
                    .clip(shape)
                    .background(bgColor)
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = shape
                    )
                    .drawBehind {
                        if (progress > 0f) {
                            val canvasWidth = this.size.width   // ← use "this.size" explicitly
                            val canvasHeight = this.size.height // ← use "this.size" explicitly

                            val stroke = Stroke(
                                width = 2.5.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )

                            val p1 = Offset(canvasWidth * 0.18f, canvasHeight * 0.52f)
                            val p2 = Offset(canvasWidth * 0.42f, canvasHeight * 0.72f)
                            val p3 = Offset(canvasWidth * 0.82f, canvasHeight * 0.28f)

                            val segment1End = progress.coerceAtMost(0.5f) / 0.5f
                            val segment2Start = (progress - 0.5f).coerceAtLeast(0f) / 0.5f

                            drawLine(
                                color = Color.White,
                                start = p1,
                                end = Offset(
                                    p1.x + (p2.x - p1.x) * segment1End,
                                    p1.y + (p2.y - p1.y) * segment1End
                                ),
                                strokeWidth = stroke.width,
                                cap = stroke.cap
                            )

                            if (segment2Start > 0f) {
                                drawLine(
                                    color = Color.White,
                                    start = p2,
                                    end = Offset(
                                        p2.x + (p3.x - p2.x) * segment2Start,
                                        p2.y + (p3.y - p2.y) * segment2Start
                                    ),
                                    strokeWidth = stroke.width,
                                    cap = stroke.cap
                                )
                            }
                        }
                    }
            )

            label?.let {
                Text(
                    text = it,
                    color = theme.textPrimary,
                    fontSize = when (size) {
                        KomposeSize.Small -> 12.sp
                        KomposeSize.Medium -> 14.sp
                        KomposeSize.Large -> 16.sp
                    }
                )
            }
        }
    }

    row()
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
//  KomposeRadio — Animated Radio Buttons
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

/**
 * # KomposeRadio
 *
 * An animated radio button with satisfying spring animations.
 *
 * ```kotlin
 * var selectedIndex by remember { mutableStateOf(0) }
 * val options = listOf("Option A", "Option B", "Option C")
 *
 * options.forEachIndexed { index, option ->
 *     KomposeRadio(
 *         selected = selectedIndex == index,
 *         onClick = { selectedIndex = index },
 *         label = option
 *     )
 * }
 * ```
 */
@Composable
fun KomposeRadio(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    color: Color? = null,
    size: KomposeSize = KomposeSize.Medium,
    enabled: Boolean = true,
) {
    val theme = KomposeKit.colors
    val primaryColor = color ?: theme.primary
    val haptic = LocalHapticFeedback.current

    val radioSize = when (size) {
        KomposeSize.Small -> 18.dp
        KomposeSize.Medium -> 24.dp
        KomposeSize.Large -> 30.dp
    }

    val dotScale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "radioDot"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) primaryColor else Color.Gray.copy(0.5f),
        animationSpec = tween(200),
        label = "radioBorder"
    )

    Row(
        modifier = modifier
            .graphicsLayer { alpha = if (enabled) 1f else 0.45f }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(radioSize)
                .clip(CircleShape)
                .background(Color.Transparent)
                .border(2.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(radioSize * 0.5f)
                    .scale(dotScale)
                    .clip(CircleShape)
                    .background(primaryColor)
            )
        }

        label?.let {
            Text(
                text = it,
                color = theme.textPrimary,
                fontSize = when (size) {
                    KomposeSize.Small -> 12.sp
                    KomposeSize.Medium -> 14.sp
                    KomposeSize.Large -> 16.sp
                }
            )
        }
    }
}