package bassem.ahoy.weather.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import bassem.ahoy.weather.data.model.DegreeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class SettingsStoreImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    SettingsStore {

    private object Keys {
        val degreeUnit = stringPreferencesKey("temperature_unit")
    }

    override suspend fun getUnit(): DegreeUnit = dataStore.data.map {
        it[Keys.degreeUnit].toDegreeUnit()
    }.first()

    override suspend fun setUnit(degreeUnit: DegreeUnit) {
        dataStore.edit {
            it[Keys.degreeUnit] = degreeUnit.name
        }
    }
}

private fun String?.toDegreeUnit(): DegreeUnit =
    if (this == DegreeUnit.FAHRENHEIT.name)
        DegreeUnit.FAHRENHEIT
    else
        DegreeUnit.CELSIUS


