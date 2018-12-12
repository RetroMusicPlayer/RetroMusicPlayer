package code.name.monkey.retromusic.glide.artistimage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u0000 \u00152\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u0015\u0016B\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ0\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\r\u001a\u00020\u0002H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcode/name/monkey/retromusic/glide/artistimage/ArtistImageLoader;", "Lcom/bumptech/glide/load/model/ModelLoader;", "Lcode/name/monkey/retromusic/glide/artistimage/ArtistImage;", "Ljava/io/InputStream;", "context", "Landroid/content/Context;", "lastFMClient", "Lcode/name/monkey/retromusic/rest/LastFMRestClient;", "okhttp", "Lokhttp3/OkHttpClient;", "(Landroid/content/Context;Lcode/name/monkey/retromusic/rest/LastFMRestClient;Lokhttp3/OkHttpClient;)V", "buildLoadData", "Lcom/bumptech/glide/load/model/ModelLoader$LoadData;", "model", "width", "", "height", "options", "Lcom/bumptech/glide/load/Options;", "handles", "", "Companion", "Factory", "app_normalDebug"})
public final class ArtistImageLoader implements com.bumptech.glide.load.model.ModelLoader<code.name.monkey.retromusic.glide.artistimage.ArtistImage, java.io.InputStream> {
    private final android.content.Context context = null;
    private final code.name.monkey.retromusic.rest.LastFMRestClient lastFMClient = null;
    private final okhttp3.OkHttpClient okhttp = null;
    private static final int TIMEOUT = 500;
    public static final code.name.monkey.retromusic.glide.artistimage.ArtistImageLoader.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public com.bumptech.glide.load.model.ModelLoader.LoadData<java.io.InputStream> buildLoadData(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.glide.artistimage.ArtistImage model, int width, int height, @org.jetbrains.annotations.NotNull()
    com.bumptech.glide.load.Options options) {
        return null;
    }
    
    @java.lang.Override()
    public boolean handles(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.glide.artistimage.ArtistImage model) {
        return false;
    }
    
    public ArtistImageLoader(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.rest.LastFMRestClient lastFMClient, @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okhttp) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcode/name/monkey/retromusic/glide/artistimage/ArtistImageLoader$Factory;", "Lcom/bumptech/glide/load/model/ModelLoaderFactory;", "Lcode/name/monkey/retromusic/glide/artistimage/ArtistImage;", "Ljava/io/InputStream;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "lastFMClient", "Lcode/name/monkey/retromusic/rest/LastFMRestClient;", "okHttp", "Lokhttp3/OkHttpClient;", "build", "Lcom/bumptech/glide/load/model/ModelLoader;", "multiFactory", "Lcom/bumptech/glide/load/model/MultiModelLoaderFactory;", "teardown", "", "app_normalDebug"})
    public static final class Factory implements com.bumptech.glide.load.model.ModelLoaderFactory<code.name.monkey.retromusic.glide.artistimage.ArtistImage, java.io.InputStream> {
        private final code.name.monkey.retromusic.rest.LastFMRestClient lastFMClient = null;
        private final okhttp3.OkHttpClient okHttp = null;
        private final android.content.Context context = null;
        
        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public com.bumptech.glide.load.model.ModelLoader<code.name.monkey.retromusic.glide.artistimage.ArtistImage, java.io.InputStream> build(@org.jetbrains.annotations.NotNull()
        com.bumptech.glide.load.model.MultiModelLoaderFactory multiFactory) {
            return null;
        }
        
        @java.lang.Override()
        public void teardown() {
        }
        
        public Factory(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcode/name/monkey/retromusic/glide/artistimage/ArtistImageLoader$Companion;", "", "()V", "TIMEOUT", "", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}