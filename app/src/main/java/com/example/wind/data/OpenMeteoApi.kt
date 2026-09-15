package com.example.wind.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("forecast")
    suspend fun getCurrentConditions(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "wind_speed_10m,wind_direction_10m,wind_gusts_10m",
        @Query("wind_speed_unit") windSpeedUnit: String = "mph",
        @Query("timezone") timezone: String = "auto"
    ): OpenMeteoResponse
}

@JsonClass(generateAdapter = true)
data class OpenMeteoResponse(
    @Json(name = "current") val current: CurrentWeather?
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "time") val time: String?,
    @Json(name = "wind_speed_10m") val windSpeedMph: Double?,
    @Json(name = "wind_direction_10m") val windDirectionDegrees: Double?,
    @Json(name = "wind_gusts_10m") val windGustMph: Double?
)
