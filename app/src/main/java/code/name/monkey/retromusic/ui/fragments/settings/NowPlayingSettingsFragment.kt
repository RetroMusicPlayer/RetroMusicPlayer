package code.name.monkey.retromusic.ui.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.TwoStatePreference
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.PreferenceUtil.*

/**
 * @author Hemanth S (h4h13).
 */

class NowPlayingSettingsFragment : AbsSettingsFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun invalidateSettings() {
        updateNowPlayingScreenSummary()
        updateAlbumCoverStyleSummary()

        val carouselEffect = findPreference("carousel_effect") as TwoStatePreference
        carouselEffect.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean && !App.isProVersion) {
                showProToastAndNavigate(activity!!.getString(R.string.pref_title_toggle_carousel_effect))
                return@setOnPreferenceChangeListener false
            }
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_now_playing_screen)
    }

    private fun updateAlbumCoverStyleSummary() {
        findPreference(ALBUM_COVER_STYLE).setSummary(getInstance().albumCoverStyle.titleRes)
    }

    private fun updateNowPlayingScreenSummary() {
        findPreference(NOW_PLAYING_SCREEN_ID).setSummary(getInstance().nowPlayingScreen.titleRes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInstance().registerOnSharedPreferenceChangedListener(this)
        val preference = findPreference("album_cover_transform")
        setSummary(preference)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getInstance().unregisterOnSharedPreferenceChangedListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            NOW_PLAYING_SCREEN_ID -> updateNowPlayingScreenSummary()
            ALBUM_COVER_STYLE -> updateAlbumCoverStyleSummary()
            CIRCULAR_ALBUM_ART, CAROUSEL_EFFECT -> invalidateSettings()
        }
    }
}
