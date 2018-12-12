package code.name.monkey.retromusic.ui.adapter.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0012\u0010J\u001a\u00020K2\b\u0010L\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010M\u001a\u00020N2\b\u0010L\u001a\u0004\u0018\u00010\u0005H\u0016J\u000e\u0010O\u001a\u00020K2\u0006\u0010P\u001a\u00020QR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u0006R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001c\u0010#\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u001a\"\u0004\b%\u0010\u001cR\u001c\u0010&\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\'\u0010\t\"\u0004\b(\u0010\u0006R\u001c\u0010)\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\t\"\u0004\b+\u0010\u0006R\u001c\u0010,\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\t\"\u0004\b.\u0010\u0006R\u001c\u0010/\u001a\u0004\u0018\u000100X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b1\u00102\"\u0004\b3\u00104R\u001c\u00105\u001a\u0004\u0018\u000106X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b7\u00108\"\u0004\b9\u0010:R\u001c\u0010;\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b<\u0010\t\"\u0004\b=\u0010\u0006R\u001c\u0010>\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b?\u0010\t\"\u0004\b@\u0010\u0006R\u001c\u0010A\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bB\u0010 \"\u0004\bC\u0010\"R\u001c\u0010D\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bE\u0010 \"\u0004\bF\u0010\"R\u001c\u0010G\u001a\u0004\u0018\u00010\u001eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bH\u0010 \"\u0004\bI\u0010\"\u00a8\u0006R"}, d2 = {"Lcode/name/monkey/retromusic/ui/adapter/base/MediaEntryViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "Landroid/view/View$OnClickListener;", "Landroid/view/View$OnLongClickListener;", "w", "Landroid/view/View;", "(Landroid/view/View;)V", "dragView", "getDragView", "()Landroid/view/View;", "setDragView", "image", "Landroid/widget/ImageView;", "getImage", "()Landroid/widget/ImageView;", "setImage", "(Landroid/widget/ImageView;)V", "imageContainer", "Landroid/view/ViewGroup;", "getImageContainer", "()Landroid/view/ViewGroup;", "setImageContainer", "(Landroid/view/ViewGroup;)V", "imageContainerCard", "Landroidx/cardview/widget/CardView;", "getImageContainerCard", "()Landroidx/cardview/widget/CardView;", "setImageContainerCard", "(Landroidx/cardview/widget/CardView;)V", "imageText", "Landroid/widget/TextView;", "getImageText", "()Landroid/widget/TextView;", "setImageText", "(Landroid/widget/TextView;)V", "imageTextContainer", "getImageTextContainer", "setImageTextContainer", "mask", "getMask", "setMask", "menu", "getMenu", "setMenu", "paletteColorContainer", "getPaletteColorContainer", "setPaletteColorContainer", "playSongs", "Landroid/widget/ImageButton;", "getPlaySongs", "()Landroid/widget/ImageButton;", "setPlaySongs", "(Landroid/widget/ImageButton;)V", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "getRecyclerView", "()Landroidx/recyclerview/widget/RecyclerView;", "setRecyclerView", "(Landroidx/recyclerview/widget/RecyclerView;)V", "separator", "getSeparator", "setSeparator", "shortSeparator", "getShortSeparator", "setShortSeparator", "text", "getText", "setText", "time", "getTime", "setTime", "title", "getTitle", "setTitle", "onClick", "", "v", "onLongClick", "", "setImageTransitionName", "transitionName", "", "app_normalDebug"})
public class MediaEntryViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder implements android.view.View.OnClickListener, android.view.View.OnLongClickListener {
    @org.jetbrains.annotations.Nullable()
    private android.widget.ImageView image;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView imageText;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView title;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView text;
    @org.jetbrains.annotations.Nullable()
    private android.view.ViewGroup imageContainer;
    @org.jetbrains.annotations.Nullable()
    private androidx.cardview.widget.CardView imageContainerCard;
    @org.jetbrains.annotations.Nullable()
    private android.view.View menu;
    @org.jetbrains.annotations.Nullable()
    private android.view.View separator;
    @org.jetbrains.annotations.Nullable()
    private android.view.View shortSeparator;
    @org.jetbrains.annotations.Nullable()
    private android.view.View dragView;
    @org.jetbrains.annotations.Nullable()
    private android.view.View paletteColorContainer;
    @org.jetbrains.annotations.Nullable()
    private android.widget.TextView time;
    @org.jetbrains.annotations.Nullable()
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    @org.jetbrains.annotations.Nullable()
    private android.widget.ImageButton playSongs;
    @org.jetbrains.annotations.Nullable()
    private android.view.View mask;
    @org.jetbrains.annotations.Nullable()
    private androidx.cardview.widget.CardView imageTextContainer;
    
