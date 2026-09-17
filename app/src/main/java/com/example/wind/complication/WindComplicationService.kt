package com.example.wind.complication

import androidx.wear.watchface.complications.data.ComplicationType
import com.example.wind.data.WeatherSnapshot

class WindComplicationService : BaseWindComplicationService() {
    override val label = "Wind"
    override val showTitle = false
    override val valueSelector: (WeatherSnapshot) -> String = { snapshot ->
        "${snapshot.directionText} ${snapshot.sustainedMph.toInt()}"
    }

    override fun getPreviewData(type: ComplicationType) = super.getPreviewData(type)
}
