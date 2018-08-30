package code.name.monkey.retromusic.ui.fragments.settings;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEColorPreference;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.VersionUtils;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager;
import code.name.monkey.retromusic.ui.activities.SettingsActivity;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * @author Hemanth S (h4h13).
 */

public class ThemeSettingsFragment extends AbsSettingsFragment {

    @Override
    public void invalidateSettings() {

        final ATEColorPreference primaryColorPref = (ATEColorPreference) findPreference(
                "primary_color");
        //noinspection ConstantConditions
        primaryColorPref.setVisible(PreferenceUtil.getInstance(getActivity()).getGeneralTheme() == R.style.Theme_RetroMusic_Color);
        final int primaryColor = ThemeStore.primaryColor(getActivity());
        primaryColorPref.setColor(primaryColor, ColorUtil.darkenColor(primaryColor));
        primaryColorPref.setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder(getActivity(), R.string.primary_color)
                    .accentMode(false)
                    .allowUserColorInput(true)
                    .allowUserColorInputAlpha(false)
                    .preselect(primaryColor)
                    .show(getActivity());
            return true;
        });

        final Preference generalTheme = findPreference("general_theme");
        setSummary(generalTheme);
        generalTheme.setOnPreferenceChangeListener((preference, newValue) -> {
            String theme = (String) newValue;

            if (theme.equals("color") && !RetroApplication.isProVersion()) {
                primaryColorPref.setVisible(false);
                showProToastAndNavigate("Color theme");
                return false;
            } else {
                primaryColorPref.setVisible(true);
            }

            setSummary(generalTheme, newValue);


            switch (theme) {
                case "light":
                    ThemeStore.editTheme(getContext()).primaryColor(Color.WHITE).commit();
                    break;
                case "black":
                    ThemeStore.editTheme(getContext()).primaryColor(Color.BLACK).commit();
                    break;
                case "dark":
                    ThemeStore.editTheme(getContext()).primaryColor(ContextCompat.getColor(getContext(), R.color.md_grey_900)).commit();
                    break;
                case "color":
                    ThemeStore.editTheme(getContext()).primaryColor(ContextCompat.getColor(getContext(), R.color.md_blue_grey_800)).commit();
                    break;
            }

            ThemeStore.editTheme(getActivity())
                    .activityTheme(PreferenceUtil.getThemeResFromPrefValue(theme))
                    .commit();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                getActivity().setTheme(PreferenceUtil.getThemeResFromPrefValue(theme));
                new DynamicShortcutManager(getActivity()).updateDynamicShortcuts();
            }
            getActivity().recreate();
            //invalidateSettings();
            return true;
        });

        ATEColorPreference accentColorPref = (ATEColorPreference) findPreference("accent_color");
        final int accentColor = ThemeStore.accentColor(getActivity());
        accentColorPref.setColor(accentColor, ColorUtil.darkenColor(accentColor));

        accentColorPref.setOnPreferenceClickListener(preference -> {
            new ColorChooserDialog.Builder(((SettingsActivity) getActivity()), R.string.accent_color)
                    .accentMode(true)
                    .allowUserColorInput(true)
                    .allowUserColorInputAlpha(false)
                    .preselect(accentColor)
                    .show(getActivity());
            return true;
        });

        TwoStatePreference colorNavBar = (TwoStatePreference) findPreference(
                "should_color_navigation_bar");
        if (!VersionUtils.hasLollipop()) {
            colorNavBar.setEnabled(false);
            colorNavBar.setSummary(R.string.pref_only_lollipop);
        } else {
            colorNavBar.setChecked(ThemeStore.coloredNavigationBar(getActivity()));
            colorNavBar.setOnPreferenceChangeListener((preference, newValue) -> {
                ThemeStore.editTheme(getActivity())
                        .coloredNavigationBar((Boolean) newValue)
                        .commit();
                getActivity().recreate();
                return true;
            });
        }

        TwoStatePreference colorAppShortcuts = (TwoStatePreference) findPreference(
                "should_color_app_shortcuts");
        if (!VersionUtils.hasNougatMR()) {
            colorAppShortcuts.setVisible(false);
        } else {
            colorAppShortcuts.setChecked(PreferenceUtil.getInstance(getActivity()).coloredAppShortcuts());
            colorAppShortcuts.setOnPreferenceChangeListener((preference, newValue) -> {
                // Save preference
                PreferenceUtil.getInstance(getActivity()).setColoredAppShortcuts((Boolean) newValue);
                // Update app shortcuts
                new DynamicShortcutManager(getActivity()).updateDynamicShortcuts();

                return true;
            });
        }

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);
    }
}
