package code.name.monkey.retromusic.ui.fragments.settings

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.preference.TwoStatePreference
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEColorPreference
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.color.ColorChooserDialog

/**
 * @author Hemanth S (h4h13).
 */

class ThemeSettingsFragment : AbsSettingsFragment() {

    override fun invalidateSettings() {
        val primaryColorPref = findPreference("primary_color") as ATEColorPreference
        primaryColorPref.isVisible = PreferenceUtil.getInstance().generalTheme == R.style.Theme_RetroMusic_Color
        val primaryColor = ThemeStore.primaryColor(activity!!)
        primaryColorPref.setColor(primaryColor, ColorUtil.darkenColor(primaryColor))
        primaryColorPref.setOnPreferenceClickListener {
            ColorChooserDialog.Builder(activity!!, R.string.primary_color)
                    .accentMode(false)
                    .allowUserColorInput(true)
                    .allowUserColorInputAlpha(false)
                    .preselect(primaryColor)
                    .show(activity!!)
            true
        }

        val generalTheme = findPreference("general_theme")
        setSummary(generalTheme)
        generalTheme.setOnPreferenceChangeListener { _, newValue ->
            val theme = newValue as String

            if (theme == "color" && !App.isProVersion) {
                primaryColorPref.isVisible = false
                showProToastAndNavigate("Color theme")
                return@setOnPreferenceChangeListener false
            } else {
                primaryColorPref.isVisible = true
            }

            setSummary(generalTheme, newValue)


            when (theme) {
                "light" -> ThemeStore.editTheme(context!!).primaryColor(Color.WHITE).commit()
                "black" -> ThemeStore.editTheme(context!!).primaryColor(Color.BLACK).commit()
                "dark" -> ThemeStore.editTheme(context!!).primaryColor(ContextCompat.getColor(context!!, R.color.md_grey_900)).commit()
                "color" -> ThemeStore.editTheme(context!!).primaryColor(ContextCompat.getColor(context!!, R.color.md_blue_grey_800)).commit()
            }

            ThemeStore.editTheme(activity!!)
                    .activityTheme(PreferenceUtil.getThemeResFromPrefValue(theme))
                    .commit()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                activity!!.setTheme(PreferenceUtil.getThemeResFromPrefValue(theme))
                DynamicShortcutManager(activity!!).updateDynamicShortcuts()
            }
            activity!!.recreate()
            //invalidateSettings();
            true
        }

        val accentColorPref = findPreference("accent_color") as ATEColorPreference
        val accentColor = ThemeStore.accentColor(activity!!)
        accentColorPref.setColor(accentColor, ColorUtil.darkenColor(accentColor))

        accentColorPref.setOnPreferenceClickListener {
            ColorChooserDialog.Builder(context!!, R.string.accent_color)
                    .accentMode(true)
                    .allowUserColorInput(true)
                    .allowUserColorInputAlpha(false)
                    .preselect(accentColor)
                    .show(activity!!)
            true
        }

        val colorAppShortcuts = findPreference("should_color_app_shortcuts") as TwoStatePreference
        if (!VersionUtils.hasNougatMR()) {
            colorAppShortcuts.isVisible = false
        } else {
            colorAppShortcuts.isChecked = PreferenceUtil.getInstance().coloredAppShortcuts()
            colorAppShortcuts.setOnPreferenceChangeListener { _, newValue ->
                // Save preference
                PreferenceUtil.getInstance().setColoredAppShortcuts(newValue as Boolean)
                DynamicShortcutManager(activity!!).updateDynamicShortcuts()
                true
            }
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_general)
    }
}
