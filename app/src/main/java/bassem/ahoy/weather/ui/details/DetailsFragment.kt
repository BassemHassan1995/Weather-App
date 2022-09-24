package bassem.ahoy.weather.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.toCityResponse
import bassem.ahoy.weather.databinding.FragmentDetailsBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.utils.extensions.showToast
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailsFragment : BaseFragment<FragmentDetailsBinding, DetailsEvent>() {

    override val viewModel by activityViewModels<DetailsViewModel>()
    private val navigationArgs: DetailsFragmentArgs by navArgs()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

    override fun handleEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.ErrorGettingDetailsEvent -> showToast(event.errorMessage)
        }
    }

    override fun observeData() {
        super.observeData()
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                todayWeather.flowWithLifecycle(lifecycle)
                    .collect {
                        it?.let {
                            bindDay(it.weatherDay)
                            bindCity(it.toCityResponse())
                        }
                    }
            }
        }
    }

    private fun bindDay(dayWeather: DayWeather) {
        with(binding) {
            header.visibility = View.VISIBLE
            textViewDay.text = dayWeather.weekDay
            textViewDate.text = dayWeather.date
            textViewDegree.text = dayWeather.temperature
            Glide.with(requireContext())
                .load(dayWeather.iconUrl)
                .into(imageViewIcon)
        }
    }

    private fun bindCity(cityResponse: CityResponse) {
        with(binding) {
            textViewCity.text = cityResponse.name
            val favoriteResId = if (cityResponse.isFavorite)
                R.drawable.ic_favorite_filled
            else
                R.drawable.ic_favorite_outlined
            imageViewFavorites.setImageResource(favoriteResId)
        }
    }

    override fun setupViews(view: View) {
        val latitude = navigationArgs.latitude.toDouble()
        val longitude = navigationArgs.longitude.toDouble()

        viewModel.getDayWeatherDetails(latitude, longitude)
        binding.imageViewFavorites.setOnClickListener {
            viewModel.updateFavoriteState()
        }
    }
}