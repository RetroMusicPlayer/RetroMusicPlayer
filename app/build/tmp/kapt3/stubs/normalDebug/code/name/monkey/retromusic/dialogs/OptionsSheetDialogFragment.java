package code.name.monkey.retromusic.dialogs;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\n\u001a\u00020\u000bH\u0002J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000eH\u0016J&\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u000bH\u0016J\u001a\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\b\u0010\u0018\u001a\u00020\u000bH\u0002J\b\u0010\u0019\u001a\u00020\u000bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u00078BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\t\u00a8\u0006\u001b"}, d2 = {"Lcode/name/monkey/retromusic/dialogs/OptionsSheetDialogFragment;", "Lcode/name/monkey/retromusic/views/RoundedBottomSheetDialogFragment;", "Landroid/view/View$OnClickListener;", "()V", "disposable", "Lio/reactivex/disposables/CompositeDisposable;", "timeOfTheDay", "", "getTimeOfTheDay", "()Ljava/lang/String;", "loadImageFromStorage", "", "onClick", "view", "Landroid/view/View;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "prepareBugReport", "shareApp", "Companion", "app_normalDebug"})
public final class OptionsSheetDialogFragment extends code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment implements android.view.View.OnClickListener {
    private final io.reactivex.disposables.CompositeDisposable disposable = null;
    private static final java.lang.String TAG = "MainOptionsBottomSheetD";
    public static final code.name.monkey.retromusic.dialogs.OptionsSheetDialogFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    private final java.lang.String getTimeOfTheDay() {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.NotNull()
    android.view.View view) {
    }
    
    private final void prepareBugReport() {
    }
    
    private final void shareApp() {
    }
    
    private final void loadImageFromStorage() {
    }
    
    public OptionsSheetDialogFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcode/name/monkey/retromusic/dialogs/OptionsSheetDialogFragment$Companion;", "", "()V", "TAG", "", "newInstance", "Lcode/name/monkey/retromusic/dialogs/OptionsSheetDialogFragment;", "selected_id", "", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.dialogs.OptionsSheetDialogFragment newInstance(int selected_id) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.dialogs.OptionsSheetDialogFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}