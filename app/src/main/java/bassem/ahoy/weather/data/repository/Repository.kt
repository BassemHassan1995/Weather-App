package bassem.ahoy.weather.data.repository

import bassem.ahoy.weather.data.model.*
import bassem.ahoy.weather.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun searchCity(query: String): DataResult<List<CityResponse>>
    suspend fun getWeekForecast(latitude: Double, longitude: Double): DataResult<WeekForecast>
    suspend fun getTodayForecast(latitude: Double, longitude: Double): DataResult<TodayForecast>
    suspend fun updateCurrentLocationForecast(weekForecast: WeekForecast)
    fun getLastKnownLocationForecast(): Flow<WeekForecast?>

    suspend fun getDegreeUnit(): DegreeUnit
    suspend fun setDegreeUnit(degreeUnit: DegreeUnit)

    fun getFavorites(): Flow<List<CityResponse>>
    suspend fun isCityFavorite(cityId: Int): Boolean
    suspend fun addCityToFavorites(cityResponse: CityResponse)
    suspend fun removeCityFromFavorites(cityResponse: CityResponse)
}