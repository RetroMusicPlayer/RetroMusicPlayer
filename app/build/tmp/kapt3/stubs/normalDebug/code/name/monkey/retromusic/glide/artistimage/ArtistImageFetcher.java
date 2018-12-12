package code.name.monkey.retromusic.glide.artistimage;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \"2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\"B5\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\f\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0017H\u0016J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J \u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u001f2\u000e\u0010 \u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00020!H\u0016R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcode/name/monkey/retromusic/glide/artistimage/ArtistImageFetcher;", "Lcom/bumptech/glide/load/data/DataFetcher;", "Ljava/io/InputStream;", "context", "Landroid/content/Context;", "lastFMRestClient", "Lcode/name/monkey/retromusic/rest/LastFMRestClient;", "okHttp", "Lokhttp3/OkHttpClient;", "model", "Lcode/name/monkey/retromusic/glide/artistimage/ArtistImage;", "width", "", "height", "(Landroid/content/Context;Lcode/name/monkey/retromusic/rest/LastFMRestClient;Lokhttp3/OkHttpClient;Lcode/name/monkey/retromusic/glide/artistimage/ArtistImage;II)V", "call", "Lretrofit2/Call;", "Lcode/name/monkey/retromusic/rest/model/LastFmArtist;", "isCancelled", "", "streamFetcher", "Lcom/bumptech/glide/integration/okhttp3/OkHttpStreamFetcher;", "cancel", "", "cleanup", "getDataClass", "Ljava/lang/Class;", "getDataSource", "Lcom/bumptech/glide/load/DataSource;", "loadData", "priority", "Lcom/bumptech/glide/Priority;", "callback", "Lcom/bumptech/glide/load/data/DataFetcher$DataCallback;", "Companion", "app_normalDebug"})
public final class ArtistImageFetcher implements com.bumptech.glide.load.data.DataFetcher<java.io.InputStream> {
    private volatile boolean isCancelled;
    private retrofit2.Call<code.name.monkey.retromusic.rest.model.LastFmArtist> call;
    private com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher streamFetcher;
    private final android.content.Context context = null;
    private final code.name.monkey.retromusic.rest.LastFMRestClient lastFMRestClient = null;
    private final okhttp3.OkHttpClient okHttp = null;
    private final code.name.monkey.retromusic.glide.artistimage.ArtistImage model = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = null;
    public static final code.name.monkey.retromusic.glide.artistimage.ArtistImageFetcher.Companion Companion = null;
    
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
    
    @java.lang.Override()
    public void loadData(@org.jetbrains.annotations.NotNull()
    com.bumptech.glide.Priority priority, @org.jetbrains.annotations.NotNull()
    com.bumptech.glide.load.data.DataFetcher.DataCallback<? super java.io.InputStream> callback) {
    }
    
    @java.lang.Override()
    public void cleanup() {
    }
    
    @java.lang.Override()
    public void cancel() {
    }
    
    public ArtistImageFetcher(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.rest.LastFMRestClient lastFMRestClient, @org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttp, @org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.glide.artistimage.ArtistImage model, int width, int height) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcode/name/monkey/retromusic/glide/artistimage/ArtistImageFetcher$Companion;", "", "()V", "TAG", "", "getTAG", "()Ljava/lang/String;", "app_normalDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getTAG() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}