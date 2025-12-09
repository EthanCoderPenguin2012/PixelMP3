# Animation System Documentation

## Overview

PixelMP3 features a comprehensive animation system built with Jetpack Compose that provides smooth, bouncy, and delightful user interactions. The animation system is designed to be consistent, performant, and easy to use throughout the app.

## Animation Specifications

All animations use centralized specifications defined in `AnimationSpec.kt`:

### Spring Animations

- **bouncySpring**: Medium bouncy spring with low stiffness - perfect for UI elements that need a playful feel
- **smoothSpring**: No-bounce spring with medium stiffness - for subtle, professional movements
- **fastBouncySpring**: Fast bouncy spring - ideal for interactive elements like buttons

### Rotation Animations

- **rotationSpec**: Smooth 3-second rotation - for continuous spinning effects
- **fastRotationSpec**: Fast 1.5-second rotation - for active playback indicators

### Fade Animations

- **fadeInSpec**: 300ms fade-in with FastOutSlowIn easing
- **fadeOutSpec**: 200ms fade-out with FastOutLinear easing

### Slide Animations

- **slideInFromBottom**: Slide in from bottom with fade
- **slideOutToTop**: Slide out to top with fade

## Animated Components

### AnimatedCard

A card that bounces in when it appears with scale and fade animations.

```kotlin
AnimatedCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = { /* handle click */ }
) {
    // Card content
}
```

### SpinningIcon

An icon that rotates continuously - great for loading states or music playback.

```kotlin
SpinningIcon(
    icon = { Icon(Icons.Filled.Sync, contentDescription = null) },
    isSpinning = true
)
```

### PulsingIcon

An icon that scales up and down rhythmically.

```kotlin
PulsingIcon(
    icon = { Icon(Icons.Filled.MusicNote, contentDescription = null) },
    isPulsing = true
)
```

### BouncyButton

A button with satisfying bounce animation on press.

```kotlin
BouncyButton(
    onClick = { /* handle click */ }
) {
    Text("Click Me!")
}
```

### AnimatedFloatingActionButton

A FAB that bounces in after a short delay.

```kotlin
AnimatedFloatingActionButton(
    onClick = { /* handle click */ },
    icon = { Icon(Icons.Filled.Add, contentDescription = null) }
)
```

## Loading Indicators

### BouncingDotsLoader

Three dots that bounce in sequence.

```kotlin
BouncingDotsLoader(
    dotColor = MaterialTheme.colorScheme.primary
)
```

### SpinningLoader

A circular progress indicator with rotation.

```kotlin
SpinningLoader(
    color = MaterialTheme.colorScheme.primary
)
```

### PulsingLoader

A circle that pulses with scale and opacity changes.

```kotlin
PulsingLoader(
    color = MaterialTheme.colorScheme.primary
)
```

### LoadingOverlay

A full-screen loading overlay with message.

```kotlin
LoadingOverlay(
    message = "Loading your music..."
)
```

## Screen Transitions

Screen navigation uses `AnimatedContent` with slide transitions:

- Screens slide in from the right
- Previous screens slide out to the left
- 300ms duration with FastOutSlowIn easing

## List Animations

List items use staggered animations:

1. Each item slides in from the bottom
2. Items are delayed by 50ms each
3. Creates a cascading effect
4. Combines slide + fade for smooth appearance

## Interactive Animations

### Press States

Buttons and cards respond to press with:
- Scale down to 0.95-0.97
- Elevation increase
- Fast bouncy spring animation
- Auto-reset after 100ms

### Navigation Icons

Selected navigation items scale up to 1.2x with bouncy spring.

### Empty States

Empty state icons pulse continuously to draw attention.

## Color Scheme

Vibrant Material 3 Expressive colors:

- **Primary**: Vibrant purple (#7B5FFF light, #BBA4FF dark)
- **Secondary**: Vibrant pink (#FF6B9D light, #FFB1CC dark)
- **Tertiary**: Vibrant teal (#00BFA5 light, #4FDAC6 dark)

## Performance Considerations

1. All animations use hardware acceleration
2. Infinite animations are properly disposed
3. Animations pause when elements are off-screen
4. Spring animations use efficient damping ratios
5. Transitions use optimized easing curves

## Best Practices

1. **Consistency**: Use AnimationSpec constants for consistent timing
2. **Accessibility**: Animations respect system preferences (TODO: add reduce motion support)
3. **Purpose**: Every animation serves a purpose (feedback, attention, or delight)
4. **Performance**: Test animations on low-end devices
5. **Subtlety**: Don't overuse animations - less is often more

## Future Enhancements

- [ ] Add support for Android's reduce motion accessibility setting
- [ ] Add haptic feedback integration
- [ ] Create more advanced shared element transitions
- [ ] Add sound effects for key interactions
- [ ] Implement gesture-based animations (swipe to dismiss, drag to reorder)
