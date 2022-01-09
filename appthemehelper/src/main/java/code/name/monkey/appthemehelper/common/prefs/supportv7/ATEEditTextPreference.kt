package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.content.Context
import android.util.AttributeSet
import androidx.preference.EditTextPreference

class ATEEditTextPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : EditTextPreference(context, attrs, defStyleAttr, defStyleRes) {
}
/*{

    init {
        if (summary == null || summary.toString().trim { it <= ' ' }.isEmpty()) {
            summary = "%s"
        }
    }
}*/