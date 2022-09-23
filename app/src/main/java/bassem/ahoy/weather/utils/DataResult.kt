package bassem.ahoy.weather.utils

sealed class DataResult<out T> {
  data class Success<T>(val value: T): DataResult<T>()
  data class Failure(val cause: Throwable): DataResult<Nothing>()
}

inline fun <T> DataResult(block: () -> T): DataResult<T> = try {
  DataResult.Success(block())
} catch (t: Throwable) {
  DataResult.Failure(t)
}