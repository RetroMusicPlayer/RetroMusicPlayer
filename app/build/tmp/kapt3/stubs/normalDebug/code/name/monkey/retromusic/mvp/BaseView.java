package code.name.monkey.retromusic.mvp;

import java.lang.System;

/**
 * * Created by hemanths on 09/08/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0004H&J\u0015\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00028\u0000H&\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0004H&\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/mvp/BaseView;", "T", "", "completed", "", "loading", "showData", "list", "(Ljava/lang/Object;)V", "showEmptyView", "app_normalDebug"})
public abstract interface BaseView<T extends java.lang.Object> {
    
    public abstract void loading();
    
    public abstract void showData(T list);
    
    public abstract void showEmptyView();
    
    public abstract void completed();
}