plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.komposekit"
    compileSdk = 34                          // ✅ stays 34

    defaultConfig {
        applicationId = "com.komposekit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ── Pin BOM to a version compatible with AGP 8.5.2 + compileSdk 34 ──
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))  // ← KEY FIX

    // ── Compose Core ──────────────────────────────────────────────────
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // ── Added for KomposeKit (no versions — BOM manages them) ─────────
    implementation("androidx.compose.material:material")           // rememberRipple
    implementation("androidx.compose.animation:animation")         // animateColorAsState
    implementation("androidx.compose.animation:animation-core")    // spring, tween
    implementation("androidx.compose.foundation:foundation")       // gestures, layout
    implementation("androidx.compose.ui:ui-geometry")              // Offset, CornerRadius
    implementation("androidx.compose.runtime:runtime")             // mutableStateOf
    implementation("androidx.compose.material:material-icons-extended")


    // ── AndroidX — pinned to versions compatible with compileSdk 34 ───
    implementation("androidx.core:core-ktx:1.13.1")               // ← NOT 1.17.0
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")    // ← NOT 1.12.x

    // ── Test ──────────────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}