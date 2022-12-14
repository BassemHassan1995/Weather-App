package bassem.ahoy.weather.ui.details

import bassem.ahoy.weather.data.model.TodayForecast
import bassem.ahoy.weather.data.model.toCityResponse
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<DetailsEvent>() {

    private val _todayWeather: MutableStateFlow<TodayForecast?> = MutableStateFlow(null)
    val todayWeather: StateFlow<TodayForecast?> = _todayWeather

    private val _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun getDayWeatherDetails(latitude: Double, longitude: Double) {
        startLoading()
        launchCoroutine {
            handleResult(
                repository.getTodayForecast(latitude, longitude)
            )
        }
    }

    private fun handleResult(result: DataResult<TodayForecast>) {
        when (result) {
            is DataResult.Failure -> result.cause.localizedMessage?.let {
                sendEvent(DetailsEvent.ErrorGettingDetailsEvent(it))
            }
            is DataResult.Success -> with(result.value) {
                _todayWeather.value = this
                checkIsFavorite(this.cityId)
            }
        }.also {
            endLoading()
        }
    }

    private fun checkIsFavorite(cityId: Int) {
        launchCoroutine {
            repository.isCityFavorite(cityId).collect {
                _isFavorite.value = it
            }
        }
    }

    fun updateFavoriteState() {
        launchCoroutine {
            _todayWeather.value?.let {
                if (_isFavorite.value)
                    repository.removeCityFromFavorites(it.toCityResponse())
                else
                    repository.addCityToFavorites(it.toCityResponse())
            }
        }
    }

}