package bassem.ahoy.weather.utils.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import bassem.ahoy.weather.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showToast(stringId: Int) {
    Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.showSnackbar(stringId: Int, function: () -> Unit) {
    Snackbar.make(
        requireView(),
        stringId,
        Snackbar.LENGTH_LONG
    )
        .setAction(R.string.action_settings) { function() }
        .show()
}