/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.preferences

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.fragment.app.DialogFragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.colorControlNormal
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.slider.Slider


class DurationPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {
    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }
}

class DurationPreferenceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater
            .inflate(R.layout.preference_dialog_audio_fade, null)


        val slider = view.findViewById<Slider>(R.id.slider)
        val duration = view.findViewById<TextView>(R.id.duration)
        ColorStateList.valueOf(ThemeStore.accentColor(requireContext())).let {
            slider.trackTintList = it
            slider.thumbTintList = it
        }
        slider.value = PreferenceUtil.audioFadeDuration.toFloat()
        updateText(slider.value.toInt(), duration)
        slider.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
            if (fromUser) {
                updateText(value.toInt(), duration)
            }
        })


        return materialDialog(R.string.audio_fade_duration)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.save) { _, _ -> updateDuration(slider.value.toInt()) }
            .setView(view)
            .create()
            .colorButtons()
    }

    private fun updateText(value: Int, duration: TextView) {
        var durationText = "$value ms"
        if (value == 0) durationText += " / Off"
        duration.text = durationText
    }

    private fun updateDuration(duration: Int) {
        PreferenceUtil.audioFadeDuration = duration
    }

    companion object {
        fun newInstance(): DurationPreferenceDialog {
            return DurationPreferenceDialog()
        }
    }
}