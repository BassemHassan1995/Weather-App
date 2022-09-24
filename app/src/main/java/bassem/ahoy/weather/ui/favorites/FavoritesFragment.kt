package bassem.ahoy.weather.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.getFullName
import bassem.ahoy.weather.databinding.FragmentFavoritesBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.search.CityAdapter
import bassem.ahoy.weather.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesEvent>() {

    override val viewModel by viewModels<FavoritesViewModel>()

    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CityAdapter {
            openWeatherDetails(it)
        }
    }

    private fun openWeatherDetails(cityResponse: CityResponse) =
        navigateTo(FavoritesFragmentDirections.actionFavoritesToDetails(cityResponse.lat.toFloat(),
            cityResponse.lon.toFloat()
        ))

    override fun observeData() {
        super.observeData()
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                favorites.flowWithLifecycle(lifecycle)
                    .collect {
                        adapter.submitList(it)
                    }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun handleEvent(event: FavoritesEvent) {
        when (event) {
            FavoritesEvent.NoFavoritesFoundEvent -> showToast(R.string.no_favorites_found)
        }
    }

    override fun setupViews() {
        binding.recyclerViewFavorites.adapter = adapter
    }

}