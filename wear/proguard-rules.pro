# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Wearable API classes
-keep class com.google.android.gms.wearable.** { *; }

# Keep Media3 classes
-keep class androidx.media3.** { *; }

# Keep Wear Compose classes
-keep class androidx.wear.compose.** { *; }
