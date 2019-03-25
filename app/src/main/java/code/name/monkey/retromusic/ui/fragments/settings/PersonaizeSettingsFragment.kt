package code.name.monkey.retromusic.ui.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.TwoStatePreference
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil


class PersonaizeSettingsFragment : AbsSettingsFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun invalidateSettings() {
        val cornerWindow: TwoStatePreference = findPreference("corner_window")!!
        cornerWindow.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean && !App.isProVersion) {
                showProToastAndNavigate(activity!!.getString(R.string.pref_title_round_corners))
                return@setOnPreferenceChangeListener false
            }
            activity!!.recreate()
            return@setOnPreferenceChangeListener true
        }


        val toggleFullScreen: TwoStatePreference = findPreference("toggle_full_screen")!!
        toggleFullScreen.setOnPreferenceChangeListener { _, _ ->
            activity!!.recreate()
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_ui)
        addPreferencesFromResource(R.xml.pref_window)
        addPreferencesFromResource(R.xml.pref_lockscreen)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this)

        var preference: Preference? = findPreference("album_grid_style")
        setSummary(preference!!)
        preference = findPreference("artist_grid_style")
        setSummary(preference!!)
        preference = findPreference("home_artist_grid_style")
        setSummary(preference!!)
        preference = findPreference("tab_text_mode")
        setSummary(preference!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            PreferenceUtil.CAROUSEL_EFFECT -> invalidateSettings()
        }
    }
}
