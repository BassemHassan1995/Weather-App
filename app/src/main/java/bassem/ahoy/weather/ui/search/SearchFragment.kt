package bassem.ahoy.weather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.databinding.FragmentSearchBinding
import bassem.ahoy.weather.ui.base.BaseFragment
import bassem.ahoy.weather.utils.extensions.showToast
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchEvent>() {

    override val viewModel by activityViewModels<SearchViewModel>()

    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CityAdapter {
            openCityDetails(it)
        }
    }

    private fun openCityDetails(city: CityResponse) =
        navigateTo(
            SearchFragmentDirections.actionSearchToDetails(
                city.lat.toFloat(),
                city.lon.toFloat()
            )
        )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding =
        FragmentSearchBinding.inflate(inflater, container, false)

    override fun observeData() {
        super.observeData()

        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                cities.flowWithLifecycle(lifecycle)
                    .collect {
                        adapter.submitList(it)
                    }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                isLoading.flowWithLifecycle(lifecycle)
                    .collect {
                        binding.progressBar.visibility = when (it) {
                            true -> View.VISIBLE
                            false -> View.GONE
                        }
                    }
            }
        }
    }

    override fun handleEvent(event: SearchEvent) = when (event) {
        is SearchEvent.ErrorGettingResult -> showToast(event.errorMessage)
    }

    override fun setupViews(view: View) {
        with(binding) {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotBlank())
                            viewModel.searchForCity(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean = false

            })

            recyclerViewSearch.adapter = adapter
        }
    }
}