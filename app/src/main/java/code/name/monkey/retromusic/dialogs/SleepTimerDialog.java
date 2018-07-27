package code.name.monkey.retromusic.dialogs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment;

import static code.name.monkey.retromusic.Constants.ACTION_QUIT;

public class SleepTimerDialog extends RoundedBottomSheetDialogFragment {
    @BindView(R.id.seek_arc)
    SeekBar seekArc;
    @BindView(R.id.timer_display)
    TextView timerDisplay;
    @BindView(R.id.action_set)
    Button setButton;
    @BindView(R.id.action_cancel)
    Button cancelButton;

    private int seekArcProgress;
    private TimerUpdater timerUpdater;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        timerUpdater.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE) != null) {
            timerUpdater.start();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_sleep_timer, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timerUpdater = new TimerUpdater();

        seekArcProgress = PreferenceUtil.getInstance(getActivity()).getLastSleepTimerValue();
        updateTimeDisplayTime();
        seekArc.setProgress(seekArcProgress);

        int accentColor = ThemeStore.accentColor(getContext());
        TintHelper.setTintAuto(seekArc, accentColor, true);
        setButton.setTextColor(accentColor);
        cancelButton.setTextColor(accentColor);

        seekArc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < 1) {
                    seekArc.setProgress(1);
                    return;
                }
                seekArcProgress = i;
                updateTimeDisplayTime();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PreferenceUtil.getInstance(getActivity()).setLastSleepTimerValue(seekArcProgress);
            }
        });
    }

    @OnClick({R.id.action_cancel, R.id.action_set})
    void set(View view) {
        switch (view.getId()) {
            case R.id.action_cancel:
                if (getActivity() == null) {
                    return;
                }
                final PendingIntent previous = makeTimerPendingIntent(PendingIntent.FLAG_NO_CREATE);
                if (previous != null) {
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    if (am != null) {
                        am.cancel(previous);
                    }
                    previous.cancel();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.sleep_timer_canceled), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_set:
                if (getActivity() == null) {
                    return;
                }
                final int minutes = seekArcProgress;
                PendingIntent pi = makeTimerPendingIntent(PendingIntent.FLAG_CANCEL_CURRENT);
                final long nextSleepTimerElapsedTime = SystemClock.elapsedRealtime() + minutes * 60 * 1000;
                PreferenceUtil.getInstance(getActivity()).setNextSleepTimerElapsedRealtime(nextSleepTimerElapsedTime);
                AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                if (am != null) {
                    am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextSleepTimerElapsedTime, pi);
                }
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.sleep_timer_set, minutes), Toast.LENGTH_SHORT).show();
                break;
        }
        dismiss();
    }

    private void updateTimeDisplayTime() {
        timerDisplay.setText(String.format(Locale.getDefault(), "%d min", seekArcProgress));
    }

    private PendingIntent makeTimerPendingIntent(int flag) {
        return PendingIntent.getService(getActivity(), 0, makeTimerIntent(), flag);
    }

    private Intent makeTimerIntent() {
        return new Intent(getActivity(), MusicService.class)
                .setAction(ACTION_QUIT);
    }

    private class TimerUpdater extends CountDownTimer {
        TimerUpdater() {
            super(PreferenceUtil.getInstance(getActivity()).getNextSleepTimerElapsedRealTime() - SystemClock.elapsedRealtime(), 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            cancelButton.setText(String.format("%s (%s)", getString(R.string.cancel_current_timer), MusicUtil.getReadableDurationString(millisUntilFinished)));
            //materialDialog.setActionButton(DialogAction.NEUTRAL, materialDialog.getContext().getString(R.string.cancel_current_timer) + " (" + MusicUtil.getReadableDurationString(millisUntilFinished) + ")");
        }

        @Override
        public void onFinish() {
            cancelButton.setText(null);
            //materialDialog.setActionButton(DialogAction.NEUTRAL, null);
        }
    }
}