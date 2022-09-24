package bassem.ahoy.weather.data.db

import androidx.room.TypeConverter
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.WeekForecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val dayWeatherType = Types.newParameterizedType(List::class.java, DayWeather::class.java)
    private val dayWeatherListAdapter = moshi.adapter<List<DayWeather>>(dayWeatherType)

    @TypeConverter
    fun stringToDayWeatherList(value: String): List<DayWeather>? =
        dayWeatherListAdapter.fromJson(value)

    @TypeConverter
    fun dayWeatherListToString(dayWeatherList: List<DayWeather>): String =
        dayWeatherListAdapter.toJson(dayWeatherList)

}