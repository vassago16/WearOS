package com.example.wind.complication

import androidx.wear.watchface.complications.data.ComplicationType
import com.example.wind.data.WeatherSnapshot

class WindSummaryComplicationService : BaseWindComplicationService() {
    override val label = "Wind Summary"
    override val valueSelector: (WeatherSnapshot) -> String = { snapshot -> "${snapshot.directionText} ${snapshot.sustainedMph.toInt()} mph" }

    override fun getPreviewData(type: ComplicationType) = super.getPreviewData(type)
}
