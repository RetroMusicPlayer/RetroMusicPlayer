package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.view.MenuItem
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import kotlinx.android.synthetic.main.activity_license.*

class LicenseActivity : AbsBaseActivity() {


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)


        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        license.loadUrl("file:///android_asset/index.html")
        bannerTitle.setTextColor(ThemeStore.textColorPrimary(this))
        toolbar!!.apply {
            setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            setNavigationOnClickListener { onBackPressed() }
            setBackgroundColor(ThemeStore.primaryColor(this@LicenseActivity))
        }
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this))
        title = null
        setSupportActionBar(toolbar)
        ToolbarContentTintHelper.colorBackButton(toolbar!!, ThemeStore.accentColor(this))
    }
}