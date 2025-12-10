# GitHub Copilot Instructions for PixelMP3

## Project Overview

PixelMP3 is an Android MP3 player app for mobile and Wear OS, built with Jetpack Compose and Material 3 Expressive design. The project emphasizes fun, bouncy animations and a vibrant, modern user interface.

## Project Structure

The project is organized into three Gradle modules:

- **mobile**: Android phone app with full UI and audio management (`/mobile`)
- **wear**: Wear OS app for watch-based playback (`/wear`)
- **shared**: Common data models and communication constants (`/shared`)

## Technology Stack

- **Language**: Kotlin 1.9.20+
- **UI Framework**: Jetpack Compose with Material 3 Expressive theming
- **Build System**: Gradle with Kotlin DSL (`.gradle.kts` files)
- **Media Playback**: Media3 (ExoPlayer)
- **Animations**: Compose Animation APIs with spring physics
- **Wear OS**: Wear OS APIs for watch communication and data sync
- **Target SDK**: Android 34

## Code Style and Conventions

### Kotlin Style
- Use idiomatic Kotlin: data classes, sealed classes, object declarations
- Prefer immutability and functional programming patterns
- Use KDoc comments (`/**  */`) for public APIs and complex logic
- Package structure: `com.pixelmp3.<module>.<feature>`

### Compose Best Practices
- Keep composable functions small and focused
- Use `remember` for state that survives recomposition
- Use `derivedStateOf` for computed state
- Hoist state when needed for reusability
- Follow Material 3 design guidelines
- Use `Modifier` parameters for layout customization

### Animation Guidelines
- **Always use centralized animation specs** from `AnimationSpec.kt`:
  - `bouncySpring` for playful UI elements
  - `smoothSpring` for subtle movements
  - `fastBouncySpring` for interactive elements
  - `rotationSpec` and `fastRotationSpec` for spinning elements
  - `fadeInSpec` and `fadeOutSpec` for opacity transitions
- Use spring-based animations for natural, physics-based motion
- Apply consistent timing across similar interactions
- See `ANIMATIONS.md` for detailed animation system documentation

### Color Scheme
Use Material 3 Expressive colors defined in the theme:
- **Primary**: Vibrant purple (#7B5FFF light, #BBA4FF dark)
- **Secondary**: Vibrant pink (#FF6B9D light, #FFB1CC dark)
- **Tertiary**: Vibrant teal (#00BFA5 light, #4FDAC6 dark)

## Building and Testing

### Build Commands
```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :mobile:build
./gradlew :wear:build
./gradlew :shared:build

# Clean build
./gradlew clean build
```

### Running the App
- **Mobile app**: Use the `mobile` run configuration in Android Studio
- **Wear OS app**: Use the `wear` run configuration in Android Studio

### Testing
```bash
# Run tests
./gradlew test

# Run tests for specific module
./gradlew :mobile:test
```

## Architecture Patterns

### UI Layer
- Use MVVM pattern where appropriate
- Compose UI components in `ui/components/` directory
- Animated components in `ui/animations/` directory
- Theme definitions in `ui/theme/` directory

### Services
- `AudioPlaybackService`: Foreground service for media playback
- `WearDataService`: Handles Wear OS data synchronization

### Data Layer
- `AudioRepository`: Manages audio file access and metadata
- Use `PlaybackManager` for media playback coordination

## Key Features to Maintain

1. **Bouncy Animations**: All interactive elements should have spring-based animations
2. **Material 3 Expressive**: Follow vibrant color scheme and Material 3 guidelines
3. **Staggered List Animations**: List items should cascade in with delays
4. **Interactive Feedback**: Buttons and cards should respond to press with scale animations
5. **Wear OS Sync**: Music can be transferred to and played on Wear OS devices

## Important Considerations

### Permissions
- Mobile app requires: `READ_MEDIA_AUDIO`, `READ_EXTERNAL_STORAGE`, `FOREGROUND_SERVICE`, `WAKE_LOCK`
- Wear app requires: `WAKE_LOCK`, `VIBRATE`
- Always request permissions properly with appropriate rationale

### Performance
- Animations use hardware acceleration
- Infinite animations must be properly disposed
- Test on low-end devices
- Optimize list rendering with `LazyColumn`/`LazyRow`

### Accessibility
- Provide content descriptions for all icons and images
- Ensure proper contrast ratios
- Future enhancement: Support reduce motion accessibility setting

## Common Tasks

### Adding New Animations
1. Define animation spec in `AnimationSpec.kt` if it's reusable
2. Create animated component in `AnimatedComponents.kt` if it's complex
3. Document in `ANIMATIONS.md` if it's a major feature

### Adding New UI Screens
1. Create composable function in appropriate module
2. Apply consistent animation transitions
3. Use Material 3 components and theme colors
4. Ensure proper state management

### Working with Wear OS
1. Shared models go in `shared` module
2. Communication constants in `shared/communication/`
3. Use Wearable Data Layer API for data sync
4. Test on both phone and watch emulators

## File Locations

- **Animation specs**: `mobile/src/main/java/com/pixelmp3/mobile/ui/animations/AnimationSpec.kt`
- **Animated components**: `mobile/src/main/java/com/pixelmp3/mobile/ui/animations/AnimatedComponents.kt`
- **Loading indicators**: `mobile/src/main/java/com/pixelmp3/mobile/ui/components/LoadingIndicators.kt`
- **Theme**: `mobile/src/main/java/com/pixelmp3/mobile/ui/theme/Theme.kt`
- **Main app**: `mobile/src/main/java/com/pixelmp3/mobile/ui/PixelMP3App.kt`

## Documentation

- Main README: `/README.md`
- Animation system: `/ANIMATIONS.md`
- QoL improvements tracking: `/QOL_IMPROVEMENTS.md`

## Dependencies

Avoid adding new dependencies unless absolutely necessary. Current key dependencies include:
- Jetpack Compose BOM
- Material 3
- Media3 (ExoPlayer)
- Wear Compose
- AndroidX libraries

When suggesting new features or changes, always:
1. Maintain the fun, bouncy animation style
2. Follow Material 3 Expressive design principles
3. Use existing animation specs and components
4. Keep code modular and testable
5. Consider both mobile and wear platforms if applicable
