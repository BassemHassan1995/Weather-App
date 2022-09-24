package bassem.ahoy.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class WeekForecast(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val city: String,
    val country: String,
    val weatherDays: List<DayWeather>,
    val lat: String = "",
    val lon: String = "",
    val isFavorite: Boolean = false,
    val isCurrent: Boolean = true
)

fun WeekForecast.toCityResponse(): CityResponse =
    CityResponse(name = city, lat = lat, lon = lon, country = country)
