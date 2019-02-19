package code.name.monkey.retromusic.ui.fragments.settings

import android.os.Bundle
import android.view.View

import code.name.monkey.retromusic.R

/**
 * @author Hemanth S (h4h13).
 */

class OtherSettingsFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_blacklist)
        addPreferencesFromResource(R.xml.pref_playlists)
        addPreferencesFromResource(R.xml.pref_advanced)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = findPreference("last_added_interval")
        setSummary(preference)
    }
}
