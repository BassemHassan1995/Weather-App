package bassem.ahoy.weather.data.store

import bassem.ahoy.weather.data.model.DegreeUnit
import kotlinx.coroutines.flow.Flow

interface SettingsStore {
    suspend fun getUnit(): DegreeUnit

    suspend fun setUnit(degreeUnit: DegreeUnit)
}
