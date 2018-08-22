package code.name.monkey.retromusic.ui.fragments.settings;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEPreferenceFragmentCompat;
import code.name.monkey.retromusic.preferences.BlacklistPreference;
import code.name.monkey.retromusic.preferences.BlacklistPreferenceDialog;
import code.name.monkey.retromusic.preferences.NowPlayingScreenPreference;
import code.name.monkey.retromusic.preferences.NowPlayingScreenPreferenceDialog;
import code.name.monkey.retromusic.util.NavigationUtil;

/**
 * @author Hemanth S (h4h13).
 */

public abstract class AbsSettingsFragment extends ATEPreferenceFragmentCompat {
    protected void showProToastAndNavigate(String message) {
        Toast.makeText(getContext(), message + " is Pro version feature.", Toast.LENGTH_SHORT).show();
        //noinspection ConstantConditions
        NavigationUtil.goToProVersion(getActivity());
    }

    protected void setSummary(@NonNull Preference preference) {
        setSummary(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

    protected void setSummary(Preference preference, @NonNull Object value) {
        String stringValue = value.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
        } else {
            preference.setSummary(stringValue);
        }
    }

    public abstract void invalidateSettings();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDivider(new ColorDrawable(Color.TRANSPARENT));
        getListView().setBackgroundColor(ThemeStore.primaryColor(getContext()));
        getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
        getListView().setPadding(0, 0, 0, 0);
        getListView().setPaddingRelative(0, 0, 0, 0);
        invalidateSettings();
    }

    @Nullable
    @Override
    public DialogFragment onCreatePreferenceDialog(Preference preference) {
        if (preference instanceof NowPlayingScreenPreference) {
            return NowPlayingScreenPreferenceDialog.newInstance();
        } else if (preference instanceof BlacklistPreference) {
            return BlacklistPreferenceDialog.newInstance();
        }
        return super.onCreatePreferenceDialog(preference);
    }
}
