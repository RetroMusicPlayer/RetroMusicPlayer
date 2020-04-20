package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.view.MenuItem
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.fragments.EqualizerFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote.audioSessionId
import kotlinx.android.synthetic.main.activity_about.*

class TestActivity : AbsMusicServiceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)

        val toolbarColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        toolbar.setBackgroundColor(toolbarColor)
        ToolbarContentTintHelper.colorBackButton(toolbar)
        setSupportActionBar(toolbar)

        val equalizerFragment = EqualizerFragment.newInstance(audioSessionId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, equalizerFragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}