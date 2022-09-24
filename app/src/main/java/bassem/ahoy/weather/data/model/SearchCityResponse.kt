package bassem.ahoy.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity (tableName = "favorite")
data class CityResponse(
    @PrimaryKey var id: Int = 0,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)

fun CityResponse.getFullName() : String{
    return "$name, $country"
}