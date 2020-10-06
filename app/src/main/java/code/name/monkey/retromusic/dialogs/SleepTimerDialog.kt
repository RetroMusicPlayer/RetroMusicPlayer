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

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.addAccentColor
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.service.MusicService.ACTION_PENDING_QUIT
import code.name.monkey.retromusic.service.MusicService.ACTION_QUIT
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog

class SleepTimerDialog : DialogFragment() {

    private var seekArcProgress: Int = 0
    private lateinit var timerUpdater: TimerUpdater
    private lateinit var dialog: MaterialDialog
    private lateinit var shouldFinishLastSong: CheckBox
    private lateinit var seekBar: SeekBar
    private lateinit var timerDisplay: TextView

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        timerUpdater = TimerUpdater()
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_sleep_timer, null)
        shouldFinishLastSong = layout.findViewById(R.id.shouldFinishLastSong)
        seekBar = layout.findViewById(R.id.seekBar)
        timerDisplay = layout.findViewById(R.id.timerDisplay)

        val finishMusic = PreferenceUtil.isSleepTimerFinishMusic
        shouldFinishLastSong.apply {
            addAccentColor()
            isChecked = finishMusic
        }
        seekBar.apply {
            addAccentColor()
            seekArcProgress = PreferenceUtil.lastSleepTimerValue
            updateTimeDisplayTime()
            seekBar.progress = seekArcProgress
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        return materialDialog(R.string.action_sleep_timer)
            .setView(layout)
            .setPositiveButton(R.string.action_set) { _, _ ->
                PreferenceUtil.isSleepTimerFinishMusic = shouldFinishLastSong.isChecked
                val minutes = seekArcProgress
                val pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)
                val nextSleepTimerElapsedTime = SystemClock.elapsedRealtime() + minutes * 60 * 1000
                PreferenceUtil.nextSleepTimerElapsedRealTime = nextSleepTimerElapsedTime.toInt()
                val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextSleepTimerElapsedTime, pi)

                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.sleep_timer_set, minutes),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                val previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE)
                if (previous != null) {
                    val am =
                        requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    am.cancel(previous)
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
            .create()
            .colorButtons()
    }

    private fun updateTimeDisplayTime() {
        timerDisplay.text = "$seekArcProgress min"
    }

    private fun makeTimerPendingIntent(flag: Int): PendingIntent? {
        return PendingIntent.getService(requireActivity(), 0, makeTimerIntent(), flag)
    }

    private fun makeTimerIntent(): Intent {
        val intent = Intent(requireActivity(), MusicService::class.java)
        return if (shouldFinishLastSong.isChecked) {
            intent.setAction(ACTION_PENDING_QUIT)
        } else intent.setAction(ACTION_QUIT)
    }

    private fun updateCancelButton() {
        val musicService = MusicPlayerRemote.musicService
        if (musicService != null && musicService.pendingQuit) {
            dialog.getActionButton(DialogAction.NEUTRAL).text =
                dialog.context.getString(R.string.cancel_current_timer)
        } else {
            dialog.getActionButton(DialogAction.NEUTRAL).text = null
        }
    }

    private inner class TimerUpdater() :
        CountDownTimer(
            PreferenceUtil.nextSleepTimerElapsedRealTime - SystemClock.elapsedRealtime(),
            1000
        ) {

        override fun onTick(millisUntilFinished: Long) {
            println("onTick: $millisUntilFinished")
            seekBar.progress = millisUntilFinished.toInt()
        }

        override fun onFinish() {
            updateCancelButton()
        }
    }
}