    @java.lang.Override()
    public boolean onLongClick(@org.jetbrains.annotations.Nullable()
    android.view.View v) {
        return false;
    }
    
    @java.lang.Override()
    public void onClick(@org.jetbrains.annotations.Nullable()
    android.view.View v) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.ImageView getImage() {
        return null;
    }
    
    public final void setImage(@org.jetbrains.annotations.Nullable()
    android.widget.ImageView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.TextView getImageText() {
        return null;
    }
    
    public final void setImageText(@org.jetbrains.annotations.Nullable()
    android.widget.TextView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.TextView getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.Nullable()
    android.widget.TextView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.TextView getText() {
        return null;
    }
    
    public final void setText(@org.jetbrains.annotations.Nullable()
    android.widget.TextView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.ViewGroup getImageContainer() {
        return null;
    }
    
    public final void setImageContainer(@org.jetbrains.annotations.Nullable()
    android.view.ViewGroup p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final androidx.cardview.widget.CardView getImageContainerCard() {
        return null;
    }
    
    public final void setImageContainerCard(@org.jetbrains.annotations.Nullable()
    androidx.cardview.widget.CardView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.View getMenu() {
        return null;
    }
    
    public final void setMenu(@org.jetbrains.annotations.Nullable()
    android.view.View p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.View getSeparator() {
        return null;
    }
    
    public final void setSeparator(@org.jetbrains.annotations.Nullable()
    android.view.View p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.View getShortSeparator() {
        return null;
    }
    
    public final void setShortSeparator(@org.jetbrains.annotations.Nullable()
    android.view.View p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.View getDragView() {
        return null;
    }
    
    public final void setDragView(@org.jetbrains.annotations.Nullable()
    android.view.View p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.View getPaletteColorContainer() {
        return null;
    }
    
    public final void setPaletteColorContainer(@org.jetbrains.annotations.Nullable()
    android.view.View p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.TextView getTime() {
        return null;
    }
    
    public final void setTime(@org.jetbrains.annotations.Nullable()
    android.widget.TextView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final androidx.recyclerview.widget.RecyclerView getRecyclerView() {
        return null;
    }
    
    public final void setRecyclerView(@org.jetbrains.annotations.Nullable()
    androidx.recyclerview.widget.RecyclerView p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.widget.ImageButton getPlaySongs() {
        return null;
    }
    
    public final void setPlaySongs(@org.jetbrains.annotations.Nullable()
    android.widget.ImageButton p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.view.View getMask() {
        return null;
    }
    
    public final void setMask(@org.jetbrains.annotations.Nullable()
    android.view.View p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final androidx.cardview.widget.CardView getImageTextContainer() {
        return null;
    }
    
    public final void setImageTextContainer(@org.jetbrains.annotations.Nullable()
    androidx.cardview.widget.CardView p0) {
    }
    
    public final void setImageTransitionName(@org.jetbrains.annotations.NotNull()
    java.lang.String transitionName) {
    }
    
    public MediaEntryViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.View w) {
        super(null);
    }
}