package bassem.ahoy.weather.data.repository

import android.location.Location
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.utils.DataResult

interface Repository {

    suspend fun getWeekForecast(query: String): DataResult<WeekForecast>
    suspend fun searchCity(query: String): DataResult<List<CityResponse>>
    suspend fun getWeekForecast(location: Location): DataResult<WeekForecast>

    suspend fun getAppSettings(): Settings
    suspend fun updateDegreeUnit(degreeUnit: DegreeUnit)
    suspend fun upsertAppSettings(settings: Settings)

    suspend fun getFavorites(): List<CityResponse>
    suspend fun addCityToFavorites(cityResponse: CityResponse)
    suspend fun removeCityFromFavorites(cityResponse: CityResponse)
}