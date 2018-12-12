package code.name.monkey.retromusic.ui.fragments.base;

import java.lang.System;

/**
 * * Created by hemanths on 24/09/17.
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H$J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\u001a\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u0005H$J\b\u0010\u0010\u001a\u00020\u0005H$J\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tJ\b\u0010\u0012\u001a\u00020\u0005H$J\b\u0010\u0013\u001a\u00020\u0005H$\u00a8\u0006\u0014"}, d2 = {"Lcode/name/monkey/retromusic/ui/fragments/base/AbsPlayerControlsFragment;", "Lcode/name/monkey/retromusic/ui/fragments/base/AbsMusicServiceFragment;", "Lcode/name/monkey/retromusic/helper/MusicProgressViewUpdateHelper$Callback;", "()V", "hide", "", "hideVolumeIfAvailable", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "setDark", "color", "", "setUpProgressSlider", "show", "showBonceAnimation", "updateRepeatState", "updateShuffleState", "app_normalDebug"})
public abstract class AbsPlayerControlsFragment extends code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment implements code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback {
    private java.util.HashMap _$_findViewCache;
    
    protected abstract void show();
    
    protected abstract void hide();
    
    protected abstract void updateShuffleState();
    
    protected abstract void updateRepeatState();
    
    protected abstract void setUpProgressSlider();
    
    public abstract void setDark(int color);
    
    public final void showBonceAnimation(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void hideVolumeIfAvailable() {
    }
    
    public AbsPlayerControlsFragment() {
        super();
    }
}