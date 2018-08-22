package code.name.monkey.retromusic.ui.activities.tageditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.jaudiotagger.tag.FieldKey;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.loaders.AlbumLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.rest.LastFMRestClient;
import code.name.monkey.retromusic.rest.model.LastFmAlbum;
import code.name.monkey.retromusic.util.ImageUtil;
import code.name.monkey.retromusic.util.LastFMUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AlbumTagEditorActivity extends AbsTagEditorActivity implements TextWatcher {

    public static final String TAG = AlbumTagEditorActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.title)
    EditText albumTitle;
    @BindView(R.id.album_artist)
    EditText albumArtist;
    @BindView(R.id.genre)
    EditText genre;
    @BindView(R.id.year)
    EditText year;
    @BindView(R.id.gradient_background)
    View background;
    private Bitmap albumArtBitmap;
    private boolean deleteAlbumArt;
    private LastFMRestClient lastFMRestClient;

    private void setupToolbar() {
        //toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setTitle(R.string.action_tag_editor);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.edit)
    void edit() {
        getShow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        lastFMRestClient = new LastFMRestClient(this);

        setUpViews();
        setupToolbar();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_album_tag_editor;
    }

    private void setUpViews() {
        fillViewsWithFileTags();
        albumTitle.addTextChangedListener(this);
        albumArtist.addTextChangedListener(this);
        genre.addTextChangedListener(this);
        year.addTextChangedListener(this);
    }


    private void fillViewsWithFileTags() {
        albumTitle.setText(getAlbumTitle());
        albumArtist.setText(getAlbumArtistName());
        genre.setText(getGenreName());
        year.setText(getSongYear());
    }

    @Override
    protected void loadCurrentImage() {
        Bitmap bitmap = getAlbumArt();
        setImageBitmap(bitmap, RetroColorUtil.getColor(RetroColorUtil.generatePalette(bitmap),
                ATHUtil.resolveColor(this, R.attr.defaultFooterColor)));
        deleteAlbumArt = false;
    }

    @Override
    protected void getImageFromLastFM() {
        String albumTitleStr = albumTitle.getText().toString();
        String albumArtistNameStr = albumArtist.getText().toString();
        if (albumArtistNameStr.trim().equals("") || albumTitleStr.trim().equals("")) {
            Toast.makeText(this, getResources().getString(R.string.album_or_artist_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        lastFMRestClient.getApiService()
                .getAlbumInfo(albumTitleStr, albumArtistNameStr, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::extractDetails);
    }

    private void extractDetails(@NonNull LastFmAlbum lastFmAlbum) {
        if (lastFmAlbum.getAlbum() != null) {

            String url = LastFMUtil.getLargestAlbumImageUrl(lastFmAlbum.getAlbum().getImage());

            if (!TextUtils.isEmpty(url) && url.trim().length() > 0) {
                Glide.with(this)
                        .load(url)
                        .asBitmap()
                        .transcode(new BitmapPaletteTranscoder(this), BitmapPaletteWrapper.class)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.default_album_art)
                        .into(new SimpleTarget<BitmapPaletteWrapper>() {
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                e.printStackTrace();
                                Toast.makeText(AlbumTagEditorActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResourceReady(BitmapPaletteWrapper resource,
                                                        GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
                                albumArtBitmap = ImageUtil.resizeBitmap(resource.getBitmap(), 2048);
                                setImageBitmap(albumArtBitmap, RetroColorUtil.getColor(resource.getPalette(),
                                        ContextCompat.getColor(AlbumTagEditorActivity.this, R.color.md_grey_500)));
                                deleteAlbumArt = false;
                                dataChanged();
                                setResult(RESULT_OK);
                            }
                        });
                return;
            }
            if (lastFmAlbum.getAlbum().getTags().getTag().size() > 0) {
                genre.setText(lastFmAlbum.getAlbum().getTags().getTag().get(0).getName());
            }

        }
        toastLoadingFailed();
    }

    private void toastLoadingFailed() {
        Toast.makeText(AlbumTagEditorActivity.this,
                R.string.could_not_download_album_cover, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void searchImageOnWeb() {
        searchWebFor(albumTitle.getText().toString(), albumArtist.getText().toString());
    }

    @Override
    protected void deleteImage() {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_album_art),
                ATHUtil.resolveColor(this, R.attr.defaultFooterColor));
        deleteAlbumArt = true;
        dataChanged();
    }

    @Override
    protected void save() {
        Map<FieldKey, String> fieldKeyValueMap = new EnumMap<>(FieldKey.class);
        fieldKeyValueMap.put(FieldKey.ALBUM, albumTitle.getText().toString());
        //android seems not to recognize album_artist field so we additionally write the normal artist field
        fieldKeyValueMap.put(FieldKey.ARTIST, albumArtist.getText().toString());
        fieldKeyValueMap.put(FieldKey.ALBUM_ARTIST, albumArtist.getText().toString());
        fieldKeyValueMap.put(FieldKey.GENRE, genre.getText().toString());
        fieldKeyValueMap.put(FieldKey.YEAR, year.getText().toString());

        writeValuesToFiles(fieldKeyValueMap, deleteAlbumArt ? new ArtworkInfo(getId(), null)
                : albumArtBitmap == null ? null : new ArtworkInfo(getId(), albumArtBitmap));
    }

    @NonNull
    @Override
    protected List<String> getSongPaths() {
        ArrayList<Song> songs = AlbumLoader.getAlbum(this, getId()).blockingFirst().songs;
        ArrayList<String> paths = new ArrayList<>(songs.size());
        for (Song song : songs) {
            paths.add(song.data);
        }
        return paths;
    }

    @Override
    protected void loadImageFromFile(@NonNull final Uri selectedFileUri) {
        Glide.with(AlbumTagEditorActivity.this)
                .load(selectedFileUri)
                .asBitmap()
                .transcode(new BitmapPaletteTranscoder(AlbumTagEditorActivity.this),
                        BitmapPaletteWrapper.class)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<BitmapPaletteWrapper>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        e.printStackTrace();
                        Toast.makeText(AlbumTagEditorActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResourceReady(BitmapPaletteWrapper resource,
                                                GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
                        RetroColorUtil.getColor(resource.getPalette(), Color.TRANSPARENT);
                        albumArtBitmap = ImageUtil.resizeBitmap(resource.getBitmap(), 2048);
                        setImageBitmap(albumArtBitmap, RetroColorUtil.getColor(resource.getPalette(),
                                ATHUtil.resolveColor(AlbumTagEditorActivity.this, R.attr.defaultFooterColor)));
                        deleteAlbumArt = false;
                        dataChanged();
                        setResult(RESULT_OK);
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        dataChanged();
    }

    @Override
    protected void setColors(int color) {
        super.setColors(color);
        background.setBackgroundColor(color);
        toolbar.setBackgroundColor(color);
        setStatusbarColor(ColorUtil.darkenColor(color));
        setNavigationbarColor(ColorUtil.darkenColor(color));
        setSupportActionBar(toolbar);
    }
}
