package bassem.ahoy.weather.utils.extensions

import android.location.Location
import bassem.ahoy.weather.data.model.Coord

fun Location.toCoord() : Coord = Coord(lat = latitude, lon = longitude)