package code.name.monkey.retromusic.fragments.settings

import android.graphics.Color
import android.graphics.Color.BLUE
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.TwoStatePreference
import code.name.monkey.appthemehelper.*
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEColorPreference
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser


/**
 * @author Hemanth S (h4h13).
 */

class ThemeSettingsFragment : AbsSettingsFragment() {

    override fun invalidateSettings() {
        val primaryColorPref: ATEColorPreference = findPreference("primary_color")!!
        primaryColorPref.isVisible = PreferenceUtil.getInstance().generalTheme == code.name.monkey.retromusic.R.style.Theme_RetroMusic_Color
        val primaryColor = ThemeStore.primaryColor(activity!!)
        primaryColorPref.setColor(primaryColor, ColorUtil.darkenColor(primaryColor))
        primaryColorPref.setOnPreferenceClickListener {
            MaterialDialog(activity!!).show {
                title(code.name.monkey.retromusic.R.string.primary_color)
                positiveButton(R.string.set)
                colorChooser(initialSelection = BLUE, allowCustomArgb = true, colors = PRIMARY_COLORS, subColors = PRIMARY_COLORS_SUB) { _, color ->
                    val theme = if (ColorUtil.isColorLight(color))
                        PreferenceUtil.getThemeResFromPrefValue("light")
                    else
                        PreferenceUtil.getThemeResFromPrefValue("dark")

                    ThemeStore.editTheme(context).activityTheme(theme).primaryColor(color).commit()

                    if (VersionUtils.hasNougatMR())
                        DynamicShortcutManager(context).updateDynamicShortcuts()
                    activity!!.recreate()
                }
            }
            true
        }

        val generalTheme: Preference = findPreference("general_theme")!!
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
                "dark" -> ThemeStore.editTheme(context!!).primaryColor(ContextCompat.getColor(context!!, code.name.monkey.retromusic.R.color.md_grey_900)).commit()
                "color" -> ThemeStore.editTheme(context!!).primaryColor(ContextCompat.getColor(context!!, code.name.monkey.retromusic.R.color.md_blue_grey_800)).commit()
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

        val accentColorPref: ATEColorPreference = findPreference("accent_color")!!
        val accentColor = ThemeStore.accentColor(activity!!)
        accentColorPref.setColor(accentColor, ColorUtil.darkenColor(accentColor))

        accentColorPref.setOnPreferenceClickListener {
            MaterialDialog(activity!!).show {
                title(R.string.accent_color)
                positiveButton(R.string.set)
                colorChooser(colors = ACCENT_COLORS, allowCustomArgb = true, subColors = ACCENT_COLORS_SUB) { _, color ->
                    /*var colorFinal = Color.BLACK;
                    if (!ColorUtil.isColorSaturated(color)) {
                        colorFinal = color
                    }*/
                    ThemeStore.editTheme(context).accentColor(color).commit()
                    if (VersionUtils.hasNougatMR())
                        DynamicShortcutManager(context).updateDynamicShortcuts()
                    activity!!.recreate()
                }
            }
            return@setOnPreferenceClickListener true
        }

        val colorAppShortcuts: TwoStatePreference = findPreference("should_color_app_shortcuts")!!
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
