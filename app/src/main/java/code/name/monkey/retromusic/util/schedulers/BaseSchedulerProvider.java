package code.name.monkey.retromusic.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Created by hemanths on 12/08/17.
 */

public interface BaseSchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
