package bassem.ahoy.weather.ui.weather

import android.location.Location
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bassem.ahoy.weather.data.model.Coord
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.data.model.toCityResponse
import bassem.ahoy.weather.data.notifications.NotificationScheduler
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.utils.DataResult
import bassem.ahoy.weather.utils.extensions.toCoord
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.renderer.DescriptorRenderer.ValueParametersHandler.DEFAULT

private const val DEFAULT_HOUR = 5
private const val DEFAULT_MINUTE = 0

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository, notificationScheduler: NotificationScheduler
) :
    BaseViewModel<WeatherEvent>(), OnSuccessListener<Location?>,
    SwipeRefreshLayout.OnRefreshListener {

    private val _currentForecast: MutableStateFlow<WeekForecast?> = MutableStateFlow(null)
    val currentForecast: StateFlow<WeekForecast?> = _currentForecast

    private val _isFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite


    init {
        getLastKnownLocation()
        checkCurrentLocation()
        notificationScheduler.scheduleReminderNotification(DEFAULT_HOUR, DEFAULT_MINUTE)
    }

    private fun getLastKnownLocation() {
        launchCoroutine {
            repository.getLastKnownLocationForecast().collect {
                _currentForecast.emit(it)
            }
        }
    }

    private fun getData(coord: Coord) {
        startLoading()
        launchCoroutine {
            handleResult(
                repository.getWeekForecast(
                    latitude = coord.lat,
                    longitude = coord.lon
                )
            )
        }
    }

    private fun handleResult(result: DataResult<WeekForecast>) {
        when (result) {
            is DataResult.Failure -> result.cause.localizedMessage?.let {
                sendEvent(WeatherEvent.ErrorGettingForecastEvent(it))
            }
            is DataResult.Success -> with(result.value) {
                _currentForecast.value = this
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

    private fun checkCurrentLocation() = sendEvent(WeatherEvent.GetLocationEvent)

    override fun onSuccess(location: Location?) {
        if (location == null) {
            sendEvent(WeatherEvent.NoLocationDetectedEvent)
        } else {
            getData(location.toCoord())
        }
    }

    override fun onRefresh() {
        checkCurrentLocation()
    }

    fun updateFavoriteState() {
        _currentForecast.value?.let {
            launchCoroutine {
                if (_isFavorite.value)
                    repository.removeCityFromFavorites(it.toCityResponse())
                else
                    repository.addCityToFavorites(it.toCityResponse())
            }
        }
    }

}