package com.example.wind.complication

import android.app.PendingIntent
import android.content.Intent
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceService
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import com.example.wind.data.LocationRepository
import com.example.wind.data.WeatherRepository
import com.example.wind.data.WeatherSnapshot
import com.example.wind.ui.MainActivity
import java.time.Instant
import kotlinx.coroutines.runBlocking

abstract class BaseWindComplicationService : ComplicationDataSourceService() {
    protected abstract val label: String
    protected open val showTitle: Boolean = true
    protected abstract val valueSelector: (WeatherSnapshot) -> String

    private val weatherRepository by lazy { WeatherRepository(applicationContext) }

    override fun onComplicationRequest(
        request: ComplicationRequest,
        listener: ComplicationRequestListener,
    ) {
        val location = weatherRepository.readLastLocation() ?: LocationRepository.OVERLAND_PARK_KS
        val snapshot = runBlocking {
            weatherRepository.getLatestWeather(forceRefresh = true, location = location)
                ?: weatherRepository.readCachedWeather()
        }
        val data = buildData(request.complicationType, snapshot)
        listener.onComplicationData(data)
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData {
        val preview = WeatherSnapshot(
            latitude = 0.0,
            longitude = 0.0,
            sustainedMph = 12.0,
            directionDegrees = 225.0,
            directionText = "SW",
            gustMph = 18.0,
            observedAt = Instant.now(),
            isStale = false,
        )
        return buildData(type, preview)
    }

    protected fun buildData(type: ComplicationType, snapshot: WeatherSnapshot?): ComplicationData {
        val weather = snapshot ?: WeatherSnapshot(
            latitude = 0.0,
            longitude = 0.0,
            sustainedMph = 0.0,
            directionDegrees = 0.0,
            directionText = "--",
            gustMph = 0.0,
            observedAt = Instant.now(),
            isStale = true,
        )

        val valueText = valueSelector(weather)
        val titleText = label

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val tapAction = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val shortValueText = PlainComplicationText.Builder(valueText).build()
        val shortTitleText = PlainComplicationText.Builder(titleText).build()
        val fallbackValueText = PlainComplicationText.Builder("--").build()
        val fallbackTitleText = PlainComplicationText.Builder(label).build()

        return when (type) {
            ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(
                text = shortValueText,
                contentDescription = shortValueText,
            )
                .apply { if (showTitle) setTitle(shortTitleText) }
                .setTapAction(tapAction)
                .build()

            ComplicationType.LONG_TEXT -> LongTextComplicationData.Builder(
                text = shortValueText,
                contentDescription = shortValueText,
            )
                .apply { if (showTitle) setTitle(shortTitleText) }
                .setTapAction(tapAction)
                .build()

            else -> ShortTextComplicationData.Builder(
                text = fallbackValueText,
                contentDescription = fallbackValueText,
            )
                .apply { if (showTitle) setTitle(fallbackTitleText) }
                .setTapAction(tapAction)
                .build()
        }
    }
}
