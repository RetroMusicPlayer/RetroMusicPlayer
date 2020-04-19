package code.name.monkey.retromusic.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.setRange
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.volume.AudioVolumeObserver
import code.name.monkey.retromusic.volume.OnAudioVolumeChangedListener
import com.google.android.material.slider.Slider

import kotlinx.android.synthetic.main.fragment_volume.*

class VolumeFragment : Fragment(), OnAudioVolumeChangedListener,
    View.OnClickListener, Slider.OnChangeListener {

    private var audioVolumeObserver: AudioVolumeObserver? = null

    private val audioManager: AudioManager?
        get() = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTintable(ThemeStore.accentColor(requireContext()))
        volumeDown.setOnClickListener(this)
        volumeUp.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (audioVolumeObserver == null) {
            audioVolumeObserver = AudioVolumeObserver(requireActivity())
        }
        audioVolumeObserver!!.register(AudioManager.STREAM_MUSIC, this)

        val audioManager = audioManager
        if (audioManager != null) {
            volumeSeekBar.valueTo =
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            volumeSeekBar.value = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        }
        volumeSeekBar.addOnChangeListener(this)
    }

    override fun onAudioVolumeChanged(currentVolume: Float, maxVolume: Float) {
        if (volumeSeekBar == null) {
            return
        }
        if (maxVolume <= 0.0f) {
            setPauseWhenZeroVolume(currentVolume < 1)
            return
        }
        volumeSeekBar.setRange(currentVolume, maxVolume)
        volumeDown.setImageResource(if (currentVolume == 0.0f) R.drawable.ic_volume_off_white_24dp else R.drawable.ic_volume_down_white_24dp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (audioVolumeObserver != null) {
            audioVolumeObserver!!.unregister()
        }
    }

    override fun onClick(view: View) {
        val audioManager = audioManager
        when (view.id) {
            R.id.volumeDown -> audioManager?.adjustStreamVolume(
                AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0
            )
            R.id.volumeUp -> audioManager?.adjustStreamVolume(
                AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0
            )
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

    private fun setPauseWhenZeroVolume(pauseWhenZeroVolume: Boolean) {
        if (PreferenceUtil.getInstance(requireContext()).pauseOnZeroVolume()) if (MusicPlayerRemote.isPlaying && pauseWhenZeroVolume) {
            MusicPlayerRemote.pauseSong()
        }
    }

    fun setTintableColor(color: Int) {
        volumeDown.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        volumeUp.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        ViewUtil.setProgressDrawable(volumeSeekBar, color, true)
    }

    companion object {

        fun newInstance(): VolumeFragment {
            return VolumeFragment()
        }
    }

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        if (value <= 0) {
            return
        }
        val audioManager = audioManager
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, value.toInt(), 0)
        setPauseWhenZeroVolume(value < 1.0f)
        volumeDown.setImageResource(if (value == 0.0f) R.drawable.ic_volume_off_white_24dp else R.drawable.ic_volume_down_white_24dp)
    }
}
