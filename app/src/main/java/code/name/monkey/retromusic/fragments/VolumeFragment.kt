package code.name.monkey.retromusic.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.volume.AudioVolumeObserver
import code.name.monkey.retromusic.volume.OnAudioVolumeChangedListener
import kotlinx.android.synthetic.main.fragment_volume.*

class VolumeFragment : Fragment(), SeekBar.OnSeekBarChangeListener, OnAudioVolumeChangedListener, View.OnClickListener {

    private var audioVolumeObserver: AudioVolumeObserver? = null

    private val audioManager: AudioManager?
        get() = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTintable(ThemeStore.accentColor(context!!))
        volumeDown.setOnClickListener(this)
        volumeUp.setOnClickListener(this)

        val iconColor = ATHUtil.resolveColor(context!!, R.attr.iconColor)
        volumeDown.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
        volumeUp.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
    }

    override fun onResume() {
        super.onResume()
        if (audioVolumeObserver == null) {
            audioVolumeObserver = AudioVolumeObserver(activity!!)
        }
        audioVolumeObserver!!.register(AudioManager.STREAM_MUSIC, this)

        val audioManager = audioManager
        if (audioManager != null) {
            volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
        volumeSeekBar.setOnSeekBarChangeListener(this)
    }

    override fun onAudioVolumeChanged(currentVolume: Int, maxVolume: Int) {
        if (volumeSeekBar == null) {
            return
        }

        volumeSeekBar.max = maxVolume
        volumeSeekBar.progress = currentVolume
        volumeDown.setImageResource(if (currentVolume == 0) R.drawable.ic_volume_off_white_24dp else R.drawable.ic_volume_down_white_24dp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (audioVolumeObserver != null) {
            audioVolumeObserver!!.unregister()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
        val audioManager = audioManager
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0)
        setPauseWhenZeroVolume(i < 1)
        volumeDown?.setImageResource(if (i == 0) R.drawable.ic_volume_off_white_24dp else R.drawable.ic_volume_down_white_24dp)

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }

    override fun onClick(view: View) {
        val audioManager = audioManager
        when (view.id) {
            R.id.volumeDown -> audioManager?.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0)
            R.id.volumeUp -> audioManager?.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0)
        }
    }

    fun tintWhiteColor() {
        val iconColor = Color.WHITE
        volumeDown.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
        volumeUp.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)

        ViewUtil.setProgressDrawable(volumeSeekBar, iconColor, true)

    }

    fun setTintable(color: Int) {
        ViewUtil.setProgressDrawable(volumeSeekBar, color, true)
    }

    fun removeThumb() {
        volumeSeekBar.thumb = null
    }

    private fun setPauseWhenZeroVolume(pauseWhenZeroVolume: Boolean) {
        if (PreferenceUtil.getInstance(requireContext()).pauseOnZeroVolume())
            if (MusicPlayerRemote.isPlaying && pauseWhenZeroVolume) {
                MusicPlayerRemote.pauseSong()
            }
    }

    fun setTintableColor(color: Int) {
        volumeDown.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        volumeUp.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        //TintHelper.setTint(volumeSeekBar, color, false)
        ViewUtil.setProgressDrawable(volumeSeekBar, color, true)
    }

    companion object {

        fun newInstance(): VolumeFragment {
            return VolumeFragment()
        }
    }
}
