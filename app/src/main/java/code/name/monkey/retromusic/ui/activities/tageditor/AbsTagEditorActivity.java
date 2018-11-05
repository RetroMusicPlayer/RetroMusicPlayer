package code.name.monkey.retromusic.ui.activities.tageditor;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.base.AbsBaseActivity;
import code.name.monkey.retromusic.util.RetroUtil;

public abstract class AbsTagEditorActivity extends AbsBaseActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_PALETTE = "extra_palette";
    private static final String TAG = AbsTagEditorActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SELECT_IMAGE = 1000;
    @BindView(R.id.save_fab)
    FloatingActionButton save;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.image_container)
    FrameLayout imageContainer;

    CharSequence[] items;
    private int id;
    private int paletteColorPrimary;
    private boolean isInNoImageMode;
    private List<String> songPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        ButterKnife.bind(this);

        getIntentExtras();

        songPaths = getSongPaths();
        if (songPaths.isEmpty()) {
            finish();
            return;
        }

        setUpViews();


        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
    }

    private void setUpViews() {
        setUpScrollView();
        setUpFab();
        setUpImageView();
    }

    private void setUpScrollView() {
        //observableScrollView.setScrollViewCallbacks(observableScrollViewCallbacks);
    }

    private void setUpImageView() {
        loadCurrentImage();
        items = new CharSequence[]{
                getString(R.string.download_from_last_fm),
                getString(R.string.pick_from_local_storage),
                getString(R.string.web_search),
                getString(R.string.remove_cover)
        };
        image.setOnClickListener(v -> getShow());
    }

    protected MaterialDialog getShow() {
        return new MaterialDialog.Builder(AbsTagEditorActivity.this)
                .title(R.string.update_image)
                .items(items)
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            getImageFromLastFM();
                            break;
                        case 1:
                            startImagePicker();
                            break;
                        case 2:
                            searchImageOnWeb();
                            break;
                        case 3:
                            deleteImage();
                            break;
                    }
                }).show();
    }

    private void startImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.pick_from_local_storage)),
                REQUEST_CODE_SELECT_IMAGE);
    }

    protected abstract void loadCurrentImage();

    protected abstract void getImageFromLastFM();

    protected abstract void searchImageOnWeb();

    protected abstract void deleteImage();

    private void setUpFab() {
        save.setScaleX(0);
        save.setScaleY(0);
        save.setEnabled(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        TintHelper.setTintAuto(save, ThemeStore.accentColor(this), true);
    }

    protected abstract void save();

    private void getIntentExtras() {
        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            id = intentExtras.getInt(EXTRA_ID);
        }
    }

    protected abstract int getContentViewLayout();

    @NonNull
    protected abstract List<String> getSongPaths();

    protected void searchWebFor(String... keys) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            stringBuilder.append(key);
            stringBuilder.append(" ");
        }
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, stringBuilder.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setNoImageMode() {
        isInNoImageMode = true;
        imageContainer.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        image.setEnabled(false);

        setColors(getIntent().getIntExtra(EXTRA_PALETTE, ThemeStore.primaryColor(this)));
    }

    protected void dataChanged() {
        showFab();
    }

    private void showFab() {
        save.animate()
                .setDuration(500)
                .setInterpolator(new OvershootInterpolator())
                .scaleX(1)
                .scaleY(1)
                .start();
        save.setEnabled(true);
    }

    protected void setImageBitmap(@Nullable final Bitmap bitmap, int bgColor) {
        if (bitmap == null) {
            image.setImageResource(R.drawable.default_album_art);
        } else {
            image.setImageBitmap(bitmap);
        }
        setColors(bgColor);
    }

    protected void setColors(int color) {
        paletteColorPrimary = color;
    }

    protected void writeValuesToFiles(@NonNull final Map<FieldKey, String> fieldKeyValueMap,
                                      @Nullable final ArtworkInfo artworkInfo) {
        RetroUtil.hideSoftKeyboard(this);

        new WriteTagsAsyncTask(this)
                .execute(new WriteTagsAsyncTask.LoadingInfo(getSongPaths(), fieldKeyValueMap, artworkInfo));
    }

    protected int getId() {
        return id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @NonNull Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    loadImageFromFile(selectedImage);
                }
                break;
        }
    }

    protected abstract void loadImageFromFile(Uri selectedFile);

    @NonNull
    private AudioFile getAudioFile(@NonNull String path) {
        try {
            return AudioFileIO.read(new File(path));
        } catch (Exception e) {
            Log.e(TAG, "Could not read audio file " + path, e);
            return new AudioFile();
        }
    }

    @Nullable
    String getAlbumArtist() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault()
                    .getFirst(FieldKey.ALBUM_ARTIST);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getSongTitle() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.TITLE);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getAlbumTitle() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.ALBUM);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getArtistName() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.ARTIST);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getAlbumArtistName() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault()
                    .getFirst(FieldKey.ALBUM_ARTIST);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getGenreName() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.GENRE);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getSongYear() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.YEAR);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getTrackNumber() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.TRACK);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getLyrics() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.LYRICS);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected String getComposer() {
        try {
            return getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault().getFirst(FieldKey.COMPOSER);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Nullable
    protected Bitmap getAlbumArt() {
        try {
            Artwork artworkTag = getAudioFile(songPaths.get(0)).getTagOrCreateAndSetDefault()
                    .getFirstArtwork();
            if (artworkTag != null) {
                byte[] artworkBinaryData = artworkTag.getBinaryData();
                return BitmapFactory.decodeByteArray(artworkBinaryData, 0, artworkBinaryData.length);
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static class ArtworkInfo {

        public final int albumId;
        final Bitmap artwork;

        ArtworkInfo(int albumId, Bitmap artwork) {
            this.albumId = albumId;
            this.artwork = artwork;
        }
    }
}
