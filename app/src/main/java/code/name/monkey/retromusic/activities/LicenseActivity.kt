package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.view.MenuItem
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsBaseActivity
import code.name.monkey.retromusic.extensions.applyToolbar
import kotlinx.android.synthetic.main.activity_license.*
import kotlinx.android.synthetic.main.activity_license.appBarLayout
import kotlinx.android.synthetic.main.activity_license.toolbar
import kotlinx.android.synthetic.main.activity_playing_queue.*

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
        applyToolbar(toolbar)
        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this))
        setSupportActionBar(toolbar)
    }
}