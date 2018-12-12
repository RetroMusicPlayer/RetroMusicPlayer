package code.name.monkey.retromusic.dialogs;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001\u001eB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\u0004H\u0002J&\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\u0012\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0015H\u0016J\u001a\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\u0010\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u0004H\u0002J\b\u0010\u001d\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00060\u0006R\u00020\u0000X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcode/name/monkey/retromusic/dialogs/SleepTimerDialog;", "Lcode/name/monkey/retromusic/views/RoundedBottomSheetDialogFragment;", "()V", "seekArcProgress", "", "timerUpdater", "Lcode/name/monkey/retromusic/dialogs/SleepTimerDialog$TimerUpdater;", "makeTimerIntent", "Landroid/content/Intent;", "makeTimerPendingIntent", "Landroid/app/PendingIntent;", "flag", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDismiss", "", "dialog", "Landroid/content/DialogInterface;", "onResume", "onViewCreated", "view", "setProgressBarColor", "dark", "updateTimeDisplayTime", "TimerUpdater", "app_normalDebug"})
public final class SleepTimerDialog extends code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment {
    private int seekArcProgress;
    private code.name.monkey.retromusic.dialogs.SleepTimerDialog.TimerUpdater timerUpdater;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    public void onDismiss(@org.jetbrains.annotations.Nullable()
    android.content.DialogInterface dialog) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    private final void setProgressBarColor(int dark) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void updateTimeDisplayTime() {
    }
    
    private final android.app.PendingIntent makeTimerPendingIntent(int flag) {
        return null;
    }
    
    private final android.content.Intent makeTimerIntent() {
        return null;
    }
    
    public SleepTimerDialog() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\u0007\b\u0000\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/dialogs/SleepTimerDialog$TimerUpdater;", "Landroid/os/CountDownTimer;", "(Lcode/name/monkey/retromusic/dialogs/SleepTimerDialog;)V", "onFinish", "", "onTick", "millisUntilFinished", "", "app_normalDebug"})
    final class TimerUpdater extends android.os.CountDownTimer {
        
        @java.lang.Override()
        public void onTick(long millisUntilFinished) {
        }
        
        @java.lang.Override()
        public void onFinish() {
        }
        
        public TimerUpdater() {
            super(0L, 0L);
        }
    }
}