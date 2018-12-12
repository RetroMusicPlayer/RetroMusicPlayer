package code.name.monkey.retromusic.glide.audiocover;

import java.lang.System;

/**
 * * @author Karim Abou Zeid (kabouzeid)
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0017B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\tH\u0016J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\u00022\u0006\u0010\f\u001a\u00020\rH\u0002J\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J \u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u00142\u000e\u0010\u0015\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00020\u0016H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcode/name/monkey/retromusic/glide/audiocover/AudioFileCoverFetcher;", "Lcom/bumptech/glide/load/data/DataFetcher;", "Ljava/io/InputStream;", "model", "Lcode/name/monkey/retromusic/glide/audiocover/AudioFileCover;", "(Lcode/name/monkey/retromusic/glide/audiocover/AudioFileCover;)V", "stream", "Ljava/io/FileInputStream;", "cancel", "", "cleanup", "fallback", "path", "", "getDataClass", "Ljava/lang/Class;", "getDataSource", "Lcom/bumptech/glide/load/DataSource;", "loadData", "priority", "Lcom/bumptech/glide/Priority;", "callback", "Lcom/bumptech/glide/load/data/DataFetcher$DataCallback;", "Companion", "app_normalDebug"})
public final class AudioFileCoverFetcher implements com.bumptech.glide.load.data.DataFetcher<java.io.InputStream> {
    private java.io.FileInputStream stream;
    private final code.name.monkey.retromusic.glide.audiocover.AudioFileCover model = null;
    private static final java.lang.String[] FALLBACKS = null;
    public static final code.name.monkey.retromusic.glide.audiocover.AudioFileCoverFetcher.Companion Companion = null;
    
    @java.lang.Override()
    public void loadData(@org.jetbrains.annotations.NotNull()
    com.bumptech.glide.Priority priority, @org.jetbrains.annotations.NotNull()
    com.bumptech.glide.load.data.DataFetcher.DataCallback<? super java.io.InputStream> callback) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.Class<java.io.InputStream> getDataClass() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.bumptech.glide.load.DataSource getDataSource() {
        return null;
    }
    
    private final java.io.InputStream fallback(java.lang.String path) throws java.io.FileNotFoundException {
        return null;
    }
    
    @java.lang.Override()
    public void cleanup() {
    }
    
    @java.lang.Override()
    public void cancel() {
    }
    
    public AudioFileCoverFetcher(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.glide.audiocover.AudioFileCover model) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/glide/audiocover/AudioFileCoverFetcher$Companion;", "", "()V", "FALLBACKS", "", "", "[Ljava/lang/String;", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}