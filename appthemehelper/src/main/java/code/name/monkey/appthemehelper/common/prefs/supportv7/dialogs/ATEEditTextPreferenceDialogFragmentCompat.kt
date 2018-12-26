package code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs

import android.os.Bundle
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEEditTextPreference
import com.afollestad.materialdialogs.MaterialDialog

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
class ATEEditTextPreferenceDialogFragmentCompat : ATEPreferenceDialogFragment(), MaterialDialog.InputCallback {

    private var input: CharSequence? = null

    private val editTextPreference: ATEEditTextPreference
        get() = preference as ATEEditTextPreference

    override fun onPrepareDialogBuilder(builder: MaterialDialog.Builder) {
        super.onPrepareDialogBuilder(builder)
        builder.input("", editTextPreference.text, this)
    }

    override fun needInputMethod(): Boolean {
        return true
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val value = input!!.toString()
            if (this.editTextPreference.callChangeListener(value)) {
                this.editTextPreference.text = value
            }
        }

    }

    override fun onInput(dialog: MaterialDialog, input: CharSequence) {
        this.input = input
    }

    companion object {

        fun newInstance(key: String): ATEEditTextPreferenceDialogFragmentCompat {
            val fragment = ATEEditTextPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ATEPreferenceDialogFragment.ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }
}
