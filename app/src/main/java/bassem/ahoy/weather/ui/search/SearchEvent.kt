package bassem.ahoy.weather.ui.search

import bassem.ahoy.weather.ui.base.Event

sealed class SearchEvent : Event() {

    data class ErrorGettingResult(val errorMessage: String) : SearchEvent()

}