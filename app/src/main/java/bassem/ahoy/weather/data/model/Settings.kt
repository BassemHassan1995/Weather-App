package bassem.ahoy.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val unit: DegreeUnit = DegreeUnit.CELSIUS
)