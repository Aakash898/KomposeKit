package com.komposekit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.NightsStay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // ← Use KomposeKit theme, NOT MyApplicationTheme
            KomposeTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KomposeKit.colors.surface)
                ) {
                    KomposeShowcase()
                }
            }
        }
    }
}

@Composable
fun KomposeShowcase() {
    val colors = KomposeKit.colors

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // ── Header ──────────────────────────────────────
        item {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "⚡ KomposeKit",
                color = colors.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Component Showcase",
                color = colors.textSecondary,
                fontSize = 15.sp
            )
        }

        // ── Toggles ─────────────────────────────────────
        item {
            SectionTitle("Toggle — 6 Styles")
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                val styles = listOf(
                    ToggleStyle.Pill     to "Pill",
                    ToggleStyle.Squircle to "Squircle",
                    ToggleStyle.Neon     to "Neon",
                    ToggleStyle.Glass    to "Glass",
                    ToggleStyle.Outlined to "Outlined",
                    ToggleStyle.Minimal  to "Minimal",
                )
                val states = remember { mutableStateListOf(true, false, true, false, true, false) }
                styles.forEachIndexed { i, (style, name) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(name, color = colors.textSecondary, fontSize = 14.sp)
                        KomposeToggle(
                            checked = states[i],
                            onCheckedChange = { states[i] = it },
                            style = style
                        )
                    }
                }
            }
        }

        // ── Toggle with Icons ────────────────────────────
        item {
            SectionTitle("Toggle — With Icons")
            Spacer(Modifier.height(12.dp))
            var darkMode by remember { mutableStateOf(false) }
            KomposeToggle(
                checked = darkMode,
                onCheckedChange = { darkMode = it },
                style = ToggleStyle.Pill,
                iconOn = Icons.Rounded.WbSunny,
                iconOff = Icons.Rounded.NightsStay,
                label = "Dark Mode",
                colorOn = Color(0xFFFFB300)
            )
        }

        // ── Toggle Group ─────────────────────────────────
        item {
            SectionTitle("Toggle Group")
            Spacer(Modifier.height(12.dp))
            var sel by remember { mutableStateOf(0) }
            KomposeToggleGroup(
                options = listOf("Daily", "Weekly", "Monthly"),
                selectedIndex = sel,
                onOptionSelected = { sel = it }
            )
        }

        // ── Buttons ──────────────────────────────────────
        item {
            SectionTitle("Buttons — 6 Styles")
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    ButtonStyle.Gradient to "Gradient",
                    ButtonStyle.Elevated to "Elevated",
                    ButtonStyle.Ghost    to "Ghost",
                    ButtonStyle.Tonal    to "Tonal",
                    ButtonStyle.Neon     to "Neon",
                    ButtonStyle.Glass    to "Glass",
                ).forEach { (style, name) ->
                    KomposeButton(
                        text = name,
                        onClick = {},
                        style = style,
                        fullWidth = true
                    )
                }
            }
        }

        // ── Pulse FAB ────────────────────────────────────
        item {
            SectionTitle("Pulse Button (FAB)")
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                KomposePulseButton(Icons.Rounded.Add, {}, color = colors.primary)
                KomposePulseButton(Icons.Rounded.Favorite, {}, color = Color(0xFFFF4081))
                KomposePulseButton(Icons.Rounded.Send, {}, color = Color(0xFF00E676))
            }
        }

        // ── Checkboxes ───────────────────────────────────
        item {
            SectionTitle("Checkbox — 4 Styles")
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val states = remember { mutableStateListOf(true, false, true, false) }
                listOf(
                    CheckboxStyle.Classic  to "Classic style",
                    CheckboxStyle.Circle   to "Circle style",
                    CheckboxStyle.Squircle to "Squircle style",
                    CheckboxStyle.Soft     to "Soft style",
                ).forEachIndexed { i, (style, label) ->
                    KomposeCheckbox(
                        checked = states[i],
                        onCheckedChange = { states[i] = it },
                        label = label,
                        style = style
                    )
                }
            }
        }

        // ── Radio ────────────────────────────────────────
        item {
            SectionTitle("Radio Buttons")
            Spacer(Modifier.height(12.dp))
            var selected by remember { mutableStateOf(0) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("Option Alpha", "Option Beta", "Option Gamma").forEachIndexed { i, opt ->
                    KomposeRadio(
                        selected = selected == i,
                        onClick = { selected = i },
                        label = opt
                    )
                }
            }
        }

        // ── Chips ────────────────────────────────────────
        item {
            SectionTitle("Chips")
            Spacer(Modifier.height(12.dp))
            val tags = listOf("Trending", "New", "Sale", "Popular")
            val selected = remember { mutableStateListOf(true, false, true, false) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tags.forEachIndexed { i, tag ->
                    KomposeChip(
                        label = tag,
                        selected = selected[i],
                        onSelectionChange = { selected[i] = it }
                    )
                }
            }
        }

        // ── Progress ─────────────────────────────────────
        item {
            SectionTitle("Progress Bar")
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KomposeProgressBar(0.73f, label = "Storage", showPercentage = true)
                KomposeProgressBar(0.45f, label = "Battery", showPercentage = true, color = Color(0xFF4CAF50))
            }
        }

        // ── Rating ───────────────────────────────────────
        item {
            SectionTitle("Star Rating")
            Spacer(Modifier.height(12.dp))
            var rating by remember { mutableStateOf(3.5f) }
            KomposeRating(
                rating = rating,
                onRatingChange = { rating = it },
                allowHalf = true,
                showLabel = true
            )
        }

        // ── Cards ────────────────────────────────────────
        item {
            SectionTitle("Cards")
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                KomposeCard(style = CardStyle.Glass, onClick = {}) {
                    Text("Glass Card", color = colors.textPrimary, fontWeight = FontWeight.Bold)
                    Text("Tap me!", color = colors.textSecondary, fontSize = 13.sp)
                }
                KomposeCard(style = CardStyle.Outlined, onClick = {}) {
                    Text("Outlined Card", color = colors.textPrimary, fontWeight = FontWeight.Bold)
                    Text("Tap me!", color = colors.textSecondary, fontSize = 13.sp)
                }
            }
        }

        // ── Tags & Divider ───────────────────────────────
        item {
            SectionTitle("Tags")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KomposeTag("New", color = Color(0xFF4CAF50))
                KomposeTag("Beta", color = Color(0xFF2196F3))
                KomposeTag("Pro", color = Color(0xFFFFB300))
                KomposeTag("Hot", color = Color(0xFFFF4081))
            }
            Spacer(Modifier.height(16.dp))
            KomposeDivider(label = "end of showcase")
        }

        item { Spacer(Modifier.height(60.dp)) }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = KomposeKit.colors.primary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.3.sp
    )
}