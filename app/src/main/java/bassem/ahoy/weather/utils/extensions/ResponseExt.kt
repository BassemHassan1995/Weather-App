package bassem.ahoy.weather.utils.extensions

import bassem.ahoy.weather.data.model.ApiError
import com.squareup.moshi.Moshi
import retrofit2.Response

fun <T> Response<T>.getApiError(): ApiError? {
    val moshi = Moshi.Builder().build()
    val adapter = moshi.adapter(ApiError::class.java)
    val errorJsonString = this.errorBody()?.charStream()?.readText().orEmpty()
    return adapter.fromJson(errorJsonString)
}