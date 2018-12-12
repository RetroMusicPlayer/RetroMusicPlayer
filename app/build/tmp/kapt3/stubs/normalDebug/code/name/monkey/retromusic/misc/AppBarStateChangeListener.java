package code.name.monkey.retromusic.misc;

import java.lang.System;

/**
 * * @author Hemanth S (h4h13).
 * * https://stackoverflow.com/a/33891727
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0004H&R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcode/name/monkey/retromusic/misc/AppBarStateChangeListener;", "Lcom/google/android/material/appbar/AppBarLayout$OnOffsetChangedListener;", "()V", "mCurrentState", "Lcode/name/monkey/retromusic/misc/AppBarStateChangeListener$State;", "onOffsetChanged", "", "appBarLayout", "Lcom/google/android/material/appbar/AppBarLayout;", "i", "", "onStateChanged", "state", "State", "app_normalDebug"})
public abstract class AppBarStateChangeListener implements com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener {
    private code.name.monkey.retromusic.misc.AppBarStateChangeListener.State mCurrentState;
    
    @java.lang.Override()
    public void onOffsetChanged(@org.jetbrains.annotations.NotNull()
    com.google.android.material.appbar.AppBarLayout appBarLayout, int i) {
    }
    
    public abstract void onStateChanged(@org.jetbrains.annotations.NotNull()
    com.google.android.material.appbar.AppBarLayout appBarLayout, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.misc.AppBarStateChangeListener.State state);
    
    public AppBarStateChangeListener() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcode/name/monkey/retromusic/misc/AppBarStateChangeListener$State;", "", "(Ljava/lang/String;I)V", "EXPANDED", "COLLAPSED", "IDLE", "app_normalDebug"})
    public static enum State {
        /*public static final*/ EXPANDED /* = new EXPANDED() */,
        /*public static final*/ COLLAPSED /* = new COLLAPSED() */,
        /*public static final*/ IDLE /* = new IDLE() */;
        
        State() {
        }
    }
}