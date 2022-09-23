package bassem.ahoy.weather.ui.weather

import bassem.ahoy.weather.ui.base.Event

sealed class WeatherEvent : Event() {

    object NoLocationDetectedEvent : WeatherEvent()
    object GetLocationEvent : WeatherEvent()
    data class ErrorGettingForecastEvent(val errorMessage: String) : WeatherEvent()
}