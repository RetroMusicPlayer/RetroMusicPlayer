package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import code.name.monkey.appthemehelper.R
import com.afollestad.materialdialogs.prefs.MaterialDialogPreference


/**
 * @author Aidan Follestad (afollestad)
 */
class ATEDialogPreference(context: Context) : MaterialDialogPreference(context) {

    init {
        init()
    }

    private fun init() {
        layoutResource = R.layout.ate_preference_custom
    }
}