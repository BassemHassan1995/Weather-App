package bassem.ahoy.weather.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import bassem.ahoy.weather.databinding.FragmentSearchBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.weather.WeatherEvent
import bassem.ahoy.weather.ui.weather.WeatherViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, WeatherEvent>() {

    override val viewModel by activityViewModels<WeatherViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun observeData(event: WeatherEvent) {

    }
}