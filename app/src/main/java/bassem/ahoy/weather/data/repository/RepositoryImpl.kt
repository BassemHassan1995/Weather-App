package bassem.ahoy.weather.data.repository

import android.location.Location
import bassem.ahoy.weather.data.api.ApiHelper
import bassem.ahoy.weather.data.db.AppDatabase
import bassem.ahoy.weather.data.model.*
import bassem.ahoy.weather.utils.DataResult
import bassem.ahoy.weather.utils.extensions.getApiError
import bassem.ahoy.weather.utils.extensions.getDate
import bassem.ahoy.weather.utils.extensions.getTime
import bassem.ahoy.weather.utils.extensions.getWeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val database: AppDatabase
) : Repository {


    override suspend fun searchCity(query: String): DataResult<List<CityResponse>> = DataResult {
        val response = apiHelper.searchCity(query)

        when (response.isSuccessful) {
            true -> response.body().orEmpty()
            false -> throw Exception(response.getApiError()?.message)
        }
    }

    override suspend fun getWeekForecast(query: String): DataResult<WeekForecast> = DataResult {
        val response = apiHelper.getWeekForecast(query, getAppSettings().unit.getDegreeFormat())

        when (response.isSuccessful) {
            true -> response.body()?.toWeekForecast()!!
            false -> throw Exception(response.getApiError()?.message)
        }
    }

    override suspend fun getWeekForecast(location: Location): DataResult<WeekForecast> =
        DataResult {
            val response = apiHelper.getWeekForecast(
                location.longitude,
                location.latitude,
                getAppSettings().unit.getDegreeFormat()
            )

            when (response.isSuccessful) {
                true -> response.body()?.toWeekForecast()!!
                false -> throw Exception(response.errorBody().toString())
            }
        }

    private fun getDegreeUnitFromSettings(): DegreeUnit = DegreeUnit.CELSIUS

    override suspend fun getAppSettings(): Settings = withContext(Dispatchers.Default) {
        database.forecastDao().getSettings() ?: Settings()
    }

    override suspend fun saveAppSettings() = withContext(Dispatchers.Default) {
        database.forecastDao().insertSettings(Settings())
    }

    override suspend fun updateDegreeUnit(degreeUnit: DegreeUnit) =
        withContext(Dispatchers.Default) {
            database.forecastDao().updateSettings(Settings(unit = degreeUnit))
        }

    override suspend fun addForecastToFavorites(weekForecast: WeekForecast) =
        withContext(Dispatchers.Default) {
            database.forecastDao().insertForecast(weekForecast)
        }

    override suspend fun getFavorites(weekForecast: WeekForecast): List<WeekForecast> =
        withContext(Dispatchers.Default) {
            database.forecastDao().getFavoriteForecasts()
        }

    override suspend fun updateForecast(weekForecast: WeekForecast) =
        withContext(Dispatchers.Default) {
            database.forecastDao().updateForecast(weekForecast)
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
            iconUrl = apiHelper.getIconUrl(weather[0].icon),
            unit = getDegreeUnitFromSettings()
        )
}