package code.name.monkey.retromusic.ui.fragments.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import androidx.preference.Preference;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * @author Hemanth S (h4h13).
 */

public class AudioSettings extends AbsSettingsFragment {
    @Override
    public void invalidateSettings() {
        Preference findPreference = findPreference("equalizer");
        if (!hasEqualizer() && !PreferenceUtil.getInstance().getSelectedEqualizer().equals("retro")) {
            findPreference.setEnabled(false);
            findPreference.setSummary(getResources().getString(R.string.no_equalizer));
        } else {
            findPreference.setEnabled(true);
        }
        findPreference.setOnPreferenceClickListener(preference -> {
            //noinspection ConstantConditions
            NavigationUtil.openEqualizer(getActivity());
            return true;
        });


    }

    private boolean hasEqualizer() {
        final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        //noinspection ConstantConditions
        PackageManager pm = getActivity().getPackageManager();
        ResolveInfo ri = pm.resolveActivity(effects, 0);
        return ri != null;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_audio);
    }
}
