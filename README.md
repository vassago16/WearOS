# Wind Wear OS

A first Wear OS weather app built to show the live wind conditions for the user’s current location. It displays sustained wind, gusts, and wind direction in a compact, watch-friendly interface and includes complications for quick glance information.

## Overview

This project is a practical first version of a Wear OS app focused on a useful, glanceable experience:

- current wind speed
- current gust speed
- wind direction
- location-aware weather lookup
- cached fallback behavior when the device is offline or the location is stale
- watch complications for quick status checks

## Features

- Live weather data from Open-Meteo
- Location-based fetch using Android location APIs
- Permission-aware behavior for coarse/fine location access
- Cached weather and cached last-known location fallback
- Compose-based Wear OS UI
- Complications for:
  - sustained wind
  - gusts
  - summary view
- Lightweight persistent cache using SharedPreferences

## Tech Stack

- Kotlin
- Jetpack Compose for Wear OS
- Android Gradle Plugin
- Google Play Services Location
- Retrofit
- Moshi
- Kotlin Coroutines
- SharedPreferences

## Requirements

- Android Studio
- JDK 17
- Android SDK 35
- Wear OS emulator image or physical Wear OS device

## Setup

1. Install Android Studio.
2. Install JDK 17.
3. Open the project folder in Android Studio.
4. Let Gradle sync finish.
5. Open SDK Manager and install the required Android packages:
   - Android 35 platform
   - Android Emulator
   - Wear OS system image
6. Create or select a Wear OS AVD.
7. Run the app from Android Studio.

## Local configuration

Android Studio usually manages the SDK automatically, but if needed, make sure your local SDK path is defined in `local.properties`:

```properties
sdk.dir=C:\Users\<your-user>\AppData\Local\Android\Sdk
```

## Permissions

The app requests location permission so it can fetch weather based on the current device position.

## API

Weather data is provided by the Open-Meteo forecast API.

## Project Structure

```text
Wear-OS/
├── app/
│   ├── src/main/java/com/example/wind/
│   │   ├── complication/
│   │   ├── data/
│   │   ├── ui/
│   │   └── util/
│   └── src/main/res/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── README.md
└── .gitignore
```

## Notes

This project is meant as a first wearable app prototype and learning project. It demonstrates how to combine watch UI, location, API data, caching, and complications into a compact Wear OS experience.

## License

This project is provided for learning and experimentation. Use and adapt it as needed for personal or educational projects.
