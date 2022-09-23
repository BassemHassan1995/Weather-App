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
import bassem.ahoy.weather.databinding.FragmentWeatherBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.utils.extensions.showSnackbar
import bassem.ahoy.weather.utils.extensions.showToast
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
        adapter = WeatherDayAdapter {
            //TODO: Open details
        }
    }

    override fun setupViews(view: View) {
        setupPermission()
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onRefresh()
            }
            recyclerViewWords.adapter = adapter
        }
    }

    override fun observeData(event: WeatherEvent) {
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
                weatherDays.flowWithLifecycle(lifecycle)
                    .collect {
                        adapter.submitList(it)
                    }
            }
        }
        when (event) {
            WeatherEvent.NoLocationDetectedEvent -> showError(R.string.no_location_detected)
            WeatherEvent.GetLocationEvent -> checkPermissions()
            is WeatherEvent.ErrorGettingForecastEvent -> TODO()
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
                getLastKnownLocation()
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
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener(viewModel)
    }

}