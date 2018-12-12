package code.name.monkey.retromusic.mvp;

import java.lang.System;

/**
 * * Created by hemanths on 16/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcode/name/monkey/retromusic/mvp/Presenter;", "", "()V", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "getDisposable", "()Lio/reactivex/disposables/CompositeDisposable;", "setDisposable", "(Lio/reactivex/disposables/CompositeDisposable;)V", "repository", "Lcode/name/monkey/retromusic/providers/interfaces/Repository;", "getRepository", "()Lcode/name/monkey/retromusic/providers/interfaces/Repository;", "setRepository", "(Lcode/name/monkey/retromusic/providers/interfaces/Repository;)V", "schedulerProvider", "Lcode/name/monkey/retromusic/util/schedulers/BaseSchedulerProvider;", "getSchedulerProvider", "()Lcode/name/monkey/retromusic/util/schedulers/BaseSchedulerProvider;", "setSchedulerProvider", "(Lcode/name/monkey/retromusic/util/schedulers/BaseSchedulerProvider;)V", "app_normalDebug"})
public class Presenter {
    @org.jetbrains.annotations.NotNull()
    private code.name.monkey.retromusic.providers.interfaces.Repository repository;
    @org.jetbrains.annotations.NotNull()
    private io.reactivex.disposables.CompositeDisposable disposable;
    @org.jetbrains.annotations.NotNull()
    private code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider schedulerProvider;
    
    @org.jetbrains.annotations.NotNull()
    protected final code.name.monkey.retromusic.providers.interfaces.Repository getRepository() {
        return null;
    }
    
    protected final void setRepository(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.providers.interfaces.Repository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final io.reactivex.disposables.CompositeDisposable getDisposable() {
        return null;
    }
    
    protected final void setDisposable(@org.jetbrains.annotations.NotNull()
    io.reactivex.disposables.CompositeDisposable p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider getSchedulerProvider() {
        return null;
    }
    
    protected final void setSchedulerProvider(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider p0) {
    }
    
    public Presenter() {
        super();
    }
}