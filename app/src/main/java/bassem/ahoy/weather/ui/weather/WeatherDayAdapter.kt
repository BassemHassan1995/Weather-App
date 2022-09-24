package bassem.ahoy.weather.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import bassem.ahoy.weather.R
import bassem.ahoy.weather.data.model.DayWeather
import bassem.ahoy.weather.data.model.DegreeUnit
import bassem.ahoy.weather.databinding.DayWeatherItemBinding
import bassem.ahoy.weather.databinding.ItemCollapsedBinding
import bassem.ahoy.weather.databinding.ItemExpandedBinding
import com.bumptech.glide.Glide

class WeatherDayAdapter :
    ListAdapter<DayWeather, WeatherDayAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: DayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dayWeather: DayWeather) {
            with(binding) {
                layoutCollapsed.root.setOnClickListener {
                    layoutExpanded.root.visibility = if (layoutExpanded.root.isVisible)
                        View.GONE
                    else
                        View.VISIBLE
                }
                renderCollapsedView(layoutCollapsed, dayWeather)
                renderExpandedView(layoutExpanded, dayWeather)
            }
        }

        private fun renderExpandedView(
            layoutExpanded: ItemExpandedBinding,
            dayWeather: DayWeather
        ) = with(layoutExpanded) {
            textViewSunrise.text = dayWeather.sunrise
            textViewSunset.text = dayWeather.sunset
            textViewHumidity.text = dayWeather.humidity
            textViewSpeed.text = dayWeather.windSpeed

        }

        private fun renderCollapsedView(
            layoutCollapsed: ItemCollapsedBinding,
            dayWeather: DayWeather
        ) = with(layoutCollapsed) {
            textViewDegree.text = dayWeather.temperature
            textViewDay.text = dayWeather.weekDay
            textViewDate.text = dayWeather.date
            textViewUnit.setText(
                when (dayWeather.unit) {
                    DegreeUnit.CELSIUS -> R.string.degree_c
                    DegreeUnit.FAHRENHEIT -> R.string.degree_f
                }
            )
            Glide.with(ivIconUrl.context)
                .load(dayWeather.iconUrl)
                .into(ivIconUrl)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =

        ViewHolder(
            DayWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

    companion object DiffCallback : DiffUtil.ItemCallback<DayWeather>() {
        override fun areItemsTheSame(
            oldItem: DayWeather,
            newItem: DayWeather
        ): Boolean {
            return oldItem.weekDay == newItem.weekDay
        }

        override fun areContentsTheSame(
            oldItem: DayWeather,
            newItem: DayWeather
        ): Boolean {
            return oldItem.temperature == newItem.temperature
        }
    }
}