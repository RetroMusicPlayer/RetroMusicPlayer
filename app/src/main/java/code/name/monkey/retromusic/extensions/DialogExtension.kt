package code.name.monkey.retromusic.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.R
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.materialDialog(title: Int): MaterialAlertDialogBuilder {
    return if (BuildConfig.DEBUG) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialogTheme
        )
    } else {
        MaterialAlertDialogBuilder(
            requireContext()
        )
    }.setTitle(title)
}

fun AlertDialog.colorButtons(): AlertDialog {
    setOnShowListener {
        getButton(AlertDialog.BUTTON_POSITIVE).accentTextColor()
        getButton(AlertDialog.BUTTON_NEGATIVE).accentTextColor()
        getButton(AlertDialog.BUTTON_NEUTRAL).accentTextColor()
    }
    return this
}

fun Fragment.materialDialog(): MaterialDialog {
    return MaterialDialog(requireContext())
        .cornerRadius(res = R.dimen.m3_dialog_corner_size)
}
