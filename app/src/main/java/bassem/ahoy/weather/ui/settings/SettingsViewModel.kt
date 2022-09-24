package bassem.ahoy.weather.ui.settings

import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.ui.base.NoEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<NoEvent>() {

    private val _settings: MutableStateFlow<Settings> = MutableStateFlow(Settings())
    val settings: StateFlow<Settings> = _settings

    init {
        getCurrentSettings()
    }

    private fun getCurrentSettings() = launchCoroutine {
        val result = repository.getAppSettings()
        _settings.emit(result)
    }

    fun changeTemperatureUnit(degreeUnit: DegreeUnit) = launchCoroutine {
        repository.updateDegreeUnit(degreeUnit)
    }

}