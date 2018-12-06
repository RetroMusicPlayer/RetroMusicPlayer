package code.name.monkey.retromusic.ui.fragments.settings

import android.os.Build
import android.os.Bundle
import androidx.preference.TwoStatePreference

import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.PreferenceUtil

/**
 * @author Hemanth S (h4h13).
 */

class NotificationSettingsFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val classicNotification = findPreference("classic_notification") as TwoStatePreference
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            classicNotification.isVisible = false
        } else {
            classicNotification.isChecked = PreferenceUtil.getInstance().classicNotification()
            classicNotification.setOnPreferenceChangeListener { _, newValue ->
                // Save preference
                PreferenceUtil.getInstance().setClassicNotification(newValue as Boolean)

                val service = MusicPlayerRemote.musicService
                if (service != null) {
                    service.initNotification()
                    service.updateNotification()
                }

                true
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.pref_notification)
    }
}
