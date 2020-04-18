package code.name.monkey.retromusic.equalizer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Band

/**
 * Created by 1100416 on 2018. 1. 11..
 */
class FreqLevelItem @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var band: Band? = null
    private var mSeekBar: SeekBar? = null
    private var mCenterFreq: TextView? = null
    private var mLevel: TextView? = null

    interface OnBandLevelChangeListener {
        fun onBandLevelChange(band: View?, level: Short)
        fun onBandLevelChangeStop()
    }

    var mBandLevelChangeListener: OnBandLevelChangeListener? = null
    private fun initView() {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.freq_level_item, this, false)
        mCenterFreq = rootView.findViewById(R.id.centerFreq)

        mLevel = rootView.findViewById(R.id.currentLevel)
        mSeekBar = rootView.findViewById(R.id.seekBar)
        mSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                i: Int,
                b: Boolean
            ) {
                if (band != null) {
                    mLevel?.text = "${i + band!!.rangeMin}"
                } else {
                    mLevel?.text = "$i"
                }
                if (mBandLevelChangeListener != null) {
                    mBandLevelChangeListener!!.onBandLevelChange(
                        this@FreqLevelItem,
                        (i + band!!.rangeMin).toShort()
                    )
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (mBandLevelChangeListener != null) {
                    mBandLevelChangeListener!!.onBandLevelChangeStop()
                }
            }
        })
        addView(rootView)
    }

    fun setLevelInfo(band: Band?) {
        this.band = band
        mCenterFreq!!.text = displayNameOfHz(band!!.centerFreq)

        mSeekBar!!.max = band.rangeMax - band.rangeMin
        mSeekBar!!.progress = band.level - band.rangeMin
    }

    private fun displayNameOfHz(freq: Int): String {
        var display = freq.toString() + "mHz"
        when {
            1000 * 1000 * 1000 < freq -> {
                display = String.format("%.1f", freq / 1000 / 1000f) + "MHz"
            }
            1000 * 1000 < freq -> {
                display = String.format("%.1f", freq / 1000 / 1000f) + "KHz"
            }
            1000 < freq -> {
                display = String.format("%d", freq / 1000) + "Hz"
            }
        }
        return display
    }

    fun setBandLevel(level: Short) {
        band!!.level = level
        setLevelInfo(band)
    }

    fun setBandLevelChangeListener(listener: OnBandLevelChangeListener?) {
        mBandLevelChangeListener = listener
    }

    init {
        initView()
    }
}