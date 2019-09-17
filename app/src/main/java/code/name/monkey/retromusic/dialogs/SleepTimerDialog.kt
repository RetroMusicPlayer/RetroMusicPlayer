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

package code.name.monkey.retromusic.dialogs

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.service.MusicService.ACTION_PENDING_QUIT
import code.name.monkey.retromusic.service.MusicService.ACTION_QUIT
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView


class SleepTimerDialog : DialogFragment() {

    private var seekArcProgress: Int = 0
    private lateinit var timerUpdater: TimerUpdater
    private lateinit var materialDialog: MaterialDialog
    private lateinit var shouldFinishLastSong: CheckBox
    private lateinit var seekBar: SeekBar
    private lateinit var timerDisplay: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        timerUpdater = TimerUpdater()

        materialDialog = MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))
                .title(R.string.action_sleep_timer)
                .cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
                .positiveButton(R.string.action_set) {
                    PreferenceUtil.getInstance(requireContext()).sleepTimerFinishMusic = shouldFinishLastSong.isChecked

                    val minutes = seekArcProgress

                    val pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)

                    val nextSleepTimerElapsedTime = SystemClock.elapsedRealtime() + minutes * 60 * 1000
                    PreferenceUtil.getInstance(requireContext()).setNextSleepTimerElapsedRealtime(nextSleepTimerElapsedTime)
                    val am = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextSleepTimerElapsedTime, pi)

                    Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_set, minutes), Toast.LENGTH_SHORT).show()
                }
                .negativeButton(android.R.string.cancel) {
                    if (activity == null) {
                        return@negativeButton
                    }
                    val previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE)
                    if (previous != null) {
                        val am = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        am.cancel(previous)
                        previous.cancel()
                        Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show()
                    }

                    val musicService = MusicPlayerRemote.musicService
                    if (musicService != null && musicService.pendingQuit) {
                        musicService.pendingQuit = false
                        Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show()
                    }
                }
                .customView(R.layout.dialog_sleep_timer, scrollable = false)
                .show {
                    onShow {
                        if (makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE) != null) {
                            timerUpdater.start()
                        }
                    }
                }

        if (activity == null || materialDialog.getCustomView() == null) {
            return materialDialog
        }

        shouldFinishLastSong = materialDialog.getCustomView().findViewById(R.id.shouldFinishLastSong)
        seekBar = materialDialog.getCustomView().findViewById(R.id.seekBar)
        timerDisplay = materialDialog.getCustomView().findViewById(R.id.timerDisplay)


        val finishMusic = PreferenceUtil.getInstance(requireContext()).sleepTimerFinishMusic
        shouldFinishLastSong.isChecked = finishMusic


        seekArcProgress = PreferenceUtil.getInstance(requireContext()).lastSleepTimerValue
        updateTimeDisplayTime()
        seekBar.progress = seekArcProgress

        setProgressBarColor(ThemeStore.accentColor(context!!))

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
                PreferenceUtil.getInstance(requireContext()).lastSleepTimerValue = seekArcProgress
            }
        })

        return materialDialog
    }

    private fun updateTimeDisplayTime() {
        timerDisplay.text = "$seekArcProgress min"
    }


    private fun makeTimerPendingIntent(flag: Int): PendingIntent? {
        return PendingIntent.getService(activity, 0, makeTimerIntent(), flag)
    }

    private fun makeTimerIntent(): Intent {
        val intent = Intent(activity, MusicService::class.java)
        return if (shouldFinishLastSong.isChecked) {
            intent.setAction(ACTION_PENDING_QUIT)
        } else intent.setAction(ACTION_QUIT)
    }


    private fun updateCancelButton() {
        val musicService = MusicPlayerRemote.musicService
        if (musicService != null && musicService.pendingQuit) {
            materialDialog.getActionButton(WhichButton.NEGATIVE).text = materialDialog.context.getString(R.string.cancel_current_timer)
        } else {
            materialDialog.getActionButton(WhichButton.NEGATIVE).text = null
        }
    }

    private inner class TimerUpdater internal constructor() : CountDownTimer(PreferenceUtil.getInstance(requireContext()).nextSleepTimerElapsedRealTime - SystemClock.elapsedRealtime(), 1000) {

        override fun onTick(millisUntilFinished: Long) {
            materialDialog.getActionButton(WhichButton.NEGATIVE).text =
                    String.format("%s %s", materialDialog.context.getString(R.string.cancel_current_timer),
                            " (" + MusicUtil.getReadableDurationString(millisUntilFinished) + ")")
        }

        override fun onFinish() {
            updateCancelButton()
        }
    }

    /* override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.dialog_sleep_timer, container, false)
     }*/

    private fun setProgressBarColor(dark: Int) {
        ViewUtil.setProgressDrawable(progressSlider = seekBar, newColor = dark)
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MaterialUtil.setTint(actionCancel, false)

        title.setTextColor(ThemeStore.textColorPrimary(context!!))
        timerDisplay!!.setTextColor(ThemeStore.textColorSecondary(context!!))

        timerUpdater = TimerUpdater()

        seekArcProgress = PreferenceUtil.getInstance().lastSleepTimerValue
        updateTimeDisplayTime()
        seekBar.progress = seekArcProgress

        setProgressBarColor(ThemeStore.accentColor(context!!))

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
                PreferenceUtil.getInstance().lastSleepTimerValue = seekArcProgress
            }
        })

        actionCancel.apply {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_close_white_24dp)
            setOnClickListener {
                val previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE)
                if (previous != null) {
                    val am = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    am.cancel(previous)
                    previous.cancel()
                    Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show()
                }
                dismiss()
            }
        }
        actionSet.apply {
            icon = ContextCompat.getDrawable(context, R.drawable.ic_time_lapse_white_24dp)
            MaterialUtil.setTint(actionSet)
            setOnClickListener {
                val minutes = seekArcProgress
                val pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT)
                val nextSleepTimerElapsedTime = SystemClock.elapsedRealtime() + minutes * 60 * 1000
                PreferenceUtil.getInstance().setNextSleepTimerElapsedRealtime(nextSleepTimerElapsedTime)
                val am = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextSleepTimerElapsedTime, pi)
                Toast.makeText(activity, activity!!.resources.getString(R.string.sleep_timer_set, minutes), Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }*/


}