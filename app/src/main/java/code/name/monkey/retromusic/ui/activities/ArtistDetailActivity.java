package code.name.monkey.retromusic.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog;
import code.name.monkey.retromusic.glide.ArtistGlideRequest;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.misc.AppBarStateChangeListener;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.contract.ArtistDetailContract;
import code.name.monkey.retromusic.mvp.presenter.ArtistDetailsPresenter;
import code.name.monkey.retromusic.rest.LastFMRestClient;
import code.name.monkey.retromusic.rest.model.LastFmArtist;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity;
import code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter;
import code.name.monkey.retromusic.ui.adapter.album.HorizontalAlbumAdapter;
import code.name.monkey.retromusic.ui.adapter.song.SimpleSongAdapter;
import code.name.monkey.retromusic.util.CustomArtistImageUtil;
import code.name.monkey.retromusic.util.DensityUtil;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistDetailActivity extends AbsSlidingMusicPanelActivity implements
        ArtistDetailContract.ArtistsDetailsView {

    public static final String EXTRA_ARTIST_ID = "extra_artist_id";
    private static final int REQUEST_CODE_SELECT_IMAGE = 9003;
    @BindView(R.id.app_bar)
    @Nullable
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    @Nullable
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.biography)
    TextView biographyTextView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.album_recycler_view)
    RecyclerView albumRecyclerView;

    @BindView(R.id.album_title)
    AppCompatTextView albumTitle;

    @BindView(R.id.song_title)
    AppCompatTextView songTitle;

    @BindView(R.id.biography_title)
    AppCompatTextView biographyTitle;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.action_shuffle_all)
    FloatingActionButton shuffleButton;

    @BindView(R.id.gradient_background)
    @Nullable
    View background;

    @BindView(R.id.image_container)
    @Nullable
    View imageContainer;

    @BindView(R.id.content)
    View contentContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    private Spanned biography;
    private Artist artist;
    private LastFMRestClient lastFMRestClient;
    private ArtistDetailsPresenter artistDetailsPresenter;
    private SimpleSongAdapter songAdapter;
    private AlbumAdapter albumAdapter;
    private boolean forceDownload;

    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_artist_details);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        setDrawUnderStatusBar(true);
        super.onCreate(bundle);
        ButterKnife.bind(this);

        setBottomBarVisibility(View.GONE);

        setNavigationbarColorAuto();
        setLightNavigationBar(true);

        supportPostponeEnterTransition();


        lastFMRestClient = new LastFMRestClient(this);

        setUpViews();

        artistDetailsPresenter = new ArtistDetailsPresenter(this, getIntent().getExtras());
        artistDetailsPresenter.subscribe();
    }

    private void setUpViews() {
        setupRecyclerView();
        setupToolbar();
        setupContainerHeight();
    }

    private void setupContainerHeight() {
        if (imageContainer != null) {
            LayoutParams params = imageContainer.getLayoutParams();
            params.width = DensityUtil.getScreenHeight(this) / 2;
            imageContainer.setLayoutParams(params);
        }
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);

        if (toolbar != null && !PreferenceUtil.getInstance(this).getFullScreenMode()) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar
                    .getLayoutParams();
            params.topMargin = RetroUtil.getStatusBarHeight(this);
            toolbar.setLayoutParams(params);
        }

        int primaryColor = ThemeStore.primaryColor(this);
        TintHelper.setTintAuto(contentContainer, primaryColor, true);

        if (appBarLayout != null) {
            appBarLayout.setBackgroundColor(primaryColor);
            appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onStateChanged(AppBarLayout appBarLayout,
                                           AppBarStateChangeListener.State state) {
                    int color;
                    switch (state) {
                        case COLLAPSED:
                            setLightStatusbar(!ATHUtil.isWindowBackgroundDark(ArtistDetailActivity.this));
                            color = ATHUtil.resolveColor(ArtistDetailActivity.this, R.attr.iconColor);
                            break;
                        default:
                        case EXPANDED:
                        case IDLE:
                            setLightStatusbar(false);
                            color = ContextCompat.getColor(ArtistDetailActivity.this, R.color.md_white_1000);
                            break;
                    }
                    ToolbarContentTintHelper.colorizeToolbar(toolbar, color, ArtistDetailActivity.this);
                }
            });
        }
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setContentScrimColor(primaryColor);
            collapsingToolbarLayout.setStatusBarScrimColor(ColorUtil.darkenColor(primaryColor));
        }
    }

    private void setupRecyclerView() {
        albumAdapter = new HorizontalAlbumAdapter(this, new ArrayList<>(), false, null);
        albumRecyclerView.setItemAnimator(new DefaultItemAnimator());
        albumRecyclerView
                .setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        albumRecyclerView.setAdapter(albumAdapter);

        songAdapter = new SimpleSongAdapter(this, new ArrayList<>(), R.layout.item_song);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    CustomArtistImageUtil.getInstance(this).setCustomArtistImage(artist, data.getData());
                }
                break;
            default:
                if (resultCode == RESULT_OK) {
                    reload();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        artistDetailsPresenter.unsubscribe();
    }

    @Override
    public void loading() {
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void completed() {
        supportStartPostponedEnterTransition();
    }

    @Override
    public void showData(Artist artist) {
        setArtist(artist);
    }

    private Artist getArtist() {
        if (artist == null) {
            artist = new Artist();
        }
        return artist;
    }

    private void setArtist(Artist artist) {
        if (artist.getSongCount() <= 0) {
            finish();
        }
        this.artist = artist;
        loadArtistImage();

        if (RetroUtil.isAllowedToDownloadMetadata(this)) {
            loadBiography();
        }
        title.setText(artist.getName());
        text.setText(String.format("%s • %s", MusicUtil.getArtistInfoString(this, artist), MusicUtil
                .getReadableDurationString(MusicUtil.getTotalDuration(this, artist.getSongs()))));

        songAdapter.swapDataSet(artist.getSongs());
        albumAdapter.swapDataSet(artist.albums);
    }

    private void loadBiography() {
        loadBiography(Locale.getDefault().getLanguage());
    }

    private void loadBiography(@Nullable final String lang) {
        biography = null;

        lastFMRestClient.getApiService()
                .getArtistInfo(getArtist().getName(), lang, null)
                .enqueue(new Callback<LastFmArtist>() {
                    @Override
                    public void onResponse(@NonNull Call<LastFmArtist> call,
                                           @NonNull Response<LastFmArtist> response) {
                        final LastFmArtist lastFmArtist = response.body();
                        if (lastFmArtist != null && lastFmArtist.getArtist() != null) {
                            final String bioContent = lastFmArtist.getArtist().getBio().getContent();
                            if (bioContent != null && !bioContent.trim().isEmpty()) {
                                //TransitionManager.beginDelayedTransition(titleContainer);
                                biographyTextView.setVisibility(View.VISIBLE);
                                biographyTitle.setVisibility(View.VISIBLE);
                                biography = Html.fromHtml(bioContent);
                                biographyTextView.setText(biography);
                            }
                        }

                        // If the "lang" parameter is set and no biography is given, retry with default language
                        if (biography == null && lang != null) {
                            loadBiography(null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LastFmArtist> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        biography = null;
                    }
                });
    }

    @OnClick(R.id.biography)
    void toggleArtistBiography() {
        if (biographyTextView.getMaxLines() == 4) {
            biographyTextView.setMaxLines(Integer.MAX_VALUE);
        } else {
            biographyTextView.setMaxLines(4);
        }
    }

    private void loadArtistImage() {
        ArtistGlideRequest.Builder.from(Glide.with(this), artist)
                .forceDownload(forceDownload)
                .generatePalette(this).build()
                .dontAnimate()
                .into(new RetroMusicColoredTarget(image) {
                    @Override
                    public void onColorReady(int color) {
                        setColors(color);
                    }
                });
        forceDownload = false;
    }

    private void setColors(int color) {

        int textColor =
                PreferenceUtil.getInstance(this).getAdaptiveColor() ? color : ThemeStore.accentColor(this);

        albumTitle.setTextColor(textColor);
        songTitle.setTextColor(textColor);
        biographyTitle.setTextColor(textColor);

        TintHelper.setTintAuto(shuffleButton, textColor, true);

        if (background != null) {
            background.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        findViewById(R.id.root).setBackgroundColor(ThemeStore.primaryColor(this));
    }

    @OnClick({R.id.action_shuffle_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_shuffle_all:
                MusicPlayerRemote.openAndShuffleQueue(getArtist().getSongs(), true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleSortOrderMenuItem(item);
    }

    private boolean handleSortOrderMenuItem(@NonNull MenuItem item) {
        final ArrayList<Song> songs = getArtist().getSongs();
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_play_next:
                MusicPlayerRemote.playNext(songs);
                return true;
            case R.id.action_add_to_current_playing:
                MusicPlayerRemote.enqueue(songs);
                return true;
            case R.id.action_add_to_playlist:
                AddToPlaylistDialog.create(songs).show(getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            case R.id.action_set_artist_image:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, getString(R.string.pick_from_local_storage)),
                        REQUEST_CODE_SELECT_IMAGE);
                return true;
            case R.id.action_reset_artist_image:
                Toast.makeText(ArtistDetailActivity.this, getResources().getString(R.string.updating),
                        Toast.LENGTH_SHORT).show();
                CustomArtistImageUtil.getInstance(ArtistDetailActivity.this).resetCustomArtistImage(artist);
                forceDownload = true;
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artist_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMediaStoreChanged() {
        super.onMediaStoreChanged();
        reload();
    }

    private void reload() {
        artistDetailsPresenter.unsubscribe();
        artistDetailsPresenter.subscribe();
    }
}
