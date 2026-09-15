package com.example.wind.ui

import com.example.wind.data.WeatherSnapshot
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class WindUiState(
    val sustainedMph: Double? = null,
    val directionDegrees: Double? = null,
    val directionText: String = "--",
    val gustMph: Double? = null,
    val lastUpdatedText: String = "No data",
    val isLoading: Boolean = false,
    val isStale: Boolean = false,
    val statusText: String = "Waiting for weather",
    val errorMessage: String? = null,
    val arrowText: String = "↔",
) {
    companion object {
        fun fromSnapshot(snapshot: WeatherSnapshot?, error: String? = null): WindUiState {
            if (snapshot == null) {
                return WindUiState(
                    statusText = "No weather data yet",
                    errorMessage = error,
                    lastUpdatedText = "Not updated",
                )
            }

            val updatedText = DateTimeFormatter.ofPattern("h:mm a")
                .withZone(ZoneId.systemDefault())
                .format(snapshot.observedAt)

            return WindUiState(
                sustainedMph = snapshot.sustainedMph,
                directionDegrees = snapshot.directionDegrees,
                directionText = snapshot.directionText,
                gustMph = snapshot.gustMph,
                lastUpdatedText = updatedText,
                isStale = snapshot.isStale,
                statusText = if (snapshot.isStale) "Stale data" else "Updated",
                errorMessage = error,
                arrowText = arrowForDegrees(snapshot.directionDegrees)
            )
        }

        private fun arrowForDegrees(degrees: Double): String {
            val normalized = ((degrees % 360) + 360) % 360
            val arrows = listOf("↑", "↗", "→", "↘", "↓", "↙", "←", "↖")
            val index = ((normalized / 45.0) + 0.5).toInt() % 8
            return arrows[index]
        }
    }
}
