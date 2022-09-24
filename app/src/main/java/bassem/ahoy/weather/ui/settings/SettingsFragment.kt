package bassem.ahoy.weather.ui.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.model.Settings
import bassem.ahoy.weather.databinding.FragmentSettingsBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.base.NoEvent
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment<FragmentSettingsBinding, NoEvent>() {

    override val viewModel by activityViewModels<SettingsViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun setupViews(view: View) {
        binding.temperatureRadioGroup.setOnCheckedChangeListener { _, idRes ->
            val newDegreeUnit = if (idRes == R.id.radio_fahrenheit)
                DegreeUnit.FAHRENHEIT
            else
                DegreeUnit.CELSIUS

            viewModel.changeTemperatureUnit(newDegreeUnit)
        }
    }

    override fun observeData() {
        super.observeData()
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                settings.flowWithLifecycle(lifecycle)
                    .collect {
                        renderAppSettings(it)
                    }
            }
        }
    }

    override fun handleEvent(event: NoEvent) = Unit

    private fun renderAppSettings(settings: Settings) {
        val radioButton = when (settings.unit) {
            DegreeUnit.CELSIUS -> R.id.radio_celsius
            DegreeUnit.FAHRENHEIT -> R.id.radio_fahrenheit
        }
        if (binding.temperatureRadioGroup.checkedRadioButtonId != radioButton)
            binding.temperatureRadioGroup.check(radioButton)
    }

}