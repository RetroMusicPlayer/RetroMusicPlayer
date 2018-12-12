package code.name.monkey.retromusic.ui.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0015\u0016B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002J\b\u0010\r\u001a\u00020\u000bH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u000bH\u0016J\u0018\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u000bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SkuDetailsAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcode/name/monkey/retromusic/ui/activities/SkuDetailsAdapter$ViewHolder;", "donationsDialog", "Lcode/name/monkey/retromusic/ui/activities/SupportDevelopmentActivity;", "objects", "", "Lcom/anjlab/android/iab/v3/SkuDetails;", "(Lcode/name/monkey/retromusic/ui/activities/SupportDevelopmentActivity;Ljava/util/List;)V", "skuDetailsList", "getIcon", "", "position", "getItemCount", "onBindViewHolder", "", "viewHolder", "i", "onCreateViewHolder", "viewGroup", "Landroid/view/ViewGroup;", "Companion", "ViewHolder", "app_normalDebug"})
public final class SkuDetailsAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<code.name.monkey.retromusic.ui.activities.SkuDetailsAdapter.ViewHolder> {
    private java.util.List<? extends com.anjlab.android.iab.v3.SkuDetails> skuDetailsList;
    private code.name.monkey.retromusic.ui.activities.SupportDevelopmentActivity donationsDialog;
    @androidx.annotation.LayoutRes()
    private static final int LAYOUT_RES_ID = 2131558564;
    public static final code.name.monkey.retromusic.ui.activities.SkuDetailsAdapter.Companion Companion = null;
    
    private final int getIcon(int position) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public code.name.monkey.retromusic.ui.activities.SkuDetailsAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup viewGroup, int i) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.activities.SkuDetailsAdapter.ViewHolder viewHolder, int i) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public SkuDetailsAdapter(@org.jetbrains.annotations.NotNull()
    code.name.monkey.retromusic.ui.activities.SupportDevelopmentActivity donationsDialog, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.anjlab.android.iab.v3.SkuDetails> objects) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R\u001a\u0010\u0014\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000e\"\u0004\b\u0016\u0010\u0010\u00a8\u0006\u0017"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SkuDetailsAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "image", "Lcode/name/monkey/retromusic/views/IconImageView;", "getImage", "()Lcode/name/monkey/retromusic/views/IconImageView;", "setImage", "(Lcode/name/monkey/retromusic/views/IconImageView;)V", "price", "Landroid/widget/TextView;", "getPrice", "()Landroid/widget/TextView;", "setPrice", "(Landroid/widget/TextView;)V", "text", "getText", "setText", "title", "getTitle", "setTitle", "app_normalDebug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView title;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView text;
        @org.jetbrains.annotations.NotNull()
        private android.widget.TextView price;
        @org.jetbrains.annotations.NotNull()
        private code.name.monkey.retromusic.views.IconImageView image;
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getTitle() {
            return null;
        }
        
        public final void setTitle(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getText() {
            return null;
        }
        
        public final void setText(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.widget.TextView getPrice() {
            return null;
        }
        
        public final void setPrice(@org.jetbrains.annotations.NotNull()
        android.widget.TextView p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final code.name.monkey.retromusic.views.IconImageView getImage() {
            return null;
        }
        
        public final void setImage(@org.jetbrains.annotations.NotNull()
        code.name.monkey.retromusic.views.IconImageView p0) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View view) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0002X\u0083D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/SkuDetailsAdapter$Companion;", "", "()V", "LAYOUT_RES_ID", "", "strikeThrough", "", "textView", "Landroid/widget/TextView;", "", "app_normalDebug"})
    public static final class Companion {
        
        private final void strikeThrough(android.widget.TextView textView, boolean strikeThrough) {
        }
        
        private Companion() {
            super();
        }
    }
}