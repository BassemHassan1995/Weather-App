package bassem.ahoy.weather.ui.weather

import android.location.Location
import bassem.ahoy.weather.ui.base.Event

sealed class WeatherEvent : Event() {

    object NoLocationDetectedEvent : WeatherEvent()
    object GetLocationEvent : WeatherEvent()
    data class UpdateLocationEvent(val location: Location) : WeatherEvent()
}