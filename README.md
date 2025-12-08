# PixelMP3

An Android MP3 player app for mobile and Wear OS, built with Jetpack Compose and Material 3 Expressive design.

## Features

### Mobile App
- 🎵 Beautiful Material 3 Expressive UI with Jetpack Compose
- 📱 Browse and play audio files from your device
- 🎨 Modern, intuitive interface
- 📋 Playlist management
- ⌚ Transfer audio files to Wear OS watch

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
- **Material 3**: Latest Material Design with Expressive theming
- **Media3 (ExoPlayer)**: Advanced media playback
- **Wear OS APIs**: For watch communication and data sync
- **Kotlin**: Primary programming language

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
└── shared/                 # Shared code
    └── src/main/
        └── java/com/pixelmp3/shared/
            ├── model/     # Data models
            └── communication/ # Wear API constants
```

## License

This project is open source. Feel free to use and modify it for your needs.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.