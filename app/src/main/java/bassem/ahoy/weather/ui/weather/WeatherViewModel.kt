package bassem.ahoy.weather.ui.weather

import android.location.Location
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.data.model.toCityResponse
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.utils.DataResult
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<WeatherEvent>(), OnSuccessListener<Location?>,
    SwipeRefreshLayout.OnRefreshListener {

    private val _weatherDays: MutableStateFlow<List<DayWeather>> = MutableStateFlow(emptyList())
    val weatherDays: StateFlow<List<DayWeather>> = _weatherDays

    private val _currentCity: MutableStateFlow<CityResponse?> = MutableStateFlow(null)
    val currentCity: StateFlow<CityResponse?> = _currentCity

    init {
        checkCurrentLocation()
    }

    private fun getData(location: Location) {
        startLoading()
        launchCoroutine {
            handleResult(repository.getWeekForecast(location))
        }
    }

    private fun getData(city: String) {
        startLoading()
        launchCoroutine {
            handleResult(repository.getWeekForecast(city))
        }
    }

    private fun handleResult(result: DataResult<WeekForecast>) {
        when (result) {
            is DataResult.Failure -> result.cause.localizedMessage?.let {
                sendEvent(WeatherEvent.ErrorGettingForecastEvent(it))
            }
            is DataResult.Success -> with(result.value) {
                _weatherDays.value = weatherDays
                _currentCity.value = toCityResponse()
            }
        }.also {
            endLoading()
        }
    }

    private fun checkCurrentLocation() = sendEvent(WeatherEvent.GetLocationEvent)

    override fun onSuccess(location: Location?) {
        if (location == null) {
            sendEvent(WeatherEvent.NoLocationDetectedEvent)
        } else {
            getData(location)
        }
    }

    override fun onRefresh() {
        _currentCity.value?.let {
            getData(it.name)
        }
    }

    fun updateFavoriteState() {
        _currentCity.value?.let {
            launchCoroutine {
                if (it.isFavorite)
                {
                    repository.removeCityFromFavorites(it)
                    _currentCity.value = it.copy(isFavorite = false)
                }
                else
                {
                    repository.addCityToFavorites(it)
                    _currentCity.value = it.copy(isFavorite = true)
                }
            }
        }
    }

}