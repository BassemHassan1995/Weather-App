package bassem.ahoy.weather.ui.favorites

import bassem.ahoy.weather.ui.base.Event

sealed class FavoritesEvent : Event(){

    object NoFavoritesFoundEvent : FavoritesEvent()
}