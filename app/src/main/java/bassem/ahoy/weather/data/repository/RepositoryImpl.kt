package bassem.ahoy.weather.data.repository

import android.location.Location
import bassem.ahoy.weather.data.api.ApiHelper
import bassem.ahoy.weather.data.model.DayForecast
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.data.model.WeekForecastResponse
import bassem.ahoy.weather.utils.DataResult
import bassem.ahoy.weather.utils.extensions.getApiError
import bassem.ahoy.weather.utils.extensions.getDate
import bassem.ahoy.weather.utils.extensions.getTime
import bassem.ahoy.weather.utils.extensions.getWeekDay
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiHelper: ApiHelper) : Repository {

    override suspend fun getWeekForecast(query: String): DataResult<WeekForecast> = DataResult {
        val response = apiHelper.getWeekForecast(query, getUnitFromSettings())

        when (response.isSuccessful) {
            true -> response.body()?.toWeekForecast()!!
            false -> throw Exception(response.getApiError()?.message)
        }
    }

    private fun getUnitFromSettings(): String {
        return "imperial"
        TODO("Not yet implemented")
    }

    override suspend fun getWeekForecast(location: Location): DataResult<WeekForecast>  = DataResult {
        val response = apiHelper.getWeekForecast(location.longitude, location.latitude, getUnitFromSettings())

        when (response.isSuccessful) {
            true -> response.body()?.toWeekForecast()!!
            false -> throw Exception(response.errorBody().toString())
        }
    }

    private fun WeekForecastResponse.toWeekForecast(): WeekForecast =
        WeekForecast(city = city.name, weatherDays = list.map { it.toDayWeather() })

    private fun DayForecast.toDayWeather(): DayWeather =
        DayWeather(
            weekDay = dt.getWeekDay(),
            date = dt.getDate(),
            sunrise = sunrise.getTime(),
            sunset = sunset.getTime(),
            temperature = (temp.day).toInt().toString(),
            description = weather[0].description,
            humidity = humidity.toString(),
            windSpeed = "s $speed mph",
            iconUrl = apiHelper.getIconUrl(weather[0].icon)
        )
}