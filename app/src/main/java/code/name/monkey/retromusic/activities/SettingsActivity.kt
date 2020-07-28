package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.appshortcuts.DynamicShortcutManager
import code.name.monkey.retromusic.extensions.applyToolbar
import com.afollestad.materialdialogs.color.ColorChooserDialog
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AbsBaseActivity(), ColorChooserDialog.ColorCallback {

    private val fragmentManager = supportFragmentManager
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)
        setupToolbar()
    }

    private fun setupToolbar() {
        setTitle(R.string.action_settings)
        applyToolbar(toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, _, _ ->
            toolbar.title = navController.currentDestination?.label
        }

        //It removes the back button
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
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
