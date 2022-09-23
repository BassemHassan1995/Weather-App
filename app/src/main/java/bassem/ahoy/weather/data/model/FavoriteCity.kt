package bassem.ahoy.weather.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteCity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "city_name") val name: String
)