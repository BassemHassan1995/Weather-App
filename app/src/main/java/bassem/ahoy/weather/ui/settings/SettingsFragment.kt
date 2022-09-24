package bassem.ahoy.weather.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import bassem.ahoy.weather.databinding.FragmentSettingsBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.weather.WeatherEvent
import bassem.ahoy.weather.ui.weather.WeatherViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding, WeatherEvent>() {

    override val viewModel by activityViewModels<WeatherViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun observeData(event: WeatherEvent) {
    }

}