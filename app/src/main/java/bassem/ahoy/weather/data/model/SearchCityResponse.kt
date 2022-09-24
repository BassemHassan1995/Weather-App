package bassem.ahoy.weather.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityResponse(
    val name: String,
    val lat: String,
    val lon: String,
    val country: String,
    val state: String
)