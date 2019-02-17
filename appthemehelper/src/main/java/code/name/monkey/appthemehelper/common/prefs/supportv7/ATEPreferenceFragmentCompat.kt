package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.annotation.SuppressLint
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEEditTextPreferenceDialogFragmentCompat
import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEListPreferenceDialogFragmentCompat
import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEPreferenceDialogFragment

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
@SuppressLint("RestrictedApi")
abstract class ATEPreferenceFragmentCompat : PreferenceFragmentCompat() {

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (this.callbackFragment is PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) {
            (this.callbackFragment as PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback).onPreferenceDisplayDialog(this, preference)
            return
        }

        if (this.activity is PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) {
            (this.activity as PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback).onPreferenceDisplayDialog(this, preference)
            return
        }

        if (this.fragmentManager!!.findFragmentByTag("android.support.v7.preference.PreferenceFragment.DIALOG") == null) {
            val dialogFragment = onCreatePreferenceDialog(preference)

            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(this.fragmentManager!!, "android.support.v7.preference.PreferenceFragment.DIALOG")
                return
            }
        }

        super.onDisplayPreferenceDialog(preference)
    }

    open fun onCreatePreferenceDialog(preference: Preference): DialogFragment? {
        if (preference is ATEEditTextPreference) {
            return ATEEditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey())
        } else if (preference is ATEListPreference) {
            return ATEListPreferenceDialogFragmentCompat.newInstance(preference.getKey())
        } else if (preference is ATEDialogPreference) {
            return ATEPreferenceDialogFragment.newInstance(preference.getKey())
        }
        return null
    }
}
