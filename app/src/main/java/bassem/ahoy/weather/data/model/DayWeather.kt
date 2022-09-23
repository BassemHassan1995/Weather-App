package bassem.ahoy.weather.data.model

import java.io.Serializable

data class DayWeather(
    val weekDay: String = "",
    val date: String = "",
    val temperature: String = "",
    val sunrise: String = "",
    val sunset: String = "",
    val description: String = "",
    val windSpeed: String = "",
    val humidity: String = "",
    val iconUrl: String = ""
) : Serializable