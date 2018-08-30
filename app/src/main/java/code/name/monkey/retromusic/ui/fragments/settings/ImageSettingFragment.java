package code.name.monkey.retromusic.ui.fragments.settings;

import android.os.Bundle;
import androidx.preference.Preference;

import code.name.monkey.retromusic.R;

/**
 * @author Hemanth S (h4h13).
 */

public class ImageSettingFragment extends AbsSettingsFragment {
    @Override
    public void invalidateSettings() {
        final Preference autoDownloadImagesPolicy = findPreference("auto_download_images_policy");
        setSummary(autoDownloadImagesPolicy);
        autoDownloadImagesPolicy.setOnPreferenceChangeListener((preference, o) -> {
            setSummary(autoDownloadImagesPolicy, o);
            return true;
        });

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_images);
    }
}
