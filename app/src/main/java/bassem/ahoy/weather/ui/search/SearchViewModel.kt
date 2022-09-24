package bassem.ahoy.weather.ui.search

import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<SearchEvent>() {

    private val _cities: MutableStateFlow<List<CityResponse>> = MutableStateFlow(emptyList())
    val cities: StateFlow<List<CityResponse>> = _cities

    fun searchForCity(city: String) {
        startLoading()
        launchCoroutine {
            val result = repository.searchCity(city)
            when (result) {
                is DataResult.Failure -> result.cause.localizedMessage?.let {
                    sendEvent(SearchEvent.ErrorGettingResult(it))
                }
                is DataResult.Success -> with(result.value) {
                    _cities.value = this
                }
            }.also {
                endLoading()
            }
        }
    }

    fun onCityClicked(cityResponse: CityResponse) {
        TODO("Not yet implemented")
    }

}