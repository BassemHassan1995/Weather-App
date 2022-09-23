package bassem.ahoy.weather.ui.weather

import android.location.Location
import bassem.ahoy.weather.ui.base.BaseViewModel
import com.google.android.gms.tasks.OnSuccessListener

class WeatherViewModel : BaseViewModel<WeatherEvent>(), OnSuccessListener<Location?> {

    init {
        checkCurrentLocation()
    }

    private fun getData(location: Location) {
        sendEvent(WeatherEvent.UpdateLocationEvent(location))
        //TODO: Get week forecast of the location
    }

    private fun checkCurrentLocation() = sendEvent(WeatherEvent.GetLocationEvent)

    override fun onSuccess(location: Location?) {
        if (location == null) {
            sendEvent(WeatherEvent.NoLocationDetectedEvent)
        } else {
            getData(location)
        }
    }

}