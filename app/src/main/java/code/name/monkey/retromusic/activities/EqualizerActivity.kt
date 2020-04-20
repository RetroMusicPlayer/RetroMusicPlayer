package code.name.monkey.retromusic.activities

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.TextView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.*
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.equalizer.AudioEffects
import code.name.monkey.retromusic.helper.EqualizerHelper
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener
import code.name.monkey.retromusic.util.ViewUtil
import kotlinx.android.synthetic.main.activity_equalizer.*

/**
 * @author Hemanth S (h4h13).
 */

class EqualizerActivity : AbsMusicServiceActivity(), AdapterView.OnItemSelectedListener {

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                if (seekBar === bassBoostStrength) {
                    bassBoost.isEnabled = progress > 0
                    EqualizerHelper.instance!!.bassBoostStrength = progress
                    EqualizerHelper.instance!!.isBassBoostEnabled = progress > 0
                } else if (seekBar === virtualizerStrength) {
                    virtualizer.isEnabled = progress > 0
                    EqualizerHelper.instance!!.isVirtualizerEnabled = progress > 0
                    EqualizerHelper.instance!!.virtualizerStrength = progress
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {

        }
    }

    private lateinit var presetsNamesAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equalizer)

        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        setupToolbar()

        equalizerSwitch.isChecked = AudioEffects.areAudioEffectsEnabled()
        val widgetColor = MaterialValueHelper.getPrimaryTextColor(
            this,
            ColorUtil.isColorLight(ThemeStore.accentColor(this))
        )
        equalizerSwitch.setTextColor(widgetColor)
        TintHelper.setTintAuto(equalizerSwitch, ThemeStore.accentColor(this), false)
        equalizerSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            when (buttonView.id) {
                R.id.equalizerSwitch -> {
                    AudioEffects.setAudioEffectsEnabled(isChecked)
                    TransitionManager.beginDelayedTransition(content)
                    content.visibility = if (isChecked) View.VISIBLE else View.GONE
                }
            }
        }

        presetsNamesAdapter =
            ArrayAdapter(this, R.layout.dropdown_item, AudioEffects.getEqualizerPresets(this))
        presets.adapter = presetsNamesAdapter
        presets.onItemSelectedListener = this
        presets.setSelection(AudioEffects.getCurrentPreset())

        bassBoostStrength.progress = AudioEffects.getBassBoostStrength().toInt()
        ViewUtil.setProgressDrawable(bassBoostStrength, ThemeStore.accentColor(this), true)
        bassBoostStrength.setOnSeekBarChangeListener(seekBarChangeListener)

        virtualizerStrength.progress = AudioEffects.getVirtualizerStrength().toInt()
        ViewUtil.setProgressDrawable(virtualizerStrength, ThemeStore.accentColor(this), true)
        virtualizerStrength.setOnSeekBarChangeListener(seekBarChangeListener)

        setupUI()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        val toolbarColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        toolbar.setBackgroundColor(toolbarColor)
        ToolbarContentTintHelper.colorBackButton(toolbar)
        setSupportActionBar(toolbar)
    }


    private fun setupUI() {
        frequencyBands.removeAllViews()
        // get number of supported bands

        try {
            val bands: Short = AudioEffects.getNumberOfBands()
            val range: ShortArray = AudioEffects.getBandLevelRange()

            // for each of the supported bands, we will set up a slider from -10dB to 10dB boost/attenuation,
            // as well as text labels to assist the user
            for (band in 0 until bands) {

                val view = LayoutInflater.from(this)
                    .inflate(R.layout.retro_seekbar, frequencyBands, false)
                val freqTextView = view.findViewById<TextView>(R.id.hurtz)
                freqTextView.text =
                    String.format("%d Hz", EqualizerHelper.instance!!.getCenterFreq(band) / 1000)

                val bar = view.findViewById<SeekBar>(R.id.seekbar)
                ViewUtil.setProgressDrawable(bar, ThemeStore.accentColor(this), true)
                val seekBarMax = range[1] - range[0]
                bar.max = seekBarMax
                println("AudioEffect ${AudioEffects.getBandLevel(band.toShort())}")
                bar.progress = EqualizerHelper.instance!!.getBandLevel(band)

                bar.setOnSeekBarChangeListener(object : SimpleOnSeekbarChangeListener() {
                    override fun onProgressChanged(
                        seekBar: SeekBar,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            //val level: Short = (progress + AudioEffects.getBandLevelRange(0)).toShort()
                            //AudioEffects.setBandLevel(band.toShort(), level)
                            EqualizerHelper.instance!!.setBandLevel(
                                band, progress + EqualizerHelper.instance!!.bandLevelLow
                            )
                            println("AudioEffect: ${progress + EqualizerHelper.instance!!.bandLevelLow}")
                            presets.setSelection(0)
                        }
                    }
                })

                frequencyBands.addView(view)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        println("AudioEffect: $position")
        AudioEffects.usePreset((position).toShort())
        //EqualizerHelper.instance!!.equalizer.usePreset((position - 1).toShort())
        setupUI()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }
}