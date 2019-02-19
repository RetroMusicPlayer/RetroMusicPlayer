package code.name.monkey.retromusic.ui.fragments.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.preferences.*
import code.name.monkey.retromusic.util.NavigationUtil

/**
 * @author Hemanth S (h4h13).
 */

abstract class AbsSettingsFragment : PreferenceFragmentCompat() {
    internal fun showProToastAndNavigate(message: String) {
        Toast.makeText(context, "$message is Pro version feature.", Toast.LENGTH_SHORT).show()

        NavigationUtil.goToProVersion(activity!!)
    }

    protected fun setSummary(preference: Preference) {
        setSummary(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.context)
                .getString(preference.key, "")!!)
    }

    internal fun setSummary(preference: Preference, value: Any) {
        val stringValue = value.toString()
        if (preference is ListPreference) {
            val index = preference.findIndexOfValue(stringValue)
            preference.setSummary(if (index >= 0) preference.entries[index] else null)
        } else {
            preference.summary = stringValue
        }
    }

    abstract fun invalidateSettings()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
        listView.setBackgroundColor(ThemeStore.primaryColor(context!!))
        listView.overScrollMode = View.OVER_SCROLL_NEVER
        listView.setPadding(0, 0, 0, 0)
        listView.setPaddingRelative(0, 0, 0, 0)
        invalidateSettings()
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        var dialogFragment: DialogFragment? = null
        if (preference is NowPlayingScreenPreference) {
            dialogFragment = NowPlayingScreenPreferenceDialog.newInstance(preference.key);
        } else if (preference is AlbumCoverStylePreference) {
            dialogFragment = AlbumCoverStylePreferenceDialog.newInstance(preference.key);
        }
        if (preference is MaterialListPreference) {
            val entries = preference.entries

            dialogFragment = MaterialListPreferenceDialog.newInstance(preference)
        }
        if (preference is BlacklistPreference) {
            dialogFragment = BlacklistPreferenceDialog.newInstance(preference.key)
        }
        if (dialogFragment != null) {
            // The dialog was created (it was one of our custom Preferences), show the dialog for it
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.fragmentManager, "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            // Dialog creation could not be handled here. Try with the super method.
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
