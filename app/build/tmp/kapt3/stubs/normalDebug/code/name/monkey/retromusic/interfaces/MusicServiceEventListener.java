package code.name.monkey.retromusic.interfaces;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0003H&J\b\u0010\u0005\u001a\u00020\u0003H&J\b\u0010\u0006\u001a\u00020\u0003H&J\b\u0010\u0007\u001a\u00020\u0003H&J\b\u0010\b\u001a\u00020\u0003H&J\b\u0010\t\u001a\u00020\u0003H&J\b\u0010\n\u001a\u00020\u0003H&\u00a8\u0006\u000b"}, d2 = {"Lcode/name/monkey/retromusic/interfaces/MusicServiceEventListener;", "", "onMediaStoreChanged", "", "onPlayStateChanged", "onPlayingMetaChanged", "onQueueChanged", "onRepeatModeChanged", "onServiceConnected", "onServiceDisconnected", "onShuffleModeChanged", "app_normalDebug"})
public abstract interface MusicServiceEventListener {
    
    public abstract void onServiceConnected();
    
    public abstract void onServiceDisconnected();
    
    public abstract void onQueueChanged();
    
    public abstract void onPlayingMetaChanged();
    
    public abstract void onPlayStateChanged();
    
    public abstract void onRepeatModeChanged();
    
    public abstract void onShuffleModeChanged();
    
    public abstract void onMediaStoreChanged();
}