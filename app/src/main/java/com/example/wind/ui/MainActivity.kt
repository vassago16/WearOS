package com.example.wind.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wind.data.LocationRepository
import com.example.wind.data.WeatherRepository

class MainActivity : ComponentActivity() {
    private val weatherRepository by lazy { WeatherRepository(applicationContext) }
    private val locationRepository by lazy { LocationRepository(applicationContext) }
    private val viewModel by lazy { WindViewModel(weatherRepository, locationRepository) }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { _ ->
        viewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WindScreen(
                    viewModel = viewModel,
                ) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }
            }
        }

        if (!locationRepository.hasLocationPermission()) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        } else {
            viewModel.refresh()
        }
    }
}

@Composable
fun WindScreen(
    viewModel: WindViewModel,
    onRequestPermission: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val errorText = state.errorMessage
    val permissionRequired = errorText == "Grant location permission to refresh weather."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("WIND", fontWeight = FontWeight.Bold)
        Text("${state.sustainedMph?.toInt() ?: "--"} MPH")
        Text(state.directionText)
        Text(state.arrowText)
        Text("GUST ${state.gustMph?.toInt() ?: "--"} MPH")
        Text("Updated ${state.lastUpdatedText}")
        errorText?.let { Text(it) }
        if (state.isStale) {
            Text("STALE")
        }

        Button(
            onClick = {
                if (permissionRequired) {
                    onRequestPermission()
                } else {
                    viewModel.refresh()
                }
            }
        ) {
            Text("Refresh")
        }
    }
}
