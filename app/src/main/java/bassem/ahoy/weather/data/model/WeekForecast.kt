package bassem.ahoy.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "forecast")
data class WeekForecast(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val city: String,
    val weatherDays: List<DayWeather>,
    val isFavorite: Boolean = false,
    val isCurrent: Boolean = true
)
