package bassem.ahoy.weather.data.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast

@Dao
abstract class ForecastDao {

    @Query("SELECT * FROM forecast WHERE isCurrent = 1 LIMIT 1")
    abstract suspend fun getCurrentForecast(): WeekForecast?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCurrentForecast(forecast: WeekForecast)

    @Update
    abstract suspend fun updateCurrentForecast(forecast: WeekForecast)

    suspend fun upsertCurrentForecast(weekForecast: WeekForecast) {
        try {
            insertCurrentForecast(weekForecast)
        } catch (e: SQLiteConstraintException) {
            updateCurrentForecast(weekForecast)
        }
    }

    @Query("UPDATE forecast SET isCurrent = CASE WHEN city = :locationName THEN 1 ELSE 0 END")
    abstract suspend fun updateCurrentForecast(locationName: String)

    @Delete
    abstract suspend fun deleteForecast(forecast: WeekForecast)

    @Query("SELECT * FROM settings LIMIT 1")
    abstract suspend fun getSettings(): Settings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSettings(settings: Settings)

    @Update
    abstract suspend fun updateSettings(settings: Settings)

    suspend fun upsertSettings(settings: Settings) {
        try {
            insertSettings(settings)
        } catch (e: SQLiteConstraintException) {
            updateSettings(settings)
        }
    }

    @Query("SELECT * FROM favorite")
    abstract suspend fun getFavoriteCities(): List<CityResponse>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertFavoriteCity(cityResponse: CityResponse): Long

    @Delete
    abstract suspend fun deleteFavoriteCity(cityResponse: CityResponse)

}
