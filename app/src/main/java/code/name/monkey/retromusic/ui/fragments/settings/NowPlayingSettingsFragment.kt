package code.name.monkey.retromusic.ui.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.TwoStatePreference
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.util.PreferenceUtil

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

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.pref_now_playing_screen)
    }

    private fun updateNowPlayingScreenSummary() {

        findPreference("now_playing_screen_id").setSummary(PreferenceUtil.getInstance().nowPlayingScreen.titleRes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            PreferenceUtil.NOW_PLAYING_SCREEN_ID -> updateNowPlayingScreenSummary()
            PreferenceUtil.ALBUM_COVER_STYLE -> updateAlbumCoverStyleSummary()
            PreferenceUtil.CIRCULAR_ALBUM_ART, PreferenceUtil.CAROUSEL_EFFECT -> invalidateSettings()
        }
    }

    private fun updateAlbumCoverStyleSummary() {
        findPreference("album_cover_style_id").setSummary(PreferenceUtil.getInstance().albumCoverStyle.titleRes)
    }
}
