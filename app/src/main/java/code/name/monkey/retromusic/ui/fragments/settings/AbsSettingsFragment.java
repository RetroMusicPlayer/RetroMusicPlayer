package code.name.monkey.retromusic.ui.fragments.settings;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEPreferenceFragmentCompat;
import code.name.monkey.retromusic.preferences.AlbumCoverStylePreference;
import code.name.monkey.retromusic.preferences.AlbumCoverStylePreferenceDialog;
import code.name.monkey.retromusic.preferences.BlacklistPreference;
import code.name.monkey.retromusic.preferences.BlacklistPreferenceDialog;
import code.name.monkey.retromusic.preferences.NowPlayingScreenPreference;
import code.name.monkey.retromusic.preferences.NowPlayingScreenPreferenceDialog;
import code.name.monkey.retromusic.util.NavigationUtil;

/**
 * @author Hemanth S (h4h13).
 */

public abstract class AbsSettingsFragment extends ATEPreferenceFragmentCompat {
    void showProToastAndNavigate(String message) {
        Toast.makeText(getContext(), message + " is Pro version feature.", Toast.LENGTH_SHORT).show();
        //noinspection ConstantConditions
        NavigationUtil.goToProVersion(getActivity());
    }

    protected void setSummary(@NonNull Preference preference) {
        setSummary(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), ""));
    }

    void setSummary(Preference preference, @NonNull Object value) {
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
        getListView().setBackgroundColor(ThemeStore.Companion.primaryColor(getContext()));
        getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
        getListView().setPadding(0, 0, 0, 0);
        getListView().setPaddingRelative(0, 0, 0, 0);
        invalidateSettings();
    }

    @Nullable
    @Override
    public DialogFragment onCreatePreferenceDialog(Preference preference) {
        if (preference instanceof NowPlayingScreenPreference) {
            return NowPlayingScreenPreferenceDialog.Companion.newInstance();
        } else if (preference instanceof BlacklistPreference) {
            return BlacklistPreferenceDialog.newInstance();
        } else if (preference instanceof AlbumCoverStylePreference) {
            return AlbumCoverStylePreferenceDialog.Companion.newInstance();
        }
        return super.onCreatePreferenceDialog(preference);
    }
}
