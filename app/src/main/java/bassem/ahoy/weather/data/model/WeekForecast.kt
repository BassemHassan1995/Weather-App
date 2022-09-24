package bassem.ahoy.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class WeekForecast(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    val city: String,
    val cityId: Int,
    val country: String,
    val weatherDays: List<DayWeather>,
    val lat: String = "",
    val lon: String = "",
    var isFavorite: Boolean = false,
    val isCurrent: Boolean = true
)

fun WeekForecast.toCityResponse(): CityResponse =
    CityResponse(
        id = cityId,
        name = city,
        lat = lat,
        lon = lon,
        country = country,
        isFavorite = isFavorite
    )
