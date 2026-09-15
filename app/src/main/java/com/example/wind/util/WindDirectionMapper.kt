package com.example.wind.util

object WindDirectionMapper {
    private val directions = listOf(
        "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
        "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"
    )

    fun fromDegrees(degrees: Double): String {
        val normalized = ((degrees % 360) + 360) % 360
        val index = ((normalized / 22.5) + 0.5).toInt() % directions.size
        return directions[index]
    }
}
