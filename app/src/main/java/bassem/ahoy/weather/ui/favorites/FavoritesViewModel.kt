package bassem.ahoy.weather.ui.favorites

import android.util.Log
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<FavoritesEvent>() {

    private val _favorites: MutableStateFlow<List<CityResponse>> = MutableStateFlow(emptyList())
    val favorites: StateFlow<List<CityResponse>> = _favorites

    init {
        getFavoriteCities()
    }

    private fun getFavoriteCities() {
        startLoading()
        launchCoroutine {
            val result = repository.getFavorites()
            Log.d("Favorites", "getFavoriteCities: ${result.size}")
            if (result.isEmpty())
                sendEvent(FavoritesEvent.NoFavoritesFoundEvent)
            else
                _favorites.value = result
        }
    }
}
