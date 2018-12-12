package code.name.monkey.retromusic.mvp;

import java.lang.System;

/**
 * * Created by hemanths on 09/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcode/name/monkey/retromusic/mvp/BasePresenter;", "T", "", "subscribe", "", "unsubscribe", "app_normalDebug"})
public abstract interface BasePresenter<T extends java.lang.Object> {
    
    public abstract void subscribe();
    
    public abstract void unsubscribe();
}