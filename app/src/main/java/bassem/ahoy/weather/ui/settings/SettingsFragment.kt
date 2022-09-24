package bassem.ahoy.weather.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.databinding.FragmentSettingsBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.base.NoEvent

class SettingsFragment : BaseFragment<FragmentSettingsBinding, NoEvent>() {

    override val viewModel by activityViewModels<SettingsViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun setupViews() {
        renderAppSettings(viewModel.degreeUnit)

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
        renderAppSettings(viewModel.degreeUnit)
    }

    override fun handleEvent(event: NoEvent) = Unit

    private fun renderAppSettings(degreeUnit: DegreeUnit) {
        val radioButton = when (degreeUnit) {
            DegreeUnit.CELSIUS -> R.id.radio_celsius
            DegreeUnit.FAHRENHEIT -> R.id.radio_fahrenheit
        }
        if (binding.temperatureRadioGroup.checkedRadioButtonId != radioButton)
            binding.temperatureRadioGroup.check(radioButton)
    }

}