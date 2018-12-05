package code.name.monkey.retromusic.ui.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.preference.TwoStatePreference;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class PersonaizeSettingsFragment extends AbsSettingsFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void invalidateSettings() {
        final TwoStatePreference cornerWindow = (TwoStatePreference) findPreference("corner_window");
        cornerWindow.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((Boolean) newValue && !App.Companion.isProVersion()) {
                showProToastAndNavigate(getActivity().getString(R.string.pref_title_round_corners));
                return false;
            }
            getActivity().recreate();
            return true;
        });


        final TwoStatePreference toggleFullScreen = (TwoStatePreference) findPreference("toggle_full_screen");
        toggleFullScreen.setOnPreferenceChangeListener((preference, newValue) -> {
            getActivity().recreate();
            return true;
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_ui);
        addPreferencesFromResource(R.xml.pref_window);
        addPreferencesFromResource(R.xml.pref_lockscreen);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection ConstantConditions
        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //noinspection ConstantConditions
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceUtil.CAROUSEL_EFFECT:
                invalidateSettings();
                break;
        }
    }

}
