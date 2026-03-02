<div align="center">

# ⚡ KomposeKit

### Modern, animated UI components for Jetpack Compose — built for real apps.

<br/>

<img src="https://github.com/Aakash898/KomposeKit/blob/main/tmp_483ee618-27a9-4211-999e-c2ea8b703d50.jpeg?raw=true" width="85%" />
<br/><br/>
<img src="https://github.com/Aakash898/KomposeKit/blob/85f47458c58db089e27ca30cce3aa7e535978377/tmp_a37dd5c8-aa07-4b4d-97f9-8d65d9239584.jpeg" width="85%" />

<br/><br/>

[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6+-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen?style=for-the-badge)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=for-the-badge)](LICENSE)
[![JitPack](https://img.shields.io/badge/JitPack-1.0.0-FF6584?style=for-the-badge)](https://jitpack.io)

<br/>

> Beautifully animated toggles, buttons, sliders, chips, ratings, cards and more —  
> **Fully customizable · Spring-animated · Haptic-ready · Accessible**

<br/>

```
⚡ 15+ Components      🎨 5 Preset Themes
🌊 Natural Spring Motion   📳 Built-in Haptics
🔧 Deep Customization      ♿ Accessibility Friendly
```

</div>

---

# ✨ Why KomposeKit?

Most Compose UI libraries give you:

- ❌ Basic Material clones  
- ❌ Over-designed Dribbble-only concepts  
- ❌ Limited customization  
- ❌ No animation depth  

**KomposeKit is different.**

It is built for production apps that need:

- Smooth spring physics animations  
- Clean and predictable API  
- Sensible defaults  
- Deep theming control  
- Modern visual styles (Neon, Glass, Gradient, Tonal)  
- Accessibility support  

Designed to feel premium — without becoming hard to use.

---

# 🧩 Components

| Component | Styles | Highlights |
|------------|--------|------------|
| `KomposeToggle` | 6 | Icons, labels, haptics |
| `KomposeToggleGroup` | Segmented | Spring selector |
| `KomposeButton` | 6 | Loading state, full-width |
| `KomposePulseButton` | FAB | Infinite pulse ring |
| `KomposeIconButton` | 6 | Bounce animation |
| `KomposeChip` | 4 | Selection animation |
| `KomposeCheckbox` | 4 | Animated check draw |
| `KomposeRadio` | — | Spring dot |
| `KomposeSlider` | — | Steps + gradient track |
| `KomposeRating` | — | Half-star support |
| `KomposeBadge` | — | Pop animation |
| `KomposeCard` | 5 | Press + glass effects |
| `KomposeProgressBar` | — | Smooth animated fill |
| `KomposeDivider` | — | Optional label |
| `KomposeTag` | — | Status labels |

---

# 🚀 Installation

## 1️⃣ Add JitPack

In your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

## 2️⃣ Add Dependency

In your module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.YourGithubHandle:KomposeKit:1.0.0")
}
```

Sync and you're ready.

---

# ⚡ Quick Start

Wrap your UI with `KomposeTheme`:

```kotlin
KomposeTheme {
    var checked by remember { mutableStateOf(false) }

    KomposeToggle(
        checked = checked,
        onCheckedChange = { checked = it }
    )
}
```

Done.

---

# 🎛 KomposeToggle

The star of the library.

**6 visual styles · spring motion · icons · haptic feedback**

```kotlin
var checked by remember { mutableStateOf(false) }

KomposeToggle(
    checked = checked,
    onCheckedChange = { checked = it },
    style = ToggleStyle.Neon,
    colorOn = Color(0xFF00E676),
    size = KomposeSize.Large
)
```

### Available Styles

- `Pill`
- `Squircle`
- `Neon`
- `Glass`
- `Outlined`
- `Minimal`

---

# 🔘 KomposeToggleGroup

Segmented control with animated selector:

```kotlin
var selected by remember { mutableStateOf(0) }

KomposeToggleGroup(
    options = listOf("Daily", "Weekly", "Monthly"),
    selectedIndex = selected,
    onOptionSelected = { selected = it }
)
```

Supports icons and size variants.

---

# 🎯 KomposeButton

6 styles + loading state + spring press animation:

```kotlin
KomposeButton(
    text = "Continue",
    style = ButtonStyle.Neon,
    icon = Icons.Rounded.ArrowForward,
    onClick = { }
)
```

### Styles

- `Gradient`
- `Elevated`
- `Ghost`
- `Tonal`
- `Neon`
- `Glass`

---

# 🏷 KomposeChip

```kotlin
KomposeChip(
    label = "Trending",
    selected = selected,
    onSelectionChange = { selected = it },
    leadingIcon = Icons.Rounded.Whatshot,
    style = ChipStyle.Tonal
)
```

---

# ⭐ KomposeRating

```kotlin
KomposeRating(
    rating = rating,
    onRatingChange = { rating = it },
    maxStars = 5,
    allowHalf = true,
    showLabel = true
)
```

---

# 🎨 Themes

KomposeKit ships with 5 built-in themes:

```kotlin
KomposeTheme() // Dark (default)

KomposeTheme(theme = KomposeKit.LightTheme)
KomposeTheme(theme = KomposeKit.CyberTheme)
KomposeTheme(theme = KomposeKit.SunsetTheme)
KomposeTheme(theme = KomposeKit.OceanTheme)
```

## Custom Theme

```kotlin
KomposeTheme(
    theme = KomposeThemeData(
        colors = KomposeColors(
            primary = Color(0xFFE91E63),
            secondary = Color(0xFFFF5722),
            surface = Color(0xFF12121F)
        )
    )
) {
    // Your UI
}
```

---

# 🃏 KomposeCard

```kotlin
KomposeCard(
    style = CardStyle.Glass,
    onClick = { }
) {
    Text("Frosted Glass Card", color = Color.White)
}
```

---

# 📦 Project Structure

```
KomposeKit/
 ├── library/
 │    ├── KomposeTheme.kt
 │    ├── KomposeToggle.kt
 │    ├── KomposeButton.kt
 │    ├── KomposeInputs.kt
 │    ├── KomposeExtras.kt
 │    └── KomposeCard.kt
 └── app/  → Showcase sample app
```

---

# 🤝 Contributing

```bash
git clone https://github.com/YourGithubHandle/KomposeKit.git
```

PRs are welcome.

Before adding major components, please open an issue to discuss API design first.

---

# 📜 License

```
Copyright 2024 KomposeKit Contributors

Licensed under the Apache License, Version 2.0
```

---

<div align="center">

### ⭐ If KomposeKit saves you time, consider starring the repo.
It genuinely helps the project grow.

</div>
