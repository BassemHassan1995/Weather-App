package bassem.ahoy.weather.data.repository

import android.location.Location
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.utils.DataResult

interface Repository{

    suspend fun getWeekForecast(query: String) : DataResult<WeekForecast>
    suspend fun searchCity(query: String) : DataResult<List<CityResponse>>
    suspend fun getWeekForecast(location: Location) : DataResult<WeekForecast>

    suspend fun getAppSettings() : Settings
    suspend fun saveAppSettings()
    suspend fun updateDegreeUnit(degreeUnit: DegreeUnit)

    suspend fun addForecastToFavorites(weekForecast: WeekForecast)
    suspend fun getFavorites(weekForecast: WeekForecast) : List<WeekForecast>
    suspend fun updateForecast(weekForecast: WeekForecast)
}