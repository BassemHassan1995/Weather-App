package bassem.ahoy.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bassem.ahoy.weather.data.model.FavoriteCity
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.model.WeekForecast

@Database(entities = [FavoriteCity::class, WeekForecast::class, Settings::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao

    companion object {
        const val DATABASE_NAME = "weather-db"
    }
}