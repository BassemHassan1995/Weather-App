package bassem.ahoy.weather.data.model

import androidx.room.Entity

@Entity(tableName = "settings")
data class Settings(val unit: DegreeUnit = DegreeUnit.CELSIUS)