# Wind Wear OS

A first-generation Wear OS app that shows live wind conditions for the user’s current location, including sustained wind, gusts, and wind direction.

## Features

- Fetches current wind data from Open-Meteo
- Uses current device location when permission is granted
- Falls back to cached location and cached weather if fresh enough
- Shows wind speed, gust, and direction on a Wear OS watch UI
- Includes Wear OS complication providers for:
  - sustained wind
  - gust
  - wind summary
- Supports a lightweight cache to reduce repeated API calls

## Tech stack

- Kotlin
- Jetpack Compose for Wear OS UI
- Android SDK / Gradle
- Google Play Services Location
- Retrofit + Moshi
- Kotlin Coroutines
- SharedPreferences for local cache

## Prerequisites

- Android Studio
- JDK 17
- Android SDK with:
  - Android 35 platform
  - Android Emulator
  - Wear OS system image
- A Wear OS emulator or physical watch

## Setup

1. Install Android Studio.
2. Install JDK 17.
3. Open the project folder in Android Studio.
4. Let Gradle sync complete.
5. In Android Studio, open SDK Manager and make sure the necessary Android SDK packages are installed.
6. Create or select a Wear OS emulator.
7. Run the app from Android Studio.

## Local configuration

The project expects Android SDK configuration in the local environment, usually via Android Studio. If needed, make sure `local.properties` points to your SDK path, for example:

```properties
sdk.dir=C:\Users\<your-user>\AppData\Local\Android\Sdk
```

## Permissions

The app requests location access so it can determine the user’s current position and fetch local weather data.

## API usage

Weather data is pulled from the Open-Meteo forecast API.

## Project structure

```text
app/
  src/main/java/com/example/wind/
    complication/
    data/
    ui/
    util/
  src/main/res/
build.gradle.kts
settings.gradle.kts
gradle.properties
README.md
```

## Notes

This is a simple first Wear OS app intended as a learning and prototype project. It is designed to be a practical starting point for building a watch-first weather experience.

## License

This project is provided as-is for learning and personal experimentation.
