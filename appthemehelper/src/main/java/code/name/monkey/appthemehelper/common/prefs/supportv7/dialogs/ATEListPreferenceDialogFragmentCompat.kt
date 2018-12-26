package code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs

import android.os.Bundle
import androidx.preference.ListPreference
import android.view.View

import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEListPreference

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
class ATEListPreferenceDialogFragmentCompat : ATEPreferenceDialogFragment(), MaterialDialog.ListCallbackSingleChoice {
    private var mClickedDialogEntryIndex: Int = 0

    private val listPreference: ATEListPreference
        get() = preference as ATEListPreference

    override fun onPrepareDialogBuilder(builder: MaterialDialog.Builder) {
        super.onPrepareDialogBuilder(builder)

        val preference = listPreference

        if (preference.entries == null || preference.entryValues == null) {
            throw IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.")
        }

        mClickedDialogEntryIndex = preference.findIndexOfValue(preference.value)
        builder.items(*preference.entries)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(mClickedDialogEntryIndex, this)

        /*
         * The typical interaction for list-based dialogs is to have
         * click-on-an-item dismiss the dialog instead of the user having to
         * press 'Ok'.
         */
        builder.positiveText("")
        builder.negativeText("")
        builder.neutralText("")
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        val preference = listPreference
        if (positiveResult && mClickedDialogEntryIndex >= 0 &&
                preference.entryValues != null) {
            val value = preference.entryValues[mClickedDialogEntryIndex].toString()
            if (preference.callChangeListener(value)) {
                preference.value = value
            }
        }
    }

    override fun onSelection(dialog: MaterialDialog, itemView: View, which: Int, text: CharSequence): Boolean {
        mClickedDialogEntryIndex = which
        onClick(dialog, DialogAction.POSITIVE)
        dismiss()
        return true
    }

    companion object {

        fun newInstance(key: String): ATEListPreferenceDialogFragmentCompat {
            val fragment = ATEListPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ATEPreferenceDialogFragment.ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }
}
