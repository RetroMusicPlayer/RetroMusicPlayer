package code.name.monkey.retromusic.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringRes), duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}