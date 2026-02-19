<div align="center">

# ⚡ KomposeKit

### The Android Compose UI component library that doesn't suck.

<br/>

[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6+-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen?style=for-the-badge)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge)](LICENSE)
[![JitPack](https://img.shields.io/badge/JitPack-1.0.0-FF6584?style=for-the-badge)](https://jitpack.io)

<br/>

> Beautifully animated toggles, buttons, chips, checkboxes, sliders, ratings, and more —  
> all **fully customizable**, **spring-animated**, with **haptic feedback** baked in.

<br/>

```
⚡ 6 Toggle Styles    🎨 5 Preset Themes    🔧 100% Customizable
🌊 Spring Animations  📳 Haptic Feedback    ♿ Accessible
```

</div>

---

## 🧩 Components

| Component | Styles | Features |
|-----------|--------|----------|
| `KomposeToggle` | Pill, Squircle, Neon, Glass, Outlined, Minimal | Icons, labels, haptic |
| `KomposeToggleGroup` | Segmented Control | Spring animation, icon support |
| `KomposeButton` | Gradient, Elevated, Ghost, Tonal, Neon, Glass | Loading state, icons |
| `KomposePulseButton` | FAB + Pulse | Infinite pulse ring animation |
| `KomposeIconButton` | 6 styles | Bounce animation |
| `KomposeChip` | Filled, Outlined, Tonal, Gradient | Select/deselect animation |
| `KomposeCheckbox` | Classic, Circle, Squircle, Soft | Animated check draw |
| `KomposeRadio` | — | Spring dot animation |
| `KomposeSlider` | — | Step snapping, gradient track |
| `KomposeRating` | — | Half-star, animated fills |
| `KomposeBadge` | — | Pop animation |
| `KomposeCard` | Elevated, Outlined, Tonal, Gradient, Glass | Press animation |
| `KomposeProgressBar` | — | Smooth animated fill |
| `KomposeDivider` | — | Label support |
| `KomposeTag` | — | Status labels |

---

## 🚀 Installation

**Step 1** — Add JitPack to your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Step 2** — Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.YourGithubHandle:KomposeKit:1.0.0")
}
```

---

## ⚡ Quick Start

Wrap your content with `KomposeTheme` (or skip it for defaults):

```kotlin
KomposeTheme {
    var checked by remember { mutableStateOf(false) }
    KomposeToggle(
        checked = checked,
        onCheckedChange = { checked = it }
    )
}
```

---

## 🎛️ KomposeToggle

The star of the library. **6 visual styles**, spring animations, icons, haptic feedback.

```kotlin
var checked by remember { mutableStateOf(false) }

// Pill (default)
KomposeToggle(checked = checked, onCheckedChange = { checked = it })

// Neon style with custom color
KomposeToggle(
    checked = checked,
    onCheckedChange = { checked = it },
    style = ToggleStyle.Neon,
    colorOn = Color(0xFF00E676),
    size = KomposeSize.Large
)

// Glass style with icons
KomposeToggle(
    checked = checked,
    onCheckedChange = { checked = it },
    style = ToggleStyle.Glass,
    iconOn = Icons.Rounded.WbSunny,
    iconOff = Icons.Rounded.NightsStay,
    label = "Dark Mode",
    labelPosition = LabelPosition.End
)

// All 6 styles:
ToggleStyle.Pill       // classic pill — smooth & clean
ToggleStyle.Squircle   // rounded-square thumb
ToggleStyle.Neon       // glowing neon edge + thumb
ToggleStyle.Glass      // frosted glass effect
ToggleStyle.Outlined   // transparent with animated border
ToggleStyle.Minimal    // thin track with floating thumb
```

### Full API

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `checked` | `Boolean` | required | Current state |
| `onCheckedChange` | `(Boolean) -> Unit` | required | State callback |
| `style` | `ToggleStyle` | `Pill` | Visual style |
| `size` | `KomposeSize` | `Medium` | Small / Medium / Large |
| `colorOn` | `Color?` | theme primary | Active track color |
| `colorOff` | `Color?` | theme surface | Inactive track color |
| `thumbColor` | `Color?` | white | Thumb color |
| `iconOn` | `ImageVector?` | null | Icon when on |
| `iconOff` | `ImageVector?` | null | Icon when off |
| `enabled` | `Boolean` | true | Disabled state |
| `hapticEnabled` | `Boolean` | true | Haptic feedback |
| `label` | `String?` | null | Side label |
| `labelPosition` | `LabelPosition` | `End` | Start / End |

---

## 🔘 KomposeToggleGroup

A segmented control with spring animation:

```kotlin
var selected by remember { mutableStateOf(0) }

KomposeToggleGroup(
    options = listOf("Daily", "Weekly", "Monthly"),
    selectedIndex = selected,
    onOptionSelected = { selected = it },
    size = KomposeSize.Medium
)

// With icons
KomposeToggleGroup(
    options = listOf("List", "Grid", "Map"),
    selectedIndex = selected,
    onOptionSelected = { selected = it },
    icons = listOf(
        Icons.Rounded.ViewList,
        Icons.Rounded.GridView,
        Icons.Rounded.Map
    )
)
```

---

## 🎯 KomposeButton

6 styles, loading state, spring press animation:

```kotlin
// Gradient (default)
KomposeButton(text = "Get Started", onClick = { })

// Neon with icon
KomposeButton(
    text = "Continue",
    onClick = { },
    style = ButtonStyle.Neon,
    icon = Icons.Rounded.ArrowForward,
    color = Color(0xFF00E676),
    size = KomposeSize.Large
)

// With loading state
var loading by remember { mutableStateOf(false) }
KomposeButton(
    text = "Upload",
    onClick = { loading = true },
    loading = loading,
    style = ButtonStyle.Gradient,
    fullWidth = true
)

// All styles: Gradient, Elevated, Ghost, Tonal, Neon, Glass
```

### KomposePulseButton (FAB)

```kotlin
KomposePulseButton(
    icon = Icons.Rounded.Add,
    onClick = { },
    color = Color(0xFF6C63FF)
)
```

---

## ✅ KomposeCheckbox

```kotlin
var agreed by remember { mutableStateOf(false) }

KomposeCheckbox(
    checked = agreed,
    onCheckedChange = { agreed = it },
    label = "I agree to the Terms of Service",
    style = CheckboxStyle.Squircle,
    color = Color(0xFF6C63FF)
)

// Styles: Classic, Circle, Squircle, Soft
```

---

## 🏷️ KomposeChip

```kotlin
var selected by remember { mutableStateOf(false) }

KomposeChip(
    label = "Trending",
    selected = selected,
    onSelectionChange = { selected = it },
    leadingIcon = Icons.Rounded.Whatshot,
    style = ChipStyle.Tonal
)

// Styles: Filled, Outlined, Tonal, Gradient
```

---

## ⭐ KomposeRating

```kotlin
var rating by remember { mutableStateOf(3.5f) }

KomposeRating(
    rating = rating,
    onRatingChange = { rating = it },
    maxStars = 5,
    allowHalf = true,
    color = Color(0xFFFFB300),
    showLabel = true
)
```

---

## 📊 KomposeSlider

```kotlin
var value by remember { mutableStateOf(0.7f) }

KomposeSlider(
    value = value,
    onValueChange = { value = it },
    label = "Brightness",
    showValueDisplay = true,
    steps = 10,
    color = Color(0xFF6C63FF)
)
```

---

## 🎨 Themes

KomposeKit ships with **5 beautiful preset themes**:

```kotlin
// Dark (default)
KomposeTheme { ... }

// Light
KomposeTheme(theme = KomposeKit.LightTheme) { ... }

// Cyber (green neon)
KomposeTheme(theme = KomposeKit.CyberTheme) { ... }

// Sunset (pink/orange)
KomposeTheme(theme = KomposeKit.SunsetTheme) { ... }

// Ocean (teal/blue)
KomposeTheme(theme = KomposeKit.OceanTheme) { ... }
```

### Custom Theme

Go full custom — override any token:

```kotlin
KomposeTheme(
    theme = KomposeThemeData(
        colors = KomposeColors(
            primary = Color(0xFFE91E63),
            secondary = Color(0xFFFF5722),
            surface = Color(0xFF12121F),
            toggleTrackOn = Color(0xFFE91E63),
            neonPink = Color(0xFFFF4081)
        ),
        sizes = KomposeSizes(
            toggleWidthMedium = 70.dp,
            toggleHeightMedium = 36.dp,
        )
    )
) {
    // Your UI
}
```

---

## 🃏 KomposeCard

```kotlin
// Glass card with click
KomposeCard(
    style = CardStyle.Glass,
    onClick = { }
) {
    Text("Frosted Glass Card", color = Color.White)
    Spacer(Modifier.height(8.dp))
    KomposeTag("New", color = Color(0xFF4CAF50))
}

// Styles: Elevated, Outlined, Tonal, Gradient, Glass
```

---

## 🔔 KomposeBadge

```kotlin
KomposeBadge(count = 5) {
    Icon(Icons.Rounded.Notifications, contentDescription = null)
}
```

---

## 📦 KomposeProgressBar

```kotlin
KomposeProgressBar(
    progress = 0.75f,
    label = "Uploading...",
    showPercentage = true,
    color = Color(0xFF6C63FF)
)
```

---

## 🏗️ Project Structure

```
KomposeKit/
├── library/
│   └── src/main/java/com/komposekit/
│       ├── KomposeTheme.kt       ← Theme engine, color tokens, presets
│       ├── KomposeToggle.kt      ← Toggle + ToggleGroup
│       ├── KomposeButton.kt      ← Button, PulseButton, IconButton
│       ├── KomposeInputs.kt      ← Chip, Checkbox, Radio
│       ├── KomposeExtras.kt      ← Slider, Rating, Badge
│       └── KomposeCard.kt        ← Card, ProgressBar, Divider, Tag
└── app/                          ← Sample showcase app
```

---

## 🤝 Contributing

PRs are welcome! Here's how to get started:

```bash
git clone https://github.com/YourGithubHandle/KomposeKit.git
cd KomposeKit
# Open in Android Studio
```

**Want to add a component?** File an issue first so we can align on API design.

---

## 📜 License

```
Copyright 2024 KomposeKit Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```

---

<div align="center">

**Built with ❤️ for the Android community**

If this saved you time, please ⭐ the repo — it helps a lot!

[🐛 Report Bug](https://github.com/YourGithubHandle/KomposeKit/issues) · [💡 Request Feature](https://github.com/YourGithubHandle/KomposeKit/issues) · [📖 Full Docs](https://github.com/YourGithubHandle/KomposeKit/wiki)

</div>
