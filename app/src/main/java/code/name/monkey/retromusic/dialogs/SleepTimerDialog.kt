package code.name.monkey.retromusic.dialogs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.Constants.ACTION_QUIT
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.ViewUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_sleep_timer.*
import java.util.*

class SleepTimerDialog : RoundedBottomSheetDialogFragment() {

    private var seekArcProgress: Int = 0
    private lateinit var timerUpdater: TimerUpdater

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        timerUpdater.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE) != null) {
            timerUpdater.start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sleep_timer, container, false)
    }

    private fun setProgressBarColor(dark: Int) {
        ViewUtil.setProgressDrawable(progressSlider = seekBar, newColor = dark)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }

    private fun updateTimeDisplayTime() {
        timerDisplay!!.text = String.format(Locale.getDefault(), "%d min", seekArcProgress)
    }

    private fun makeTimerPendingIntent(flag: Int): PendingIntent? {
        return PendingIntent.getService(activity, 0, makeTimerIntent(), flag)
    }

    private fun makeTimerIntent(): Intent {
        return Intent(activity, MusicService::class.java)
                .setAction(ACTION_QUIT)
    }

    private inner class TimerUpdater internal constructor() : CountDownTimer(PreferenceUtil.getInstance().nextSleepTimerElapsedRealTime - SystemClock.elapsedRealtime(), 1000) {

        override fun onTick(millisUntilFinished: Long) {
            actionCancel.text = String.format("%s (%s)", getString(R.string.cancel_current_timer), MusicUtil.getReadableDurationString(millisUntilFinished))
        }

        override fun onFinish() {
            actionCancel.text = null
            actionCancel.visibility = View.GONE
        }
    }
}