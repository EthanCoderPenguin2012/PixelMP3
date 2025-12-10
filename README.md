# 🎵 PixelMP3

> **Feel the music, not the interface**

[![Build Status](https://github.com/EthanCoderPenguin2012/PixelMP3/workflows/Android%20CI/badge.svg)](https://github.com/EthanCoderPenguin2012/PixelMP3/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Android-34+-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20+-purple.svg)](https://kotlinlang.org)

A next-generation Android audio player for **mobile** and **Wear OS**, built with Jetpack Compose and Material 3 Expressive design. Experience audio playback reimagined with vibrant animations, fluid motion, and delightful interactions.

**Quick Links:** [Features](#-features) • [Technology](#-technology-stack) • [Building](#-building-the-project) • [Architecture](#-architecture) • [Documentation](#-documentation)

---

## ✨ What Makes PixelMP3 Special

PixelMP3 isn't just another music player—it's a celebration of what modern Android UI can be. Every interaction is crafted to feel **alive, responsive, and joyful**.

### 🎨 Massively Expressive Design

Built from the ground up with Material 3 Expressive principles and vibrant animations:

- **🌈 Animated Gradients** - Multi-color gradients that continuously shift and flow throughout the entire app
- **💎 Glassmorphism** - Frosted glass surfaces with sophisticated depth, transparency, and layering effects
- **✨ Particle Systems** - Atmospheric floating particles that bring empty states and celebrations to life
- **🎊 Celebration Effects** - Physics-based confetti bursts, sparkles, and heart animations for special moments
- **🌊 Spring Physics** - Natural, bouncy animations powered by spring dynamics on every interaction
- **💫 Living Interfaces** - Every screen pulses, shimmers, spins, and reacts to create an engaging experience

### 🎯 Design Philosophy

> *"The best interface is one you feel, not think about."*

**Our Priorities:**

- **Expressive over minimal** - Bold colors (purple, pink, teal), vibrant motion, delightful micro-interactions
- **Playful over serious** - Bouncy spring animations, celebration effects, whimsical empty states
- **Responsive over static** - Everything animates—buttons scale, lists cascade, gradients flow
- **Immersive over functional** - Beautiful particles and effects make every moment feel special

### 🎨 Color Palette

Material 3 Expressive colors that bring the app to life:

- **Primary Purple**: `#7B5FFF` (light) / `#BBA4FF` (dark) - Main brand color
- **Secondary Pink**: `#FF6B9D` (light) / `#FFB1CC` (dark) - Accent and highlights  
- **Tertiary Teal**: `#00BFA5` (light) / `#4FDAC6` (dark) - Success and active states

---

## 🚀 Features

### 📱 Mobile App

#### Core Audio Features

- 🎵 **Local Playback** - Browse and play MP3/audio files from device storage
- 🎧 **Immersive Now Playing** - Full-screen player with animated gradients and spinning album art
- 🔄 **Background Playback** - Continue listening with foreground service and notification controls
- 📋 **Playlist Management** - Create, edit, and organize your music collections
- ⌚ **Wear OS Sync** - Seamlessly transfer songs to your smartwatch for offline playback
- 🔊 **Media Controls** - Play, pause, skip, seek, and volume control

#### Visual & Animation Features

- 🌈 **Continuous Gradient Animation** - Multi-color gradients flow throughout the app using `animateColor` and `infiniteTransition`
- 💎 **Glassmorphic Cards** - Frosted glass effect with `BlurredCard` component and backdrop blur
- ✨ **Particle Effects** - Floating particles for empty states using Canvas-based `ParticleEffect` composable
- 🎊 **Confetti Celebrations** - Physics-based confetti with `ConfettiEffect` for achievements
- 💖 **Heart Burst Animation** - Radiating particles with `HeartBurstEffect` for favorites
- ⭐ **Sparkle Effects** - Twinkling stars with `SparkleEffect` for magical moments
- 🌊 **Ripple Waves** - Expanding circles with `RippleWaveEffect` for active playback
- 💫 **Shimmer Loading** - Smooth gradient shimmer with `ShimmerLoadingCard`
- 🎭 **Interactive Press Animations** - Scale and elevation changes on all buttons and cards
- 📜 **Staggered List Animations** - Items cascade in with `animateItemPlacement` and delays
- 🔄 **Spinning Album Art** - Continuous rotation with `infinitelyAnimatedRotation` modifier
- 💓 **Pulsing Icons** - Empty state icons pulse with `animateFloatAsState` to draw attention
- 🎪 **Morphing Shapes** - Smooth transitions between shapes using `androidx.graphics.shapes.Morph`

#### UI Components

All components use centralized animation specs from `AnimationSpec.kt`:

- `bouncySpring` - Playful UI elements (dampingRatio: 0.6f)
- `smoothSpring` - Subtle movements (dampingRatio: 0.8f)
- `fastBouncySpring` - Interactive elements (dampingRatio: 0.5f)
- `rotationSpec` - Spinning elements
- `fadeInSpec` / `fadeOutSpec` - Opacity transitions

### ⌚ Wear OS App

- 🎧 **Offline Playback** - Listen to music without your phone nearby
- 📲 **One-Tap Sync** - Transfer songs from mobile using Wearable Data Layer API
- 🔋 **Battery Optimized** - Efficient ExoPlayer-based playback for all-day listening
- 💫 **Material 3 for Wear** - Built with `androidx.wear.compose:compose-material3`
- 🎵 **Wrist Controls** - Full playback control with circular UI navigation
- ⌚ **Complication Support** - Quick access from watch face (future)

---

## 🏗 Architecture

### Module Structure

PixelMP3 uses a multi-module architecture for code organization and build optimization:

```dir
PixelMP3/
├── mobile/          # Android phone application module
├── wear/            # Wear OS smartwatch application module  
└── shared/          # Shared code between mobile and wear
```

#### Module Details

**`mobile/` - Mobile Application**

- Full-featured Android app with Jetpack Compose UI
- Audio playback with Media3 (ExoPlayer)
- Foreground service for background playback
- Wearable Data Layer client for watch communication
- Material 3 Expressive theming with custom animations

**`wear/` - Wear OS Application**

- Lightweight Wear OS app with Compose for Wear
- Offline audio playback with ExoPlayer
- Wearable Data Layer for receiving music from phone
- Material 3 for Wear OS UI components
- Optimized for circular watch screens

**`shared/` - Shared Library**

- Common data models (`AudioFile`, `Playlist`)
- Wearable communication constants (`WearableConstants`)
- Shared utilities and helpers
- No Android dependencies for maximum reusability

### Design Patterns

**MVVM (Model-View-ViewModel)** - Where appropriate for complex screens

**Unidirectional Data Flow** - State flows down, events flow up

**Repository Pattern** - `AudioRepository` for data access abstraction

**Service Architecture** - `AudioPlaybackService` for background playback

**Reactive Programming** - Kotlin Flows for asynchronous data streams

---

## 🛠 Technology Stack

### Core Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9.20 | Primary programming language |
| **Jetpack Compose** | BOM 2023.10.01 | Modern declarative UI framework |
| **Material 3** | 1.2.0 | Design system with Expressive theming |
| **Media3 (ExoPlayer)** | 1.2.0 | Audio playback engine |
| **Compose Animation** | 1.6.0 | Animation APIs and transitions |
| **Wear Compose** | 1.3.0 | Wear OS UI components |

### Key Dependencies

#### Mobile Module

```gradle
// Compose
implementation(platform("androidx.compose:compose-bom:2023.10.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3:1.2.0")
implementation("androidx.compose.material:material-icons-extended:1.6.8")

// Media Playback
implementation("androidx.media3:media3-exoplayer:1.2.0")
implementation("androidx.media3:media3-session:1.2.0")
implementation("androidx.media:media:1.7.0")

// Shape Morphing
implementation("androidx.graphics:graphics-shapes:1.0.0-alpha05")

// Wearable Communication
implementation("com.google.android.gms:play-services-wearable:18.1.0")

// Core Android
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.activity:activity-compose:1.8.2")
```

#### Wear Module

```gradle
// Wear Compose
implementation("androidx.wear.compose:compose-material3:1.0.0-alpha15")
implementation("androidx.wear.compose:compose-foundation:1.3.0")

// Media Playback
implementation("androidx.media3:media3-exoplayer:1.2.0")
implementation("androidx.media3:media3-session:1.2.0")

// Wearable
implementation("com.google.android.gms:play-services-wearable:18.1.0")
```

### Animation System

**Centralized Animation Specifications** (`AnimationSpec.kt`)

All animations use predefined spring specs for consistency:

```kotlin
// Bouncy spring for playful interactions
val bouncySpring = spring<Float>(
    dampingRatio = Spring.DampingRatioMediumBouncy,  // 0.6f
    stiffness = Spring.StiffnessMedium                // 1500f
)

// Smooth spring for subtle movements  
val smoothSpring = spring<Float>(
    dampingRatio = Spring.DampingRatioLowBouncy,     // 0.8f
    stiffness = Spring.StiffnessMedium
)

// Fast bouncy spring for interactive elements
val fastBouncySpring = spring<Float>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessHigh                  // 10000f
)
```

**Custom Composables** (`AnimatedComponents.kt`)

Reusable animated components:

- `AnimatedCard` - Cards with press and hover animations
- `BouncyButton` - Buttons with spring-based scale animations
- `StaggeredList` - Lists with cascading item animations
- `infinitelyAnimatedRotation()` - Continuous rotation modifier
- `animatedGradientBrush()` - Flowing gradient backgrounds

**Effect Components** (`ExpressiveComponents.kt`, `AdvancedEffects.kt`)

- `AnimatedGradientBackground` - Continuous color-shifting backgrounds
- `BlurredCard` - Glassmorphic frosted glass cards
- `ParticleEffect` - Canvas-based floating particle system
- `ConfettiEffect` - Physics-based confetti celebration
- `SparkleEffect` - Twinkling star particles
- `HeartBurstEffect` - Radiating heart particles
- `RippleWaveEffect` - Expanding circular waves
- `ShimmerLoadingCard` - Gradient shimmer for loading states
- `MorphingAnimation` - Shape morphing with androidx.graphics.shapes

---

## 🔧 Building the Project

### Prerequisites

**Required:**

- [Android Studio](https://developer.android.com/studio) Hedgehog (2023.1.1) or later
- JDK 17 (bundled with Android Studio)
- Android SDK 34
- Kotlin Plugin 1.9.20+

**Recommended:**

- Physical Android device or emulator (API 24+)
- Wear OS emulator or watch (API 30+) for testing Wear features
- At least 8 GB RAM for smooth development experience

### Setup Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/EthanCoderPenguin2012/PixelMP3.git
   cd PixelMP3
   ```

2. **Open in Android Studio**

   - Launch Android Studio
   - Select "Open" and navigate to the cloned `PixelMP3` folder
   - Wait for Gradle sync to complete (may take a few minutes on first run)

3. **Sync Gradle**

   If Gradle doesn't sync automatically:
   - Click "File" → "Sync Project with Gradle Files"
   - Or click the elephant icon in the toolbar

### Building

#### Command Line

**Build All Modules:**

```bash
./gradlew build
```

**Build Specific Module:**

```bash
# Mobile app
./gradlew :mobile:assembleDebug
./gradlew :mobile:assembleRelease

# Wear OS app  
./gradlew :wear:assembleDebug
./gradlew :wear:assembleRelease

# Shared library
./gradlew :shared:assembleDebug
```

**Run Tests:**

```bash
# All tests
./gradlew test

# Module-specific tests
./gradlew :mobile:test
./gradlew :wear:test
./gradlew :shared:test
```

**Lint Checks:**

```bash
./gradlew lint
```

**Clean Build:**

```bash
./gradlew clean build
```

#### Android Studio

**Mobile App:**

1. Select "mobile" from the run configuration dropdown
2. Click the green "Run" button or press `Shift+F10`
3. Choose your target device

**Wear OS App:**

1. Select "wear" from the run configuration dropdown
2. Ensure Wear OS emulator is running or watch is connected
3. Click "Run"

### Build Configurations

**Debug Build:**

- Logging enabled
- No code obfuscation
- Faster build times
- Suitable for development

**Release Build:**

- ProGuard/R8 code shrinking and obfuscation
- Optimized for performance
- Smaller APK size
- Requires signing for distribution

### Troubleshooting

**Gradle Sync Failed:**

- Check your internet connection (downloads dependencies)
- Try "File" → "Invalidate Caches / Restart"
- Update Android Studio to the latest version

**Build Errors:**

- Ensure you have Android SDK 34 installed
- Check that Kotlin plugin is version 1.9.20+
- Clean and rebuild: `./gradlew clean build`

**Emulator Issues:**

- Allocate at least 2 GB RAM to emulator
- Enable hardware acceleration (HAXM/KVM)
- Use latest system images

---

## 📱 Permissions

### Mobile App

**Required Permissions:**

```xml
<!-- Audio file access -->
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />

<!-- Background playback -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />
<uses-permission android:name="android.permission.WAKE_LOCK" />

<!-- Wear OS communication -->
<uses-permission android:name="android.permission.INTERNET" />
```

**Permission Handling:**

- Runtime permissions requested on first launch
- Clear rationale provided to users
- Graceful degradation if permissions denied

### Wear OS App

**Required Permissions:**

```xml
<!-- Playback -->
<uses-permission android:name="android.permission.WAKE_LOCK" />

<!-- Haptic feedback -->
<uses-permission android:name="android.permission.VIBRATE" />
```

---

## 🎓 How It Works

### Audio Playback

**Architecture:**

1. **AudioRepository** - Scans device storage for audio files using MediaStore
2. **AudioPlaybackService** - Foreground service manages Media3 ExoPlayer
3. **PlaybackManager** - Coordinates playback state and UI updates
4. **Notification** - Media-style notification with playback controls

**Flow:**

```flowchart-text
User Interaction → ViewModel → Repository → Service → ExoPlayer → Audio Output
                      ↓
                 UI Updates (StateFlow)
```

### Wear OS Sync

**Synchronization Process:**

1. **Mobile**: User selects songs to transfer
2. **Transfer**: Data sent via Wearable Data Layer API
3. **Wear**: Watch receives and stores audio files locally
4. **Playback**: Watch plays music independently offline

**Implementation:**

- `WearDataService` handles communication
- `MessageClient` for command messages
- `DataClient` for file transfers
- `CapabilityClient` for device discovery

### Animation System (WearOS & Mobile)

**Centralized Specifications:**

All animations source from `AnimationSpec.kt`:

```kotlin
// Example: Bouncy button press
Box(
    modifier = Modifier
        .graphicsLayer {
            scaleX = if (pressed) 0.95f else 1f
            scaleY = if (pressed) 0.95f else 1f
        }
        .animateContentSize(animationSpec = bouncySpring)
)
```

**Gradient Animation:**

```kotlin
val infiniteTransition = rememberInfiniteTransition()
val color1 by infiniteTransition.animateColor(
    initialValue = primary,
    targetValue = secondary,
    animationSpec = infiniteRepeatable(
        tween(3000, easing = LinearEasing),
        RepeatMode.Reverse
    )
)
```

**Particle System:**

Custom Canvas-based rendering:

```kotlin
Canvas(modifier) {
    particles.forEach { particle ->
        drawCircle(
            color = particle.color,
            radius = particle.size,
            center = Offset(particle.x, particle.y),
            alpha = particle.alpha
        )
    }
}
```

---

## 📚 Documentation

### Core Documentation

- **[ANIMATIONS.md](ANIMATIONS.md)** - Complete animation system reference
  - Animation specifications and timing
  - Reusable animated components
  - Custom modifier examples
  - Performance optimization tips

- **[UI_EXPRESSIVENESS.md](UI_EXPRESSIVENESS.md)** - Comprehensive UI/UX guide
  - Gradient animation techniques
  - Particle effect implementations
  - Glassmorphism and depth effects
  - Celebration and feedback animations

- **[SHOWCASE.md](SHOWCASE.md)** - Visual showcase
  - Before/after comparisons
  - Feature demonstrations
  - Animation examples
  - Design evolution

- **[QOL_IMPROVEMENTS.md](QOL_IMPROVEMENTS.md)** - Quality of life features
  - User experience enhancements
  - Performance optimizations
  - Accessibility improvements
  - Future roadmap

### Additional Resources

- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Technical implementation details
- **GitHub Copilot Instructions** - `.github/copilot-instructions.md` - Project coding standards

---

## 🗂 Project Structure

```dir
PixelMP3/
├── .github/
│   ├── workflows/              # CI/CD GitHub Actions
│   │   ├── android-build.yml   # Main CI workflow
│   │   ├── release.yml         # Release builds
│   │   └── lint.yml            # Code quality checks
│   └── copilot-instructions.md # Development guidelines
│
├── mobile/                     # 📱 Mobile Application Module
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/pixelmp3/mobile/
│       │   ├── MainActivity.kt
│       │   ├── ui/
│       │   │   ├── PixelMP3App.kt          # Main app composable
│       │   │   ├── animations/
│       │   │   │   ├── AnimationSpec.kt    # Centralized animation specs
│       │   │   │   ├── AnimatedComponents.kt # Reusable animated components
│       │   │   │   └── ShapeMorph.kt       # Shape morphing animations
│       │   │   ├── components/
│       │   │   │   ├── ExpressiveComponents.kt  # Gradients, glass, particles
│       │   │   │   ├── AdvancedEffects.kt       # Confetti, sparkles, hearts
│       │   │   │   ├── NowPlayingBar.kt         # Immersive playback UI
│       │   │   │   └── LoadingIndicators.kt     # Shimmer and loading states
│       │   │   ├── screens/
│       │   │   │   ├── HomeScreen.kt       # Main music library
│       │   │   │   ├── PlaylistScreen.kt   # Playlist management
│       │   │   │   └── NowPlayingScreen.kt # Full-screen player
│       │   │   └── theme/
│       │   │       ├── Color.kt            # Material 3 color schemes
│       │   │       ├── Theme.kt            # Theme configuration
│       │   │       ├── Typography.kt       # Expressive typography
│       │   │       └── Shape.kt            # Rounded corner shapes
│       │   ├── data/
│       │   │   └── AudioRepository.kt      # Data access layer
│       │   ├── service/
│       │   │   ├── AudioPlaybackService.kt # Background playback
│       │   │   └── WearDataService.kt      # Wear OS communication
│       │   └── util/
│       │       └── PlaybackManager.kt      # Playback coordination
│       └── res/
│           ├── values/
│           │   ├── strings.xml
│           │   └── themes.xml
│           └── drawable/
│
├── wear/                       # ⌚ Wear OS Application Module
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/pixelmp3/wear/
│       │   ├── MainActivity.kt
│       │   ├── ui/
│       │   │   ├── WearApp.kt              # Main watch app
│       │   │   ├── MusicListScreen.kt      # Song list for watch
│       │   │   └── theme/
│       │   │       ├── Theme.kt
│       │   │       └── Typography.kt
│       │   └── service/
│       │       └── WearPlaybackService.kt  # Watch playback service
│       └── res/
│
├── shared/                     # 📦 Shared Library Module
│   ├── build.gradle.kts
│   └── src/main/
│       └── java/com/pixelmp3/shared/
│           ├── model/
│           │   ├── AudioFile.kt            # Audio file data model
│           │   └── Playlist.kt             # Playlist data model
│           └── communication/
│               └── WearableConstants.kt    # Wear API constants
│
├── docs/                       # 🌐 Website
│   └── index.html             # Project website (Material 3 Expressive)
│
├── gradle/                     # Gradle wrapper files
├── build.gradle.kts           # Root build configuration
├── settings.gradle.kts        # Module configuration
├── gradle.properties          # Gradle properties
│
├── README.md                  # 👋 You are here
├── ANIMATIONS.md              # Animation system docs
├── UI_EXPRESSIVENESS.md       # UI/UX comprehensive guide
├── SHOWCASE.md                # Visual showcase
├── QOL_IMPROVEMENTS.md        # Quality of life features
└── IMPLEMENTATION_SUMMARY.md  # Technical implementation
```

---

## 🤝 Contributing

Contributions are welcome! Whether it's bug fixes, new features, or documentation improvements.

### How to Contribute

1. **Fork the Repository**

   Click the "Fork" button at the top right of the repository page.

2. **Create a Feature Branch**

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Your Changes**

   - Follow the coding style in `.github/copilot-instructions.md`
   - Use centralized animation specs from `AnimationSpec.kt`
   - Write clear commit messages
   - Add documentation for new features

4. **Test Your Changes**

   ```bash
   ./gradlew test
   ./gradlew lint
   ```

5. **Submit a Pull Request**

   - Push to your fork
   - Create a PR with a clear description
   - Reference any related issues

### Coding Standards

- **Kotlin Style**: Follow official Kotlin coding conventions
- **Compose Best Practices**: Small, focused composables with hoisted state
- **Animations**: Always use specs from `AnimationSpec.kt`
- **Comments**: KDoc for public APIs, inline for complex logic
- **Architecture**: MVVM pattern, unidirectional data flow

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

```license-MIT
MIT License

Copyright (c) 2025 PixelMP3

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Acknowledgments

- **Material Design Team** - For Material 3 Expressive design system
- **Jetpack Compose Team** - For the amazing declarative UI framework
- **ExoPlayer Team** - For robust media playback capabilities
- **Android Community** - For inspiration and best practices

---

## 📞 Contact & Support

- **Issues**: [GitHub Issues](https://github.com/EthanCoderPenguin2012/PixelMP3/issues)
- **Discussions**: [GitHub Discussions](https://github.com/EthanCoderPenguin2012/PixelMP3/discussions)
- **Website**: [PixelMP3 Docs](https://ethancoderpenguin2012.github.io/PixelMP3/)

---

<div align="center">

**Made with 💜 using Material 3 Expressive**

*Vibrant Purple • Playful Pink • Teal Accents*

[⬆ Back to Top](#-pixelmp3)

</div>
