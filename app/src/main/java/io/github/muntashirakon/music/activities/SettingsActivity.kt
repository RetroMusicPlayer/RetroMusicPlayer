package io.github.muntashirakon.music.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.activities.base.AbsBaseActivity
import io.github.muntashirakon.music.appshortcuts.DynamicShortcutManager
import io.github.muntashirakon.music.extensions.applyToolbar
import io.github.muntashirakon.music.fragments.settings.MainSettingsFragment
import com.afollestad.materialdialogs.color.ColorChooserDialog
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AbsBaseActivity(), ColorChooserDialog.ColorCallback {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)
        setupToolbar()

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.contentFrame, MainSettingsFragment())
                .commit()
        }
    }

    private fun setupToolbar() {
        setTitle(R.string.action_settings)
        applyToolbar(toolbar)
    }

    fun setupFragment(fragment: Fragment, @StringRes titleName: Int) {
        val fragmentTransaction = fragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.sliding_in_left,
                R.anim.sliding_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        fragmentTransaction.replace(R.id.contentFrame, fragment, fragment.tag)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        setTitle(titleName)
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            setTitle(R.string.action_settings)
            fragmentManager.popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG: String = "SettingsActivity"
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        when (dialog.title) {
            R.string.accent_color -> {
                ThemeStore.editTheme(this).accentColor(selectedColor).commit()
                if (VersionUtils.hasNougatMR())
                    DynamicShortcutManager(this).updateDynamicShortcuts()
            }
        }
        recreate()
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {

    }
}
