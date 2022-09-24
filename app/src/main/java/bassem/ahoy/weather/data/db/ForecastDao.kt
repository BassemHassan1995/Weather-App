package bassem.ahoy.weather.data.db

import androidx.room.*
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.WeekForecast
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ForecastDao {

    //Forecast
    @Query("SELECT * FROM forecast LIMIT 1")
    abstract fun getLastKnownLocationForecast(): Flow<WeekForecast?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun setCurrentLocationForecast(forecast: WeekForecast):Long

    @Delete
    abstract suspend fun deleteForecast(forecast: WeekForecast)

    //Favorites
    @Query("SELECT * FROM favorite")
    abstract fun getFavoriteCities(): Flow<List<CityResponse>>

    @Query("SELECT EXISTS (SELECT * FROM favorite WHERE id = :cityId LIMIT 1)")
    abstract fun isCityFavorite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFavoriteCity(cityResponse: CityResponse)

    @Delete
    abstract suspend fun deleteFavoriteCity(cityResponse: CityResponse)

}
