package com.example.wind.complication

import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import com.example.wind.data.WeatherSnapshot

class WindComplicationService : BaseWindComplicationService() {
    override val label = "Wind"
    override val valueSelector: (WeatherSnapshot) -> String = { snapshot -> "${snapshot.sustainedMph.toInt()} mph" }

    override fun getPreviewData(type: ComplicationType) = super.getPreviewData(type)
}
