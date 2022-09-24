package bassem.ahoy.weather.data.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ForecastDao {

    //Forecast
    @Query("SELECT * FROM forecast LIMIT 1")
    abstract fun getLastKnownLocationForecast(): Flow<WeekForecast?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun setCurrentLocationForecast(forecast: WeekForecast)

    @Delete
    abstract suspend fun deleteForecast(forecast: WeekForecast)

    //Settings
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

    //Favorites
    @Query("SELECT * FROM favorite")
    abstract fun getFavoriteCities(): Flow<List<CityResponse>>

    @Query("SELECT EXISTS (SELECT * FROM favorite WHERE id = :cityId LIMIT 1)")
    abstract suspend fun isCityFavorite(cityId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFavoriteCity(cityResponse: CityResponse)

    @Delete
    abstract suspend fun deleteFavoriteCity(cityResponse: CityResponse)

}
