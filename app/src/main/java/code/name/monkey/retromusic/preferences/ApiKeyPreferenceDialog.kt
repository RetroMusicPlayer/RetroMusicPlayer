package code.name.monkey.retromusic.preferences

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.materialDialog

class ApiKeyPreferenceDialog : DialogFragment() {
    companion object {
        fun newInstance() : ApiKeyPreferenceDialog {
            return ApiKeyPreferenceDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog(title = R.string.pref_title_api_key)
            //.
            .create()
    }
}