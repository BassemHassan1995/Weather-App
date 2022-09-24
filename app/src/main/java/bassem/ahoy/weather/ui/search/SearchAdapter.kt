package bassem.ahoy.weather.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.databinding.SearchItemBinding

class SearchAdapter :
    ListAdapter<CityResponse, SearchAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cityResponse: CityResponse) {
            binding.tvCityName.text = "${cityResponse.name}, ${cityResponse.country}"
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =

        ViewHolder(
            SearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object DiffCallback : DiffUtil.ItemCallback<CityResponse>() {
        override fun areItemsTheSame(
            oldItem: CityResponse,
            newItem: CityResponse
        ): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(
            oldItem: CityResponse,
            newItem: CityResponse
        ): Boolean = oldItem.name == newItem.name
    }
}