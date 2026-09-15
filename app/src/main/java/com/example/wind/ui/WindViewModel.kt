package com.example.wind.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wind.data.LocationRepository
import com.example.wind.data.WeatherRepository
import com.example.wind.data.isWeatherStale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WindViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WindUiState())
    val uiState: StateFlow<WindUiState> = _uiState.asStateFlow()

    fun refresh(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            if (!locationRepository.hasLocationPermission()) {
                _uiState.value = WindUiState(
                    statusText = "Location permission required",
                    lastUpdatedText = "Permission needed",
                    isLoading = false,
                    errorMessage = "Grant location permission to refresh weather.",
                )
                return@launch
            }

            val currentLocation = locationRepository.getCurrentLocationOrCached()
            val cached = weatherRepository.readCachedWeather()

            currentLocation?.let { weatherRepository.writeLastLocation(it) }

            val freshWeather = try {
                weatherRepository.getLatestWeather(forceRefresh, currentLocation)
            } catch (_: Exception) {
                cached
            }

            val resolved = freshWeather ?: cached
            val state = WindUiState.fromSnapshot(
                resolved,
                if ((freshWeather == null) && (cached != null)) "Using cached data" else null,
            )

            _uiState.value = state.copy(
                isLoading = false,
                statusText = if (resolved == null) "No weather available" else if (resolved.isWeatherStale()) "Stale data" else "Updated",
                isStale = resolved?.isStale == true,
                errorMessage = if (resolved == null) "Weather unavailable" else state.errorMessage,
            )
        }
    }
}
