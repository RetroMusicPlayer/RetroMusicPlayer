package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.equalizer.FreqLevelItem
import code.name.monkey.retromusic.equalizer.MainContract
import code.name.monkey.retromusic.equalizer.MainPresenter
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Band
import kotlinx.android.synthetic.main.activity_equalizer.*


class EqualizerActivity : AbsMusicServiceActivity(), MainContract.View {

    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setLightNavigationBar(true)

        val toolbarColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        toolbar.setBackgroundColor(toolbarColor)
        ToolbarContentTintHelper.colorBackButton(toolbar)
        setSupportActionBar(toolbar)

        equalizerToggle.setOnCheckedChangeListener { _, isChecked ->
            mainPresenter.effectOnOff(isChecked)
        }

        mainPresenter = MainPresenter(this)
        mainPresenter.initEqualizer()

        val accentColor = ThemeStore.accentColor(this)
        val textColor = MaterialValueHelper.getPrimaryTextColor(
            this,
            ColorUtil.isColorLight(accentColor)
        )
        equalizerToggle.setBackgroundColor(accentColor)
        equalizerToggle.setTextColor(textColor)

    }

    override fun onStop() {
        super.onStop()
        mainPresenter.detachView()
    }

    override fun showEmptyView() {

    }


    override fun showBandInfo(bands: Array<Band?>) {
        bandList.removeAllViews()
        for (i in bands.indices) {
            val band = bands[i]
            val freqLevelItem = FreqLevelItem(this, null, 0)
            freqLevelItem.setLevelInfo(band)
            freqLevelItem.id = i
            bandList.addView(freqLevelItem, i)
        }
    }

    override fun showPresetList(presetNames: Array<String?>?) {
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            R.layout.dropdown_item,
            presetNames!!
        )
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_item)
        preset.adapter = spinnerAdapter
        preset.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("eq", "onItemSelected position $position, id $id")
                mainPresenter.changePreset(position.toShort())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("eq", "onNothingSelected")
            }
        }
    }

    override fun showBandLevel(levels: ShortArray) {
        val count = bandList.childCount
        for (i in 0 until count) {
            val item: View = bandList.getChildAt(i)
            if (item is FreqLevelItem) {
                val freqLevelItem = item as FreqLevelItem
                freqLevelItem.setBandLevel(levels[i])
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        mainPresenter.changeAudioSession(MusicPlayerRemote.audioSessionId)
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        mainPresenter.changeAudioSession(MusicPlayerRemote.audioSessionId)
    }
}