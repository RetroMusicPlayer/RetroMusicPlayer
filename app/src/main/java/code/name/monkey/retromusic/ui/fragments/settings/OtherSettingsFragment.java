package code.name.monkey.retromusic.ui.fragments.settings;

import android.os.Bundle;

import code.name.monkey.retromusic.R;

/**
 * @author Hemanth S (h4h13).
 */

public class OtherSettingsFragment extends AbsSettingsFragment {
    @Override
    public void invalidateSettings() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_blacklist);
        addPreferencesFromResource(R.xml.pref_playlists);
        addPreferencesFromResource(R.xml.pref_advanced);
    }
}
