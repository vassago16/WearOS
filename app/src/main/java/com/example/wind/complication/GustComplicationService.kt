package com.example.wind.complication

import androidx.wear.watchface.complications.data.ComplicationType
import com.example.wind.data.WeatherSnapshot

class GustComplicationService : BaseWindComplicationService() {
    override val label = "Gust"
    override val valueSelector: (WeatherSnapshot) -> String = { snapshot -> "${snapshot.gustMph.toInt()} mph" }

    override fun getPreviewData(type: ComplicationType) = super.getPreviewData(type)
}
