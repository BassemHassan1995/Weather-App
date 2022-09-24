package bassem.ahoy.weather.data.api

import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.WeekForecastResponse
import bassem.ahoy.weather.utils.API_KEY
import bassem.ahoy.weather.utils.IMAGE_BASE_URL
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun getIconUrl(iconId: String): String = "${IMAGE_BASE_URL}img/wn/$iconId@4x.png"

    override suspend fun getWeekForecast(
        city: String,
        unit: String
    ): Response<WeekForecastResponse> =
        apiService.getWeekForecast(
            mapOf(
                QUERY_CITY to city,
                QUERY_UNITS to unit,
                QUERY_APP_ID to API_KEY
            )
        )

    override suspend fun getWeekForecast(
        longitude: Double,
        latitude: Double,
        unit: String
    ): Response<WeekForecastResponse> = apiService.getWeekForecast(
        mapOf(
            QUERY_LONGITUDE to longitude.toString(),
            QUERY_LATITUDE to latitude.toString(),
            QUERY_UNITS to unit,
            QUERY_APP_ID to API_KEY
        )
    )

    override suspend fun searchCity(city: String): Response<List<CityResponse>> =
        apiService.searchCity(
            mapOf(
                QUERY_CITY to city,
                QUERY_LIMIT to "5",
                QUERY_APP_ID to API_KEY
            )
        )

}

private const val QUERY_APP_ID = "appid"
private const val QUERY_CITY = "q"
private const val QUERY_LONGITUDE = "lon"
private const val QUERY_LATITUDE = "lat"
private const val QUERY_UNITS = "units"
private const val QUERY_LIMIT = "limit"
