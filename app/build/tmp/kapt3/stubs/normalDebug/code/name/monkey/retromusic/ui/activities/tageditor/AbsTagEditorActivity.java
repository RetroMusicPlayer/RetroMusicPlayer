package code.name.monkey.retromusic.ui.activities.tageditor;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018\u0000 a2\u00020\u0001:\u0002`aB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u00101\u001a\u000202H\u0004J\b\u00103\u001a\u000202H$J\u0010\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\bH\u0002J\b\u00107\u001a\u000202H$J\b\u00108\u001a\u000202H\u0002J\u000e\u00109\u001a\b\u0012\u0004\u0012\u00020\b0*H$J\b\u0010:\u001a\u000202H$J\u0012\u0010;\u001a\u0002022\b\u0010<\u001a\u0004\u0018\u00010=H$J\"\u0010>\u001a\u0002022\u0006\u0010?\u001a\u00020\u00122\u0006\u0010@\u001a\u00020\u00122\b\u0010A\u001a\u0004\u0018\u00010BH\u0014J\u0012\u0010C\u001a\u0002022\b\u0010D\u001a\u0004\u0018\u00010EH\u0014J\u0010\u0010F\u001a\u00020\u001d2\u0006\u0010G\u001a\u00020HH\u0016J\b\u0010I\u001a\u000202H$J\b\u0010J\u001a\u000202H$J!\u0010K\u001a\u0002022\u0012\u0010L\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u001f\"\u00020\bH\u0004\u00a2\u0006\u0002\u0010MJ\u0010\u0010N\u001a\u0002022\u0006\u0010O\u001a\u00020\u0012H\u0014J\u001a\u0010P\u001a\u0002022\b\u0010Q\u001a\u0004\u0018\u00010\u00042\u0006\u0010R\u001a\u00020\u0012H\u0004J\b\u0010S\u001a\u000202H\u0004J\b\u0010T\u001a\u000202H\u0002J\b\u0010U\u001a\u000202H\u0002J\b\u0010V\u001a\u000202H\u0002J\b\u0010W\u001a\u000202H\u0002J\b\u0010X\u001a\u000202H\u0002J\b\u0010Y\u001a\u000202H\u0002J&\u0010Z\u001a\u0002022\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020]\u0012\u0004\u0012\u00020\b0\\2\b\u0010^\u001a\u0004\u0018\u00010_H\u0004R\u0016\u0010\u0003\u001a\u0004\u0018\u00010\u00048DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b8@X\u0080\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0016\u0010\u000b\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\nR\u0016\u0010\r\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\nR\u0016\u0010\u000f\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\nR\u0012\u0010\u0011\u001a\u00020\u0012X\u00a4\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0016\u0010\u0015\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\nR$\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0012@BX\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0014\"\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001fX\u0082.\u00a2\u0006\u0004\n\u0002\u0010!R\u0016\u0010\"\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010\nR\u000e\u0010$\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010%\u001a\u00020&8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b\'\u0010(R\u0016\u0010)\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010*X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010+\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010\nR\u0016\u0010-\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010\nR\u0016\u0010/\u001a\u0004\u0018\u00010\b8DX\u0084\u0004\u00a2\u0006\u0006\u001a\u0004\b0\u0010\n\u00a8\u0006b"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/tageditor/AbsTagEditorActivity;", "Lcode/name/monkey/retromusic/ui/activities/base/AbsBaseActivity;", "()V", "albumArt", "Landroid/graphics/Bitmap;", "getAlbumArt", "()Landroid/graphics/Bitmap;", "albumArtist", "", "getAlbumArtist$app_normalDebug", "()Ljava/lang/String;", "albumArtistName", "getAlbumArtistName", "albumTitle", "getAlbumTitle", "artistName", "getArtistName", "contentViewLayout", "", "getContentViewLayout", "()I", "genreName", "getGenreName", "<set-?>", "id", "getId", "setId", "(I)V", "isInNoImageMode", "", "items", "", "", "[Ljava/lang/CharSequence;", "lyrics", "getLyrics", "paletteColorPrimary", "show", "Lcom/afollestad/materialdialogs/MaterialDialog;", "getShow", "()Lcom/afollestad/materialdialogs/MaterialDialog;", "songPaths", "", "songTitle", "getSongTitle", "songYear", "getSongYear", "trackNumber", "getTrackNumber", "dataChanged", "", "deleteImage", "getAudioFile", "Lorg/jaudiotagger/audio/AudioFile;", "path", "getImageFromLastFM", "getIntentExtras", "getSongPaths", "loadCurrentImage", "loadImageFromFile", "selectedFile", "Landroid/net/Uri;", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "save", "searchImageOnWeb", "searchWebFor", "keys", "([Ljava/lang/String;)V", "setColors", "color", "setImageBitmap", "bitmap", "bgColor", "setNoImageMode", "setUpFab", "setUpImageView", "setUpScrollView", "setUpViews", "showFab", "startImagePicker", "writeValuesToFiles", "fieldKeyValueMap", "", "Lorg/jaudiotagger/tag/FieldKey;", "artworkInfo", "Lcode/name/monkey/retromusic/ui/activities/tageditor/AbsTagEditorActivity$ArtworkInfo;", "ArtworkInfo", "Companion", "app_normalDebug"})
public abstract class AbsTagEditorActivity extends code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity {
    private java.lang.CharSequence[] items;
    private int id;
    private int paletteColorPrimary;
    private boolean isInNoImageMode;
    private java.util.List<java.lang.String> songPaths;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_ID = "extra_id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EXTRA_PALETTE = "extra_palette";
    private static final java.lang.String TAG = null;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1000;
    public static final code.name.monkey.retromusic.ui.activities.tageditor.AbsTagEditorActivity.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    protected final int getId() {
        return 0;
    }
    
    private final void setId(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected final com.afollestad.materialdialogs.MaterialDialog getShow() {
        return null;
    }
    
    protected abstract int getContentViewLayout();
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAlbumArtist$app_normalDebug() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getSongTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getAlbumTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getArtistName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getAlbumArtistName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getGenreName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getSongYear() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getTrackNumber() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final java.lang.String getLyrics() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final android.graphics.Bitmap getAlbumArt() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setUpViews() {
    }
    
    private final void setUpScrollView() {
    }
    
    private final void setUpImageView() {
    }
    
    private final void startImagePicker() {
    }
    
    protected abstract void loadCurrentImage();
    
    protected abstract void getImageFromLastFM();
    
    protected abstract void searchImageOnWeb();
    
    protected abstract void deleteImage();
    
    private final void setUpFab() {
    }
    
    protected abstract void save();
    
    private final void getIntentExtras() {
    }
    
    @org.jetbrains.annotations.NotNull()
    protected abstract java.util.List<java.lang.String> getSongPaths();
    
    protected final void searchWebFor(@org.jetbrains.annotations.NotNull()
    java.lang.String... keys) {
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    protected final void setNoImageMode() {
    }
    
    protected final void dataChanged() {
    }
    
    private final void showFab() {
    }
    
    protected final void setImageBitmap(@org.jetbrains.annotations.Nullable()
    android.graphics.Bitmap bitmap, int bgColor) {
    }
    
    protected void setColors(int color) {
    }
    
    protected final void writeValuesToFiles(@org.jetbrains.annotations.NotNull()
    java.util.Map<org.jaudiotagger.tag.FieldKey, java.lang.String> fieldKeyValueMap, @org.jetbrains.annotations.Nullable()
    code.name.monkey.retromusic.ui.activities.tageditor.AbsTagEditorActivity.ArtworkInfo artworkInfo) {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    protected abstract void loadImageFromFile(@org.jetbrains.annotations.Nullable()
    android.net.Uri selectedFile);
    
    private final org.jaudiotagger.audio.AudioFile getAudioFile(java.lang.String path) {
        return null;
    }
    
    public AbsTagEditorActivity() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/tageditor/AbsTagEditorActivity$ArtworkInfo;", "", "albumId", "", "artwork", "Landroid/graphics/Bitmap;", "(ILandroid/graphics/Bitmap;)V", "getAlbumId", "()I", "getArtwork", "()Landroid/graphics/Bitmap;", "app_normalDebug"})
    public static final class ArtworkInfo {
        private final int albumId = 0;
        @org.jetbrains.annotations.Nullable()
        private final android.graphics.Bitmap artwork = null;
        
        public final int getAlbumId() {
            return 0;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final android.graphics.Bitmap getArtwork() {
            return null;
        }
        
        public ArtworkInfo(int albumId, @org.jetbrains.annotations.Nullable()
        android.graphics.Bitmap artwork) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \t*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcode/name/monkey/retromusic/ui/activities/tageditor/AbsTagEditorActivity$Companion;", "", "()V", "EXTRA_ID", "", "EXTRA_PALETTE", "REQUEST_CODE_SELECT_IMAGE", "", "TAG", "kotlin.jvm.PlatformType", "app_normalDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}