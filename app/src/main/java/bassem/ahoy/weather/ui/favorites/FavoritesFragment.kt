package bassem.ahoy.weather.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import bassem.ahoy.weather.databinding.FragmentFavoritesBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.weather.WeatherEvent
import bassem.ahoy.weather.ui.weather.WeatherViewModel

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, WeatherEvent>() {

    override val viewModel by activityViewModels<WeatherViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun observeData(event: WeatherEvent) {
    }

}