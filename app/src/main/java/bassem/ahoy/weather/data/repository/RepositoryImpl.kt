package bassem.ahoy.weather.data.repository

import bassem.ahoy.weather.data.api.ApiHelper
import bassem.ahoy.weather.data.db.AppDatabase
import bassem.ahoy.weather.data.model.*
import bassem.ahoy.weather.data.store.SettingsStore
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
    private val database: AppDatabase,
    private val settingsStore: SettingsStore
) : Repository {

    override suspend fun searchCity(query: String): DataResult<List<CityResponse>> = DataResult {
        val response = apiHelper.searchCity(query)

        when (response.isSuccessful) {
            true -> response.body().orEmpty()
            false -> throw Exception(response.getApiError()?.message)
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
                getDegreeUnit().getDegreeFormat()
            )
            when (response.isSuccessful) {
                true -> {
                    val weekForecast = response.body()?.toWeekForecast()
                    weekForecast?.apply {
                        updateCurrentLocationForecast(this)
                    }!!
                }
                false -> throw Exception(response.errorBody().toString())
            }
        }

    override suspend fun getTodayForecast(
        latitude: Double,
        longitude: Double
    ): DataResult<TodayForecast> =
        DataResult {
            val response = apiHelper.getWeekForecast(
                longitude,
                latitude,
                getDegreeUnit().getDegreeFormat()
            )
            when (response.isSuccessful) {
                true -> response.body()?.toTodayForecast()!!
                false -> throw Exception(response.errorBody().toString())
            }
        }


    override fun getLastKnownLocationForecast(): Flow<WeekForecast?> =
        database.forecastDao().getLastKnownLocationForecast()

    override suspend fun updateCurrentLocationForecast(weekForecast: WeekForecast) =
        withContext(Dispatchers.Default) {
            database.forecastDao().setCurrentLocationForecast(weekForecast)
        }

    override suspend fun getDegreeUnit(): DegreeUnit =
        settingsStore.getUnit()

    override suspend fun setDegreeUnit(degreeUnit: DegreeUnit) =
        settingsStore.setUnit(degreeUnit)

    override suspend fun addCityToFavorites(cityResponse: CityResponse) =
        withContext(Dispatchers.Default) {
            database.forecastDao().insertFavoriteCity(cityResponse)
        }

    override fun getFavorites(): Flow<List<CityResponse>> =
        database.forecastDao().getFavoriteCities()

    override fun isCityFavorite(cityId: Int): Flow<Boolean> =
        database.forecastDao().isCityFavorite(cityId)

    override suspend fun removeCityFromFavorites(cityResponse: CityResponse) =
        withContext(Dispatchers.Default) {
            database.forecastDao().deleteFavoriteCity(cityResponse)
        }


    private suspend fun WeekForecastResponse.toWeekForecast(): WeekForecast =
        WeekForecast(
            lat = city.coord.lat,
            lon = city.coord.lon,
            city = city.name,
            weatherDays = list.map { it.toDayWeather() },
            country = city.country,
            cityId = city.id
        )

    private suspend fun WeekForecastResponse.toTodayForecast(): TodayForecast =
        TodayForecast(
            lat = city.coord.lat,
            lon = city.coord.lon,
            city = city.name,
            weatherDay = list.map { it.toDayWeather() }.first(),
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
            unit = getDegreeUnit()
        )
}