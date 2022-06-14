/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.dialogs

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.DialogSleepTimerBinding
import code.name.monkey.retromusic.extensions.addAccentColor
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.service.MusicService.Companion.ACTION_PENDING_QUIT
import code.name.monkey.retromusic.service.MusicService.Companion.ACTION_QUIT
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil

class SleepTimerDialog : DialogFragment() {

    private var seekArcProgress: Int = 0
    private lateinit var timerUpdater: TimerUpdater
    private lateinit var dialog: AlertDialog

    private var _binding: DialogSleepTimerBinding? = null
    private val binding get() = _binding!!

    private val shouldFinishLastSong: CheckBox get() = binding.shouldFinishLastSong
    private val seekBar: SeekBar get() = binding.seekBar
    private val timerDisplay: TextView get() = binding.timerDisplay

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        timerUpdater = TimerUpdater()
        _binding = DialogSleepTimerBinding.inflate(layoutInflater)

        val finishMusic = PreferenceUtil.isSleepTimerFinishMusic
        shouldFinishLastSong.apply {
            addAccentColor()
            isChecked = finishMusic
        }
        seekBar.apply {
            addAccentColor()
            seekArcProgress = PreferenceUtil.lastSleepTimerValue
            updateTimeDisplayTime()
            progress = seekArcProgress
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (i < 1) {
                    seekBar.progress = 1
                    return
                }
                seekArcProgress = i
                updateTimeDisplayTime()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                PreferenceUtil.lastSleepTimerValue = seekArcProgress
            }
        })

        materialDialog(R.string.action_sleep_timer).apply {
            if (PreferenceUtil.nextSleepTimerElapsedRealTime > System.currentTimeMillis()) {
                seekBar.isVisible = false
                shouldFinishLastSong.isVisible = false
                timerUpdater.start()
                setPositiveButton(android.R.string.ok, null)
                setNegativeButton(R.string.action_cancel) { _, _ ->
                    timerUpdater.cancel()
                    val previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE)
                    if (previous != null) {
                        val am = requireContext().getSystemService<AlarmManager>()
                        am?.cancel(previous)
                        previous.cancel()
                        Toast.makeText(
                            requireContext(),
                            requireContext().resources.getString(R.string.sleep_timer_canceled),
                            Toast.LENGTH_SHORT
                        ).show()
                        val musicService = MusicPlayerRemote.musicService
                        if (musicService != null && musicService.pendingQuit) {
                            musicService.pendingQuit = false
                            Toast.makeText(
                                requireContext(),
                                requireContext().resources.getString(R.string.sleep_timer_canceled),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                seekBar.isVisible = true
                shouldFinishLastSong.isVisible = true
                setPositiveButton(R.string.action_set) { _, _ ->
                    PreferenceUtil.isSleepTimerFinishMusic = shouldFinishLastSong.isChecked
                    val minutes = seekArcProgress
                    val pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)
                    val nextSleepTimerElapsedTime =
                        SystemClock.elapsedRealtime() + minutes * 60 * 1000
                    PreferenceUtil.nextSleepTimerElapsedRealTime = nextSleepTimerElapsedTime.toInt()
                    val am = requireContext().getSystemService<AlarmManager>()
                    am?.setExact(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        nextSleepTimerElapsedTime,
                        pi
                    )

                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.sleep_timer_set, minutes),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            setView(binding.root)
            dialog = create()

        }
        return dialog
    }

    private fun updateTimeDisplayTime() {
        timerDisplay.text = "$seekArcProgress min"
    }

    private fun makeTimerPendingIntent(flag: Int): PendingIntent? {
        return PendingIntent.getService(
            requireActivity(), 0, makeTimerIntent(), flag or if (VersionUtils.hasMarshmallow())
                PendingIntent.FLAG_IMMUTABLE
            else 0
        )
    }

    private fun makeTimerIntent(): Intent {
        val intent = Intent(requireActivity(), MusicService::class.java)
        return if (shouldFinishLastSong.isChecked) {
            intent.setAction(ACTION_PENDING_QUIT)
        } else intent.setAction(ACTION_QUIT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        timerUpdater.cancel()
        _binding = null
    }

    private inner class TimerUpdater :
        CountDownTimer(
            PreferenceUtil.nextSleepTimerElapsedRealTime - SystemClock.elapsedRealtime(),
            1000
        ) {

        override fun onTick(millisUntilFinished: Long) {
            timerDisplay.text = MusicUtil.getReadableDurationString(millisUntilFinished)
        }

        override fun onFinish() {}
    }
}
