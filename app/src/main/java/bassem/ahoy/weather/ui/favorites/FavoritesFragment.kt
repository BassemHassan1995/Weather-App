package bassem.ahoy.weather.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import bassem.ahoy.weather.databinding.FragmentFavoritesBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.ui.base.BaseViewModel
import bassem.ahoy.weather.ui.base.Event

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, Event>() {

    override val viewModel: BaseViewModel<Event>
        get() = TODO("Not yet implemented")

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun observeData(event: Event) {
        TODO("Not yet implemented")
    }

}