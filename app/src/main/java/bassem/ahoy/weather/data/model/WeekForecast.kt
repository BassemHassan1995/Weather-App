package bassem.ahoy.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class WeekForecast(
    @PrimaryKey val id: Int = 0,
    val city: String,
    val cityId: Int,
    val country: String,
    val weatherDays: List<DayWeather>,
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

data class TodayForecast(
    val id: Int = 0,
    val city: String,
    val cityId: Int,
    val country: String,
    val weatherDay: DayWeather,
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

fun WeekForecast.toCityResponse(): CityResponse =
    CityResponse(
        id = cityId,
        name = city,
        lat = lat,
        lon = lon,
        country = country
    )

fun TodayForecast.toCityResponse(): CityResponse =
    CityResponse(
        id = cityId,
        name = city,
        lat = lat,
        lon = lon,
        country = country
    )
