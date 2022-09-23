package bassem.ahoy.weather.utils.extensions

import android.text.format.DateUtils
import java.text.SimpleDateFormat

fun Int.getDate(): String =
    SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG).format(this * 1000L).split(",")[0]

fun Int.getTime(): String =
    SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(this * 1000L)

fun Int.getWeekDay(): String = when (DateUtils.isToday(this * 1000L)) {
    true -> "Today"     //This value is not recommended to be hardcoded because it will cause issues in localizations
    false -> SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL).format(this * 1000L)
        .split(",")[0]
}

