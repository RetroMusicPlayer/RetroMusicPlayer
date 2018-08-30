package code.name.monkey.retromusic.ui.fragments.settings;

import android.os.Build;
import android.os.Bundle;
import androidx.preference.TwoStatePreference;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * @author Hemanth S (h4h13).
 */

public class NotificationSettingsFragment extends AbsSettingsFragment {
    @Override
    public void invalidateSettings() {
        final TwoStatePreference classicNotification = (TwoStatePreference) findPreference("classic_notification");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            classicNotification.setVisible(false);
        } else {
            classicNotification.setChecked(PreferenceUtil.getInstance(getActivity()).classicNotification());
            classicNotification.setOnPreferenceChangeListener((preference, newValue) -> {
                // Save preference
                PreferenceUtil.getInstance(getActivity()).setClassicNotification((Boolean) newValue);

                final MusicService service = MusicPlayerRemote.musicService;
                if (service != null) {
                    service.initNotification();
                    service.updateNotification();
                }

                return true;
            });
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_notification);
    }
}
