package bassem.ahoy.weather.data.repository

import android.location.Location
import android.util.Log
import bassem.ahoy.weather.data.api.ApiHelper
import bassem.ahoy.weather.data.db.AppDatabase
import bassem.ahoy.weather.data.model.*
import bassem.ahoy.weather.utils.DataResult
import bassem.ahoy.weather.utils.extensions.getApiError
import bassem.ahoy.weather.utils.extensions.getDate
import bassem.ahoy.weather.utils.extensions.getTime
import bassem.ahoy.weather.utils.extensions.getWeekDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getWeekForecast(location: Location): DataResult<WeekForecast> =
        DataResult {
            val response = apiHelper.getWeekForecast(
                location.longitude,
                location.latitude,
                getAppSettings().unit.getDegreeFormat()
            )
            when (response.isSuccessful) {
                true -> {
                    val weekForecast = response.body()?.toWeekForecast()
                    weekForecast?.apply {
                        val test = isCityFavorite(cityId)
                        isFavorite = test
                    }!!
                }
                false -> throw Exception(response.errorBody().toString())
            }
        }

    override suspend fun getWeekForecast(
        latitude: Double,
        longitude: Double
    ): DataResult<WeekForecast> =
        DataResult {
            val response = apiHelper.getWeekForecast(
                longitude,
                latitude,
                getAppSettings().unit.getDegreeFormat()
            )
            when (response.isSuccessful) {
                true -> {
                    val weekForecast = response.body()?.toWeekForecast()
                    weekForecast?.apply {
                        val test = isCityFavorite(cityId)
                        isFavorite = test
                    }!!
                }
                false -> throw Exception(response.errorBody().toString())
            }
        }

    private suspend fun getDegreeUnitFromSettings(): DegreeUnit = getAppSettings().unit

    override suspend fun getAppSettings(): Settings =
        withContext(Dispatchers.Default) {
            val settings = database.forecastDao().getSettings()

            settings ?: Settings().apply {
                upsertAppSettings(this)
            }
        }

    override suspend fun updateDegreeUnit(degreeUnit: DegreeUnit) =
        withContext(Dispatchers.Default) {
            database.forecastDao().upsertSettings(Settings(unit = degreeUnit))
        }

    override suspend fun upsertAppSettings(settings: Settings) =
        withContext(Dispatchers.Default) {
            database.forecastDao().upsertSettings(settings)
        }

    override suspend fun addCityToFavorites(cityResponse: CityResponse) =
        withContext(Dispatchers.Default) {
            val result = database.forecastDao().insertFavoriteCity(cityResponse)
            Log.d("Favorites", "addCityToFavorites: $result")

            Unit
        }

    override fun getFavorites(): Flow<List<CityResponse>> =
        database.forecastDao().getFavoriteCities()

    override suspend fun isCityFavorite(cityId: Int): Boolean =
        withContext(Dispatchers.Default) {
            database.forecastDao().isCityFavorite(cityId)
        }

    override suspend fun removeCityFromFavorites(cityResponse: CityResponse) =
        withContext(Dispatchers.Default) {
            database.forecastDao().deleteFavoriteCity(cityResponse)
        }

    private suspend fun WeekForecastResponse.toWeekForecast(): WeekForecast =
        WeekForecast(
            city = city.name,
            weatherDays = list.map { it.toDayWeather() },
            country = city.country,
            cityId = city.id
        )

    private suspend fun DayForecast.toDayWeather(): DayWeather =
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