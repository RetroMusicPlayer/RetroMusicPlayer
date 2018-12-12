package code.name.monkey.retromusic.misc;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b&\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032\u0014\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcode/name/monkey/retromusic/misc/WeakContextAsyncTask;", "Params", "Progress", "Result", "Landroid/os/AsyncTask;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "getContext", "()Landroid/content/Context;", "contextWeakReference", "Ljava/lang/ref/WeakReference;", "app_normalDebug"})
public abstract class WeakContextAsyncTask<Params extends java.lang.Object, Progress extends java.lang.Object, Result extends java.lang.Object> extends android.os.AsyncTask<Params, Progress, Result> {
    private final java.lang.ref.WeakReference<android.content.Context> contextWeakReference = null;
    
    @org.jetbrains.annotations.Nullable()
    protected final android.content.Context getContext() {
        return null;
    }
    
    public WeakContextAsyncTask(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
}