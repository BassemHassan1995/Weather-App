package bassem.ahoy.weather.ui.settings

import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.ui.base.NoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<NoEvent>() {

    var degreeUnit = DegreeUnit.CELSIUS
        private set

    init {
        getCurrentSettings()
    }

    private fun getCurrentSettings() = launchCoroutine {
        degreeUnit = repository.getDegreeUnit()
    }

    fun changeTemperatureUnit(degreeUnit: DegreeUnit) = launchCoroutine {
        repository.setDegreeUnit(degreeUnit)
    }

}