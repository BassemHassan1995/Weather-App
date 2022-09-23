package bassem.ahoy.weather.data.repository

import android.location.Location
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.utils.DataResult

interface Repository{

    suspend fun getWeekForecast(query: String) : DataResult<WeekForecast>
    suspend fun getWeekForecast(location: Location) : DataResult<WeekForecast>
}