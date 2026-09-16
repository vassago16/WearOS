package com.example.wind.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationRepository(private val context: Context) {
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    companion object {
        val OVERLAND_PARK_KS = LocationCandidate(
            latitude = 38.9822,
            longitude = -94.6708,
        )
    }

    suspend fun getCurrentLocationOrCached(): LocationCandidate? {
        if (!hasLocationPermission()) {
            return OVERLAND_PARK_KS
        }

        return try {
            val location = fusedLocationClient.lastLocation.await()
            if (location != null) {
                LocationCandidate(location.latitude, location.longitude)
            } else {
                OVERLAND_PARK_KS
            }
        } catch (_: SecurityException) {
            OVERLAND_PARK_KS
        } catch (_: Exception) {
            OVERLAND_PARK_KS
        }
    }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }
}

data class LocationCandidate(
    val latitude: Double,
    val longitude: Double
)
