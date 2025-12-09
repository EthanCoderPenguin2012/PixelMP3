# PixelMP3

An Android MP3 player app for mobile and Wear OS, built with Jetpack Compose and Material 3 Expressive design featuring **massively expressive UI** with gradients, particles, and advanced animations.

## ✨ What Makes PixelMP3 Special

### Expressive Visual Design
PixelMP3 showcases the future of mobile UI with:
- 🌈 **Animated Gradients**: Multi-color gradients that flow continuously throughout the app
- 💎 **Glassmorphism**: Semi-transparent surfaces with depth and layering
- ✨ **Particle Effects**: Floating particles that create atmospheric, living interfaces
- 🎊 **Celebration Effects**: Confetti, sparkles, and bursts for special moments
- 🎭 **Advanced Animations**: Spring physics, rotations, pulsing, and more

Every screen, every interaction, every moment is thoughtfully animated to create a **joyful, premium audio experience**.

## Features

### Mobile App
- 🎵 **Massively Expressive UI** with Jetpack Compose and Material 3 Expressive design
- ✨ Animated gradients throughout the entire app
- 🌈 Glassmorphic surfaces with depth and layering
- 💫 Particle effects for empty states and celebrations
- 🎊 Confetti and sparkle effects for special moments
- 🎨 Vibrant color scheme with purple, pink, and teal accents
- 📱 Browse and play audio files from your device
- 🎧 Immersive now playing bar with animated gradients
- 🔄 Spinning album art during playback
- 📋 Playlist management with expressive empty states
- ⌚ Transfer audio files to Wear OS watch
- 🌊 Staggered list animations for smooth content loading
- 💎 Interactive press animations on all interactive elements
- ✨ Shimmer loading effects
- 🎭 Enhanced typography with expressive font sizes
- 🔮 Advanced micro-interactions throughout

### Wear OS App
- 🎧 Offline audio playback on your watch
- 📲 Sync music from your phone
- 🔋 Phone-less listening experience
- 💫 Optimized Wear OS UI with Compose for Wear

## Architecture

The project is organized into three modules:

- **mobile**: Android phone app with full UI and audio management
- **wear**: Wear OS app for watch-based playback
- **shared**: Common data models and communication constants

## Technology Stack

- **Jetpack Compose**: Modern UI toolkit for Android
- **Material 3 Expressive**: Latest Material Design with massively enhanced theming
- **Advanced Animations**: 
  - Gradient animations with continuous color shifts
  - Particle effects with physics-based motion
  - Spring-based bouncy animations for delightful interactions
  - Glassmorphism and depth effects
  - Confetti, sparkles, and celebration effects
- **Media3 (ExoPlayer)**: Advanced media playback
- **Wear OS APIs**: For watch communication and data sync
- **Kotlin**: Primary programming language
- **Canvas Drawing**: Custom particle and effect rendering

## Building the Project

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 34
- Kotlin 1.9.20+

### Build Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/EthanCoderPenguin2012/PixelMP3.git
   cd PixelMP3
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run:
   - For mobile app: Select the `mobile` run configuration
   - For Wear OS app: Select the `wear` run configuration

## How It Works

### Animation System

PixelMP3 features a massively expressive animation system that makes the app feel alive and modern:

- **Animated Gradients**: Multi-color gradients that shift and flow continuously throughout the app
- **Glassmorphism**: Semi-transparent surfaces with frosted glass aesthetic for depth
- **Particle Effects**: Floating particles in empty states and celebrations
- **Confetti Celebrations**: Physics-based confetti for achievements and special moments
- **Sparkle Effects**: Twinkling stars for magical moments
- **Heart Bursts**: Radiating particles for likes and favorites
- **Ripple Waves**: Expanding circular waves for active states
- **Shimmer Loading**: Smooth shimmer gradient for loading states
- **Bouncy Springs**: Cards, buttons, and UI elements use spring physics
- **Spinning Elements**: Album art spins during playback
- **Staggered Lists**: List items cascade in with delightful delays
- **Interactive Feedback**: Scale and elevation animations on all interactions
- **Pulsing Icons**: Empty state icons pulse to draw attention
- **Now Playing Bar**: Immersive playback experience with animated gradients

See [ANIMATIONS.md](ANIMATIONS.md) and [UI_EXPRESSIVENESS.md](UI_EXPRESSIVENESS.md) for detailed documentation.

### Syncing Music to Watch

1. **Select Music**: Choose songs from your mobile library
2. **Transfer**: Music is sent to your Wear OS device via Wearable Data Layer API
3. **Play Offline**: Listen on your watch without your phone

## Permissions

### Mobile App
- `READ_MEDIA_AUDIO` / `READ_EXTERNAL_STORAGE`: Access audio files
- `FOREGROUND_SERVICE`: Background audio playback
- `WAKE_LOCK`: Keep device awake during playback

### Wear OS App
- `WAKE_LOCK`: Keep watch awake during playback
- `VIBRATE`: Haptic feedback

## Project Structure

```
PixelMP3/
├── mobile/                 # Android phone app
│   └── src/main/
│       ├── java/com/pixelmp3/mobile/
│       │   ├── ui/        # Compose UI components
│       │   │   ├── animations/     # Animation specs and components
│       │   │   ├── components/     # Reusable UI components
│       │   │   │   ├── ExpressiveComponents.kt   # Gradients, particles
│       │   │   │   ├── AdvancedEffects.kt        # Confetti, sparkles
│       │   │   │   ├── NowPlayingBar.kt          # Playback UI
│       │   │   │   └── LoadingIndicators.kt      # Loading animations
│       │   │   └── theme/          # Material 3 theming
│       │   │       ├── Theme.kt                  # Color schemes
│       │   │       ├── Typography.kt             # Expressive typography
│       │   │       └── Shape.kt                  # Rounded corners
│       │   ├── service/   # Audio and Wear services
│       │   └── MainActivity.kt
│       └── AndroidManifest.xml
├── wear/                   # Wear OS app
│   └── src/main/
│       ├── java/com/pixelmp3/wear/
│       │   ├── ui/        # Wear Compose UI
│       │   ├── service/   # Wear services
│       │   └── MainActivity.kt
│       └── AndroidManifest.xml
├── shared/                 # Shared code
│   └── src/main/
│       └── java/com/pixelmp3/shared/
│           ├── model/     # Data models
│           └── communication/ # Wear API constants
├── ANIMATIONS.md          # Animation system documentation
├── UI_EXPRESSIVENESS.md   # Comprehensive UI guide
├── SHOWCASE.md            # Visual showcase and before/after
└── QOL_IMPROVEMENTS.md    # Quality of life improvements
```

## Documentation

- **[ANIMATIONS.md](ANIMATIONS.md)** - Core animation system and components
- **[UI_EXPRESSIVENESS.md](UI_EXPRESSIVENESS.md)** - Comprehensive guide to expressive UI features
- **[SHOWCASE.md](SHOWCASE.md)** - Visual showcase with before/after comparisons
- **[QOL_IMPROVEMENTS.md](QOL_IMPROVEMENTS.md)** - Quality of life improvements

## License

This project is open source. Feel free to use and modify it for your needs.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.