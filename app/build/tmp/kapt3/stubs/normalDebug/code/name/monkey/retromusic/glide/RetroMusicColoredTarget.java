package code.name.monkey.retromusic.glide;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H&J\u0012\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\"\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0010\u0010\u0014\u001a\f\u0012\u0006\b\u0000\u0012\u00020\u0013\u0018\u00010\u0015H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\b\u00a8\u0006\u0016"}, d2 = {"Lcode/name/monkey/retromusic/glide/RetroMusicColoredTarget;", "Lcode/name/monkey/retromusic/glide/palette/BitmapPaletteTarget;", "view", "Landroid/widget/ImageView;", "(Landroid/widget/ImageView;)V", "albumArtistFooterColor", "", "getAlbumArtistFooterColor", "()I", "defaultFooterColor", "getDefaultFooterColor", "onColorReady", "", "color", "onLoadFailed", "errorDrawable", "Landroid/graphics/drawable/Drawable;", "onResourceReady", "resource", "Lcode/name/monkey/retromusic/glide/palette/BitmapPaletteWrapper;", "glideAnimation", "Lcom/bumptech/glide/request/transition/Transition;", "app_normalDebug"})
public abstract class RetroMusicColoredTarget extends code.name.monkey.retromusic.glide.palette.BitmapPaletteTarget {
    
    protected final int getDefaultFooterColor() {
        return 0;
    }
    
    protected final int getAlbumArtistFooterColor() {
        return 0;
    }
    
    @java.lang.Override()
    public void onLoadFailed(@org.jetbrains.annotations.Nullable()
    android.graphics.drawable.Drawable errorDrawable) {
    }
    
    @java.lang.Override()
    public void onResourceReady(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper resource, @org.jetbrains.annotations.Nullable()
    com.bumptech.glide.request.transition.Transition<? super code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper> glideAnimation) {
    }
    
    public abstract void onColorReady(int color);
    
    public RetroMusicColoredTarget(@org.jetbrains.annotations.NotNull()
    android.widget.ImageView view) {
        super(null);
    }
}