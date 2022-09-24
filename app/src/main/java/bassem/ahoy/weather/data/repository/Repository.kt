package bassem.ahoy.weather.data.repository

import android.location.Location
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun searchCity(query: String): DataResult<List<CityResponse>>
    suspend fun getWeekForecast(location: Location): DataResult<WeekForecast>
    suspend fun getWeekForecast(latitude: Double, longitude: Double): DataResult<WeekForecast>

    suspend fun getAppSettings(): Settings
    suspend fun updateDegreeUnit(degreeUnit: DegreeUnit)
    suspend fun upsertAppSettings(settings: Settings)

    fun getFavorites(): Flow<List<CityResponse>>
    suspend fun isCityFavorite(cityId: Int) : Boolean
    suspend fun addCityToFavorites(cityResponse: CityResponse)
    suspend fun removeCityFromFavorites(cityResponse: CityResponse)
}