package bassem.ahoy.weather.data.api

import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.model.WeekForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("data/2.5/forecast/daily")
    suspend fun getWeekForecast(@QueryMap queryMap: Map<String, String>): Response<WeekForecastResponse>

    @GET("geo/1.0/direct")
    suspend fun searchCity(@QueryMap queryMap: Map<String, String>): Response<List<CityResponse>>

}