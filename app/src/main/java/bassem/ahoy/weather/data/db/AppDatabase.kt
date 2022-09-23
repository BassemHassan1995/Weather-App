package bassem.ahoy.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao

    companion object {
        const val DATABASE_NAME = "weather-db"
    }
}