package bassem.ahoy.weather.ui.details

import bassem.ahoy.weather.ui.base.Event

sealed class DetailsEvent : Event(){

    data class ErrorGettingDetailsEvent(val errorMessage: String) : DetailsEvent()
}