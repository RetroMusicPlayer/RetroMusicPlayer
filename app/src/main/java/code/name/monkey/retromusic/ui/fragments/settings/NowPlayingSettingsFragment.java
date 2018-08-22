package code.name.monkey.retromusic.ui.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.TwoStatePreference;
import android.view.View;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * @author Hemanth S (h4h13).
 */

public class NowPlayingSettingsFragment extends AbsSettingsFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void invalidateSettings() {
        updateNowPlayingScreenSummary();

        final TwoStatePreference cornerWindow = (TwoStatePreference) findPreference("corner_window");
        cornerWindow.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((Boolean) newValue && !RetroApplication.isProVersion()) {
                showProToastAndNavigate(getActivity().getString(R.string.pref_title_round_corners));
                return false;
            }
            getActivity().recreate();
            return true;
        });
        final TwoStatePreference carouselEffect = (TwoStatePreference) findPreference(
                "carousel_effect");
        carouselEffect.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((Boolean) newValue && !RetroApplication.isProVersion()) {
                showProToastAndNavigate(
                        getActivity().getString(R.string.pref_title_toggle_carousel_effect));
                return false;
            }
            return true;
        });

        final TwoStatePreference toggleFullScreen = (TwoStatePreference) findPreference(
                "toggle_full_screen");
        toggleFullScreen.setOnPreferenceChangeListener((preference, newValue) -> {
            getActivity().recreate();
            return true;
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_now_playing_screen);
        addPreferencesFromResource(R.xml.pref_ui);
        addPreferencesFromResource(R.xml.pref_window);
        addPreferencesFromResource(R.xml.pref_lockscreen);
    }

    private void updateNowPlayingScreenSummary() {
        //noinspection ConstantConditions
        findPreference("now_playing_screen_id")
                .setSummary(PreferenceUtil.getInstance(getActivity()).getNowPlayingScreen().titleRes);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection ConstantConditions
        PreferenceUtil.getInstance(getContext()).registerOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //noinspection ConstantConditions
        PreferenceUtil.getInstance(getContext()).unregisterOnSharedPreferenceChangedListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceUtil.NOW_PLAYING_SCREEN_ID:
                updateNowPlayingScreenSummary();
                break;
            case PreferenceUtil.CIRCULAR_ALBUM_ART:
            case PreferenceUtil.CAROUSEL_EFFECT:
                invalidateSettings();
                break;
        }
    }
}
