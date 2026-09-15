package com.example.wind.data

import android.content.Context
import androidx.core.content.edit
import com.example.wind.util.WindDirectionMapper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Instant
import java.time.format.DateTimeParseException

class WeatherRepository(context: Context) {
    private val prefs = context.getSharedPreferences("wind_weather_cache", Context.MODE_PRIVATE)

    private val api: OpenMeteoApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build(),
            ),
        )
        .build()
        .create(OpenMeteoApi::class.java)

    suspend fun getLatestWeather(forceRefresh: Boolean = false, location: LocationCandidate? = null): WeatherSnapshot? {
        val cached = readCachedWeather()
        if (!forceRefresh && (cached != null) && !cached.isWeatherStale()) {
            return cached
        }

        val resolvedLocation = location ?: readLastLocation() ?: return cached
        return try {
            val response = api.getCurrentConditions(resolvedLocation.latitude, resolvedLocation.longitude)
            val weather = response.current?.let { current ->
                mapCurrentToSnapshot(resolvedLocation.latitude, resolvedLocation.longitude, current)
            }

            if (weather != null) {
                writeCachedWeather(weather)
                weather
            } else {
                cached
            }
        } catch (_: Exception) {
            cached
        }
    }

    fun readCachedWeather(): WeatherSnapshot? {
        val latitude = prefs.getString("latitude", null)?.toDoubleOrNull() ?: return null
        val longitude = prefs.getString("longitude", null)?.toDoubleOrNull() ?: return null
        val sustained = prefs.getString("wind_speed", null)?.toDoubleOrNull() ?: return null
        val directionDegrees = prefs.getString("wind_direction_deg", null)?.toDoubleOrNull() ?: return null
        val gust = prefs.getString("gust_speed", null)?.toDoubleOrNull() ?: return null
        val timeEpoch = prefs.getLong("observed_at_epoch", 0L)
        val directionText = prefs.getString("wind_direction_text", WindDirectionMapper.fromDegrees(directionDegrees)) ?: WindDirectionMapper.fromDegrees(directionDegrees)

        return WeatherSnapshot(
            latitude = latitude,
            longitude = longitude,
            sustainedMph = sustained,
            directionDegrees = directionDegrees,
            directionText = directionText,
            gustMph = gust,
            observedAt = Instant.ofEpochMilli(timeEpoch),
            isStale = false,
        )
    }

    fun writeCachedWeather(weather: WeatherSnapshot) {
        prefs.edit {
            putString("latitude", weather.latitude.toString())
            putString("longitude", weather.longitude.toString())
            putString("wind_speed", weather.sustainedMph.toString())
            putString("wind_direction_deg", weather.directionDegrees.toString())
            putString("wind_direction_text", weather.directionText)
            putString("gust_speed", weather.gustMph.toString())
            putLong("observed_at_epoch", weather.observedAt.toEpochMilli())
        }
    }

    fun readLastLocation(): LocationCandidate? {
        val latitude = prefs.getString("last_location_lat", null)?.toDoubleOrNull() ?: return null
        val longitude = prefs.getString("last_location_lon", null)?.toDoubleOrNull() ?: return null
        return LocationCandidate(latitude, longitude)
    }

    fun writeLastLocation(location: LocationCandidate) {
        prefs.edit {
            putString("last_location_lat", location.latitude.toString())
            putString("last_location_lon", location.longitude.toString())
        }
    }

    private fun mapCurrentToSnapshot(latitude: Double, longitude: Double, current: CurrentWeather): WeatherSnapshot {
        val speed = current.windSpeedMph ?: 0.0
        val directionDegrees = current.windDirectionDegrees ?: 0.0
        val gust = current.windGustMph ?: 0.0
        val directionText = WindDirectionMapper.fromDegrees(directionDegrees)
        val timestamp = parseObservationTime(current.time) ?: Instant.now()

        return WeatherSnapshot(
            latitude = latitude,
            longitude = longitude,
            sustainedMph = speed,
            directionDegrees = directionDegrees,
            directionText = directionText,
            gustMph = gust,
            observedAt = timestamp,
            isStale = false
        )
    }

    private fun parseObservationTime(value: String?): Instant? {
        if (value.isNullOrBlank()) return null
        return try {
            Instant.parse(value)
        } catch (_: DateTimeParseException) {
            null
        }
    }
}

fun WeatherSnapshot.isWeatherStale(): Boolean {
    return (Instant.now().toEpochMilli() - this.observedAt.toEpochMilli()) > (15 * 60 * 1000L)
}
