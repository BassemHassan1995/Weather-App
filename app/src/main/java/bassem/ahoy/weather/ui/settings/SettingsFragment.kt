package bassem.ahoy.weather.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import bassem.ahoy.weather.databinding.FragmentSettingsBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.ui.base.Event

class SettingsFragment : BaseFragment<FragmentSettingsBinding, Event>() {

    override val viewModel: BaseViewModel<Event>
        get() = TODO("Not yet implemented")

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(inflater, container, false)

    override fun observeData(event: Event) {
        TODO("Not yet implemented")
    }

}