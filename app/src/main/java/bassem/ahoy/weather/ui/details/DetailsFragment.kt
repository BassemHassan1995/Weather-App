package bassem.ahoy.weather.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.data.model.toCityResponse
import bassem.ahoy.weather.databinding.FragmentDetailsBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.utils.extensions.showToast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding, DetailsEvent>() {

    override val viewModel by viewModels<DetailsViewModel>()
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
                            binding.textViewCity.text = it.toCityResponse().name
                        }
                    }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                isFavorite.flowWithLifecycle(lifecycle)
                    .collect {
                        val favoriteResId = if (it)
                            R.drawable.ic_favorite_filled
                        else
                            R.drawable.ic_favorite_outlined
                        binding.imageViewFavorites.setImageResource(favoriteResId)
                    }
            }
        }
    }

    private fun bindDay(dayWeather: DayWeather) {
        with(binding) {
            val degreeUnitResId = when (dayWeather.unit) {
                DegreeUnit.CELSIUS -> R.string.temperature_c
                DegreeUnit.FAHRENHEIT -> R.string.temperature_f
            }
            header.visibility = View.VISIBLE
            textViewDay.text = dayWeather.weekDay
            textViewDate.text = dayWeather.date
            textViewDegree.text = getString(degreeUnitResId, dayWeather.temperature)
            Glide.with(requireContext())
                .load(dayWeather.iconUrl)
                .into(imageViewIcon)
        }
    }

    override fun setupViews() {
        val latitude = navigationArgs.latitude.toDouble()
        val longitude = navigationArgs.longitude.toDouble()

        viewModel.getDayWeatherDetails(latitude, longitude)
        binding.imageViewFavorites.setOnClickListener {
            viewModel.updateFavoriteState()
        }
    }
}