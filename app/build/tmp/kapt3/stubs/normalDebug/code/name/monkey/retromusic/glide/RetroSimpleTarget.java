package code.name.monkey.retromusic.glide;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\b\u0007\u0012\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0006J\n\u0010\t\u001a\u0004\u0018\u00010\bH\u0016J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\u000bH\u0016J\u0012\u0010\u000f\u001a\u00020\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\u0012\u0010\u0012\u001a\u00020\u000b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0011H\u0016J\u0012\u0010\u0014\u001a\u00020\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\'\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0016\u001a\u00028\u00002\u0010\u0010\u0017\u001a\f\u0012\u0006\b\u0000\u0012\u00028\u0000\u0018\u00010\u0018H\u0016\u00a2\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u000bH\u0016J\b\u0010\u001b\u001a\u00020\u000bH\u0016J\u0010\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0012\u0010\u001d\u001a\u00020\u000b2\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0016R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcode/name/monkey/retromusic/glide/RetroSimpleTarget;", "T", "Lcom/bumptech/glide/request/target/Target;", "width", "", "height", "(II)V", "request", "Lcom/bumptech/glide/request/Request;", "getRequest", "getSize", "", "cb", "Lcom/bumptech/glide/request/target/SizeReadyCallback;", "onDestroy", "onLoadCleared", "placeholder", "Landroid/graphics/drawable/Drawable;", "onLoadFailed", "errorDrawable", "onLoadStarted", "onResourceReady", "resource", "transition", "Lcom/bumptech/glide/request/transition/Transition;", "(Ljava/lang/Object;Lcom/bumptech/glide/request/transition/Transition;)V", "onStart", "onStop", "removeCallback", "setRequest", "app_normalDebug"})
public class RetroSimpleTarget<T extends java.lang.Object> implements com.bumptech.glide.request.target.Target<T> {
    private com.bumptech.glide.request.Request request;
    private final int width = 0;
    private final int height = 0;
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public com.bumptech.glide.request.Request getRequest() {
        return null;
    }
    
    @java.lang.Override()
    public void setRequest(@org.jetbrains.annotations.Nullable()
    com.bumptech.glide.request.Request request) {
    }
    
    @java.lang.Override()
    public void onLoadStarted(@org.jetbrains.annotations.Nullable()
    android.graphics.drawable.Drawable placeholder) {
    }
    
    @java.lang.Override()
    public void onLoadFailed(@org.jetbrains.annotations.Nullable()
    android.graphics.drawable.Drawable errorDrawable) {
    }
    
    @java.lang.Override()
    public void onResourceReady(T resource, @org.jetbrains.annotations.Nullable()
    com.bumptech.glide.request.transition.Transition<? super T> transition) {
    }
    
    @java.lang.Override()
    public void onLoadCleared(@org.jetbrains.annotations.Nullable()
    android.graphics.drawable.Drawable placeholder) {
    }
    
    @java.lang.Override()
    public void getSize(@org.jetbrains.annotations.NotNull()
    com.bumptech.glide.request.target.SizeReadyCallback cb) {
    }
    
    @java.lang.Override()
    public void removeCallback(@org.jetbrains.annotations.NotNull()
    com.bumptech.glide.request.target.SizeReadyCallback cb) {
    }
    
    @java.lang.Override()
    public void onStart() {
    }
    
    @java.lang.Override()
    public void onStop() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    public RetroSimpleTarget(int width, int height) {
        super();
    }
    
    public RetroSimpleTarget(int width) {
        super();
    }
    
    public RetroSimpleTarget() {
        super();
    }
}