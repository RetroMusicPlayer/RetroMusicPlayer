package code.name.monkey.retromusic.ui.fragments.settings

import android.os.Bundle
import android.view.View
import code.name.monkey.retromusic.R

/**
 * @author Hemanth S (h4h13).
 */

class ImageSettingFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val autoDownloadImagesPolicy = findPreference("auto_download_images_policy")
        setSummary(autoDownloadImagesPolicy)
        autoDownloadImagesPolicy.setOnPreferenceChangeListener { _, o ->
            setSummary(autoDownloadImagesPolicy, o)
            true
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_images)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference = findPreference("auto_download_images_policy")
        setSummary(preference)
    }
}
