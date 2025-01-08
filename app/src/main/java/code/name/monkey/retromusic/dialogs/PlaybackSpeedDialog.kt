package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.DialogPlaybackSpeedBinding
import code.name.monkey.retromusic.extensions.accent
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.slider.Slider
import java.text.DecimalFormat

class PlaybackSpeedDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogPlaybackSpeedBinding.inflate(layoutInflater)
        binding.playbackSpeedSlider.accent()
        binding.playbackPitchSlider.accent()
        binding.playbackSpeedSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            // Format as a float with 2 decimal places
            // If the decimal part is "00", format it to 1 decimal place
            // Otherwise, use the 2 decimal places format
            val formattedSpeedValue = DecimalFormat("#.##").format(value)
            if (formattedSpeedValue.split(".").getOrElse(1) { "00" } == "00") {
                binding.speedValue.text = DecimalFormat("#.0#").format(value)
            } else {
                binding.speedValue.text = formattedSpeedValue
            }
        })
        binding.playbackPitchSlider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
            // Format as a float with 2 decimal places
            // If the decimal part is "00", format it to 1 decimal place
            // Otherwise, use the 2 decimal places format
            val formattedPitchValue = DecimalFormat("#.##").format(value)
            if (formattedPitchValue.split(".").getOrElse(1) { "00" } == "00") {
                binding.pitchValue.text = DecimalFormat("#.0#").format(value)
            } else {
                binding.pitchValue.text = formattedPitchValue
            }
        })
        binding.playbackSpeedSlider.value = PreferenceUtil.playbackSpeed
        binding.playbackPitchSlider.value = PreferenceUtil.playbackPitch

        return materialDialog(R.string.playback_settings)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.save) { _, _ ->
                updatePlaybackAndPitch(
                    binding.playbackSpeedSlider.value,
                    binding.playbackPitchSlider.value
                )
            }
            .setNeutralButton(R.string.reset_action) {_, _ ->
                updatePlaybackAndPitch(
                    1F,
                    1F
                )
            }
            .setView(binding.root)
            .create()
            .colorButtons()
    }

    private fun updatePlaybackAndPitch(speed: Float, pitch: Float) {
        PreferenceUtil.playbackSpeed = speed
        PreferenceUtil.playbackPitch = pitch
    }

    companion object {
        fun newInstance(): PlaybackSpeedDialog {
            return PlaybackSpeedDialog()
        }
    }
}