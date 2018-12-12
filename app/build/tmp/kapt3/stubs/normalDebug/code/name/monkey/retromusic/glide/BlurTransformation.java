package code.name.monkey.retromusic.glide;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001f2\u00020\u0001:\u0002\u001e\u001fB\u000f\b\u0012\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0012\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\rH\u0016J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J*\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\rH\u0014J\u0010\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcode/name/monkey/retromusic/glide/BlurTransformation;", "Lcom/bumptech/glide/load/resource/bitmap/BitmapTransformation;", "builder", "Lcode/name/monkey/retromusic/glide/BlurTransformation$Builder;", "(Lcode/name/monkey/retromusic/glide/BlurTransformation$Builder;)V", "bitmapPool", "Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;", "(Lcode/name/monkey/retromusic/glide/BlurTransformation$Builder;Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;)V", "blurRadius", "", "context", "Landroid/content/Context;", "sampling", "", "equals", "", "o", "", "hashCode", "init", "", "transform", "Landroid/graphics/Bitmap;", "pool", "toTransform", "outWidth", "outHeight", "updateDiskCacheKey", "messageDigest", "Ljava/security/MessageDigest;", "Builder", "Companion", "app_normalDebug"})
public final class BlurTransformation extends com.bumptech.glide.load.resource.bitmap.BitmapTransformation {
    private android.content.Context context;
    private float blurRadius;
    private int sampling;
    private static final float DEFAULT_BLUR_RADIUS = 5.0F;
    private static final java.lang.String ID = "com.poupa.vinylmusicplayer.glide.BlurTransformation";
    public static final code.name.monkey.retromusic.glide.BlurTransformation.Companion Companion = null;
    
    private final void init(code.name.monkey.retromusic.glide.BlurTransformation.Builder builder) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    protected android.graphics.Bitmap transform(@org.jetbrains.annotations.NotNull()
    com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool pool, @org.jetbrains.annotations.NotNull()
    android.graphics.Bitmap toTransform, int outWidth, int outHeight) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object o) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    public void updateDiskCacheKey(@org.jetbrains.annotations.NotNull()
    java.security.MessageDigest messageDigest) {
    }
    
    private BlurTransformation(code.name.monkey.retromusic.glide.BlurTransformation.Builder builder) {
        super();
    }
    
    private BlurTransformation(code.name.monkey.retromusic.glide.BlurTransformation.Builder builder, com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool bitmapPool) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u000b\u001a\u00020\u00002\b\b\u0001\u0010\u000b\u001a\u00020\fJ\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u0013\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0014R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018\u00a8\u0006\u001b"}, d2 = {"Lcode/name/monkey/retromusic/glide/BlurTransformation$Builder;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "bitmapPool", "Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;", "getBitmapPool", "()Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;", "setBitmapPool", "(Lcom/bumptech/glide/load/engine/bitmap_recycle/BitmapPool;)V", "blurRadius", "", "getBlurRadius", "()F", "setBlurRadius", "(F)V", "getContext", "()Landroid/content/Context;", "sampling", "", "getSampling", "()I", "setSampling", "(I)V", "build", "Lcode/name/monkey/retromusic/glide/BlurTransformation;", "app_normalDebug"})
    public static final class Builder {
        @org.jetbrains.annotations.Nullable()
        private com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool bitmapPool;
        private float blurRadius;
        private int sampling;
        @org.jetbrains.annotations.NotNull()
        private final android.content.Context context = null;
        
        @org.jetbrains.annotations.Nullable()
        public final com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool getBitmapPool() {
            return null;
        }
        
        public final void setBitmapPool(@org.jetbrains.annotations.Nullable()
        com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool p0) {
        }
        
        public final float getBlurRadius() {
            return 0.0F;
        }
        
        public final void setBlurRadius(float p0) {
        }
        
        public final int getSampling() {
            return 0;
        }
        
        public final void setSampling(int p0) {
        }
        
        /**
         * * @param blurRadius The radius to use. Must be between 0 and 25. Default is 5.
         *         * @return the same Builder
         */
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.glide.BlurTransformation.Builder blurRadius(@androidx.annotation.FloatRange(from = 0.0, to = 25.0)
        float blurRadius) {
            return null;
        }
        
        /**
         * * @param sampling The inSampleSize to use. Must be a power of 2, or 1 for no down sampling or 0 for auto detect sampling. Default is 0.
         *         * @return the same Builder
         */
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.glide.BlurTransformation.Builder sampling(int sampling) {
            return null;
        }
        
        /**
         * * @param bitmapPool The BitmapPool to use.
         *         * @return the same Builder
         */
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.glide.BlurTransformation.Builder bitmapPool(@org.jetbrains.annotations.NotNull()
        com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool bitmapPool) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.glide.BlurTransformation build() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Context getContext() {
            return null;
        }
        
        public Builder(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcode/name/monkey/retromusic/glide/BlurTransformation$Companion;", "", "()V", "DEFAULT_BLUR_RADIUS", "", "getDEFAULT_BLUR_RADIUS", "()F", "ID", "", "app_normalDebug"})
    public static final class Companion {
        
        public final float getDEFAULT_BLUR_RADIUS() {
            return 0.0F;
        }
        
        private Companion() {
            super();
        }
    }
}