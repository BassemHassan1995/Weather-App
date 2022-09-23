package bassem.ahoy.weather.data.db

import androidx.room.*
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast

@Dao
abstract class ForecastDao {

    @Query("SELECT * FROM forecast")
    abstract suspend fun getForecasts(): List<WeekForecast>

    @Query("SELECT * FROM forecast WHERE isFavorite = 1")
    abstract suspend fun getFavoriteForecasts(): List<WeekForecast>

    @Query("SELECT * FROM forecast WHERE isCurrent = 1 LIMIT 1")
    abstract suspend fun getCurrentForecast(): WeekForecast?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertForecast(forecast: WeekForecast)

    @Update
    abstract suspend fun updateForecast(forecast: WeekForecast)

    @Query("UPDATE forecast SET isCurrent = CASE WHEN city = :locationName THEN 1 ELSE 0 END")
    abstract suspend fun updateCurrentForecast(locationName: String)

    @Query("UPDATE forecast SET isFavorite = :isFavorite WHERE city = :locationName")
    abstract suspend fun setForecastFavoriteStatus(
        locationName: String,
        isFavorite: Boolean
    )

    @Delete
    abstract suspend fun deleteForecast(forecast: WeekForecast)

    @Query("SELECT * FROM settings LIMIT 1")
    abstract suspend fun getSettings(): Settings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSettings(settings: Settings)

    @Update
    abstract suspend fun updateSettings(settings: Settings)

}
