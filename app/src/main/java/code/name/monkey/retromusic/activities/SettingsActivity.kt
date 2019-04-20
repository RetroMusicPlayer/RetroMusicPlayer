package code.name.monkey.retromusic.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.fragments.settings.MainSettingsFragment
import code.name.monkey.retromusic.util.PreferenceUtil
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AbsBaseActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
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
        setSupportActionBar(toolbar)
        title = null
        toolbar.apply {
            setBackgroundColor(ThemeStore.primaryColor(context))
            setNavigationOnClickListener { onBackPressed() }
            ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.textColorSecondary(context))
        }
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this))
        settingsTitle.setTextColor(ThemeStore.textColorPrimary(this))
    }

    fun setupFragment(fragment: Fragment, @StringRes titleName: Int) {
        val fragmentTransaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.sliding_in_left, R.anim.sliding_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        settingsTitle.setText(titleName)

        if (detailContentFrame == null) {
            fragmentTransaction.replace(R.id.contentFrame, fragment, fragment.tag)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            fragmentTransaction.replace(R.id.detailContentFrame, fragment, fragment.tag)
            fragmentTransaction.commit()
        }
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            settingsTitle.setText(R.string.action_settings)
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

    public override fun onPause() {
        super.onPause()
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this)
    }

    public override fun onResume() {
        super.onResume()
        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == PreferenceUtil.PROFILE_IMAGE_PATH) {
            recreate()
        }
    }


    companion object {
        const val TAG: String = "SettingsActivity"
    }
}
