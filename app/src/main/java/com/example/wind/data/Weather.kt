package com.example.wind.data

import java.time.Instant

data class WeatherSnapshot(
    val latitude: Double,
    val longitude: Double,
    val sustainedMph: Double,
    val directionDegrees: Double,
    val directionText: String,
    val gustMph: Double,
    val observedAt: Instant,
    val isStale: Boolean = false,
    val source: String = "open-meteo"
)
