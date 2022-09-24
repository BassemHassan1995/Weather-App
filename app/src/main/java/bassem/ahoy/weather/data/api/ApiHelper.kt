package bassem.ahoy.weather.data.api

import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.WeekForecastResponse
import retrofit2.Response

interface ApiHelper {

    fun getIconUrl(iconId: String): String

    suspend fun getWeekForecast(
        longitude: Double,
        latitude: Double,
        unit: String
    ): Response<WeekForecastResponse>

    suspend fun searchCity(city: String): Response<List<CityResponse>>

}