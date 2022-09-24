package bassem.ahoy.weather.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.toCityResponse
import bassem.ahoy.weather.databinding.FragmentWeatherBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.utils.extensions.showSnackbar
import bassem.ahoy.weather.utils.extensions.showToast
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding, WeatherEvent>() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var adapter: WeatherDayAdapter

    override val viewModel by activityViewModels<WeatherViewModel>()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeatherBinding =
        FragmentWeatherBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = WeatherDayAdapter()
    }

    override fun setupViews(view: View) {
        setupPermission()
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false

                viewModel.onRefresh()
            }
            recyclerViewWords.adapter = adapter
            imageViewFavorites.setOnClickListener {
                viewModel.updateFavoriteState()
            }
        }
    }

    override fun observeData() {
        super.observeData()

        with(viewModel)
        {
            viewLifecycleOwner.lifecycleScope.launch {
                isLoading.flowWithLifecycle(lifecycle)
                    .collect {
                        binding.progressBar.visibility = when (it) {
                            true -> View.VISIBLE
                            false -> View.GONE
                        }
                    }

            }
            viewLifecycleOwner.lifecycleScope.launch {
                currentForecast.flowWithLifecycle(lifecycle)
                    .collect {
                        it?.let {
                            renderDays(it.weatherDays)
                            renderCurrentCity(it.toCityResponse())
                        }
                    }
            }
        }
    }

    private fun renderDays(dayWeathers: List<DayWeather>) {
        adapter.submitList(dayWeathers)
        if (dayWeathers.isNotEmpty())
            bindFirstDay(dayWeathers.first())
    }

    private fun renderCurrentCity(cityResponse: CityResponse) {
        with(binding) {
            textViewCity.text = cityResponse.name
            val favoriteResId = if (cityResponse.isFavorite)
                R.drawable.ic_favorite_filled
            else
                R.drawable.ic_favorite_outlined
            imageViewFavorites.setImageResource(favoriteResId)
        }

    }

    override fun handleEvent(event: WeatherEvent) = when (event) {
        WeatherEvent.NoLocationDetectedEvent -> showError(R.string.no_location_detected)
        WeatherEvent.GetLocationEvent -> checkPermissions()
        is WeatherEvent.ErrorGettingForecastEvent -> showToast(event.errorMessage)
    }

    private fun bindFirstDay(first: DayWeather) {
        with(binding) {
            header.visibility = View.VISIBLE
            textViewDay.text = first.weekDay
            textViewDate.text = first.date
            textViewDegree.text = first.temperature
            Glide.with(requireContext())
                .load(first.iconUrl)
                .into(imageViewIcon)
        }
    }

    private fun showError(@StringRes stringRes: Int) {
        binding.progressBar.visibility = View.GONE
        showToast(stringRes)
    }

    private fun setupPermission() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            when (isGranted) {
                true -> checkPermissions()
                false -> showError(R.string.permission_denied_explanation)
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ->
                getCurrentLocation()
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                binding.progressBar.visibility = View.GONE
                showSnackbar(R.string.permission_rationale) { requestPermission() }
            }
            else -> {
                requestPermission()
            }
        }
    }

    private fun requestPermission() =
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener(viewModel)
    }

}