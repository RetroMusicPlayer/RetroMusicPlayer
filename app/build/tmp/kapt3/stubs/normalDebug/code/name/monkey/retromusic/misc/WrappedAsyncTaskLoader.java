package code.name.monkey.retromusic.misc;

import java.lang.System;

/**
 * * [Issue
 * * 14944](http://code.google.com/p/android/issues/detail?id=14944)
 * *
 * * @author Alexander Blom
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0017\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00018\u0000H\u0016\u00a2\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\tH\u0014J\b\u0010\r\u001a\u00020\tH\u0014J\b\u0010\u000e\u001a\u00020\tH\u0014R\u0012\u0010\u0006\u001a\u0004\u0018\u00018\u0000X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0007\u00a8\u0006\u000f"}, d2 = {"Lcode/name/monkey/retromusic/misc/WrappedAsyncTaskLoader;", "D", "Landroidx/loader/content/AsyncTaskLoader;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mData", "Ljava/lang/Object;", "deliverResult", "", "data", "(Ljava/lang/Object;)V", "onReset", "onStartLoading", "onStopLoading", "app_normalDebug"})
public abstract class WrappedAsyncTaskLoader<D extends java.lang.Object> extends androidx.loader.content.AsyncTaskLoader<D> {
    private D mData;
    
    /**
     * * {@inheritDoc}
     */
    @java.lang.Override()
    public void deliverResult(@org.jetbrains.annotations.Nullable()
    D data) {
    }
    
    /**
     * * {@inheritDoc}
     */
    @java.lang.Override()
    protected void onStartLoading() {
    }
    
    /**
     * * {@inheritDoc}
     */
    @java.lang.Override()
    protected void onStopLoading() {
    }
    
    /**
     * * {@inheritDoc}
     */
    @java.lang.Override()
    protected void onReset() {
    }
    
    public WrappedAsyncTaskLoader(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super(null);
    }
}