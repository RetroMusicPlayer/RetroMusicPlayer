package code.name.monkey.retromusic.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialUtil;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog;
import code.name.monkey.retromusic.dialogs.DeleteSongsDialog;
import code.name.monkey.retromusic.glide.ArtistGlideRequest;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder;
import code.name.monkey.retromusic.loaders.ArtistLoader;
import code.name.monkey.retromusic.misc.AppBarStateChangeListener;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract;
import code.name.monkey.retromusic.mvp.presenter.AlbumDetailsPresenter;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity;
import code.name.monkey.retromusic.ui.activities.tageditor.AbsTagEditorActivity;
import code.name.monkey.retromusic.ui.activities.tageditor.AlbumTagEditorActivity;
import code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter;
import code.name.monkey.retromusic.ui.adapter.album.HorizontalAlbumAdapter;
import code.name.monkey.retromusic.ui.adapter.song.SimpleSongAdapter;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;

public class AlbumDetailsActivity extends AbsSlidingMusicPanelActivity implements
        AlbumDetailsContract.AlbumDetailsView {

    public static final String EXTRA_ALBUM_ID = "extra_album_id";
    private static final int TAG_EDITOR_REQUEST = 2001;
    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.song_title)
    AppCompatTextView songTitle;

    @BindView(R.id.action_shuffle_all)
    MaterialButton shuffleButton;

    @BindView(R.id.collapsing_toolbar)
    @Nullable
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar)
    @Nullable
    AppBarLayout appBarLayout;

    @BindView(R.id.content)
    View contentContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.more_recycler_view)
    RecyclerView moreRecyclerView;

    @BindView(R.id.more_title)
    TextView moreTitle;

    @BindView(R.id.artist_image)
    @Nullable
    ImageView artistImage;

    private AlbumDetailsPresenter albumDetailsPresenter;

    private SimpleSongAdapter adapter;
    private Album album;

    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_album);
    }

    void setupWindowTransition() {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(
                AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));
        getWindow().setEnterTransition(slide);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusBar();
        setupWindowTransition();
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        toggleBottomNavigationView(true);
        setLightNavigationBar(true);
        setNavigationbarColorAuto();

        ActivityCompat.postponeEnterTransition(this);

        adapter = new SimpleSongAdapter(this, new ArrayList<>(), R.layout.item_song);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        setupToolbarMarginHeight();

        int albumId = getIntent().getIntExtra(EXTRA_ALBUM_ID, -1);
        albumDetailsPresenter = new AlbumDetailsPresenter(this, albumId);
    }

    private void setupToolbarMarginHeight() {
        int primaryColor = ThemeStore.primaryColor(this);
        TintHelper.setTintAuto(contentContainer, primaryColor, true);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setContentScrimColor(primaryColor);
            collapsingToolbarLayout.setStatusBarScrimColor(ColorUtil.darkenColor(primaryColor));
        }

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(null);


        if (toolbar != null && !PreferenceUtil.getInstance().getFullScreenMode()) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            params.topMargin = RetroUtil.getStatusBarHeight(this);
            toolbar.setLayoutParams(params);
        }

        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onStateChanged(AppBarLayout appBarLayout, State state) {
                    int color;
                    switch (state) {
                        case COLLAPSED:
                            setLightStatusbar(ColorUtil.isColorLight(ThemeStore.primaryColor(AlbumDetailsActivity.this)));
                            color = ThemeStore.primaryColor(AlbumDetailsActivity.this);
                            break;
                        default:
                        case EXPANDED:
                        case IDLE:
                            setLightStatusbar(false);
                            color = Color.TRANSPARENT;
                            break;
                    }
                    ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(AlbumDetailsActivity.this, toolbar, color);
                }
            });
        }
    }

    @OnClick({R.id.action_shuffle_all, R.id.artist_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.artist_image:
                Pair[] artistPairs = new Pair[]{Pair.create(image,
                        getResources().getString(R.string.transition_artist_image))};
                NavigationUtil.goToArtist(this, getAlbum().getArtistId(),
                        artistPairs);
                break;
            case R.id.action_shuffle_all:
                if (getAlbum().songs != null) {
                    MusicPlayerRemote.openAndShuffleQueue(getAlbum().songs, true);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        albumDetailsPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        albumDetailsPresenter.unsubscribe();
    }

    @Override
    public void loading() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void completed() {
        ActivityCompat.startPostponedEnterTransition(this);
    }

    @Override
    public void showData(Album album) {
        if (album.songs.isEmpty()) {
            finish();
            return;
        }
        this.album = album;

        title.setText(album.getTitle());
        text.setText(String.format("%s%s • %s", album.getArtistName(),
                " • " + MusicUtil.getYearString(album.getYear()),
                MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(this, album.songs))));

        loadAlbumCover();
        loadMoreFrom(album);
        adapter.swapDataSet(album.songs);
    }

    private void loadMoreFrom(Album album) {
        if (artistImage != null) {
            ArtistGlideRequest.Builder.from(Glide.with(this),
                    ArtistLoader.getArtist(this, album.getArtistId()).blockingFirst())
                    .forceDownload(false)
                    .generatePalette(this).build()
                    .dontAnimate()
                    .into(new RetroMusicColoredTarget(artistImage) {
                        @Override
                        public void onColorReady(int color) {
                            //setColors(color);
                        }
                    });
        }

        ArrayList<Album> albums = ArtistLoader.getArtist(this, album.getArtistId())
                .blockingFirst().albums;
        albums.remove(album);
        if (!albums.isEmpty()) {
            moreTitle.setVisibility(View.VISIBLE);
            moreRecyclerView.setVisibility(View.VISIBLE);
        } else {
            return;
        }
        moreTitle.setText(String.format("More from %s", album.getArtistName()));

        AlbumAdapter albumAdapter = new HorizontalAlbumAdapter(this, albums, false, null);
        moreRecyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        moreRecyclerView.setAdapter(albumAdapter);

        ActivityCompat.startPostponedEnterTransition(this);
    }

    public Album getAlbum() {
        return album;
    }

    private void loadAlbumCover() {
        SongGlideRequest.Builder.from(Glide.with(this), getAlbum().safeGetFirstSong())
                .checkIgnoreMediaStore(this)
                .generatePalette(this).build()
                .dontAnimate()
                .into(new RetroMusicColoredTarget(image) {
                    @Override
                    public void onColorReady(int color) {
                        setColors(color);
                    }
                });
    }

    private void setColors(int color) {
        int themeColor =
                PreferenceUtil.getInstance().getAdaptiveColor() ? color : ThemeStore.accentColor(this);
        songTitle.setTextColor(themeColor);
        moreTitle.setTextColor(themeColor);

        MaterialUtil.setTint(shuffleButton, true, themeColor);
        //findViewById(R.id.root).setBackgroundColor(ThemeStore.primaryColor(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_detail, menu);
        MenuItem sortOrder = menu.findItem(R.id.action_sort_order);
        setUpSortOrderMenu(sortOrder.getSubMenu());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleSortOrderMenuItem(item);
    }

    private boolean handleSortOrderMenuItem(@NonNull MenuItem item) {
        String sortOrder = null;
        final ArrayList<Song> songs = adapter.getDataSet();
        switch (item.getItemId()) {
            case R.id.action_play_next:
                MusicPlayerRemote.playNext(songs);
                return true;
            case R.id.action_add_to_current_playing:
                MusicPlayerRemote.enqueue(songs);
                return true;
            case R.id.action_add_to_playlist:
                AddToPlaylistDialog.create(songs).show(getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            case R.id.action_delete_from_device:
                DeleteSongsDialog.create(songs).show(getSupportFragmentManager(), "DELETE_SONGS");
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_tag_editor:
                Intent intent = new Intent(this, AlbumTagEditorActivity.class);
                intent.putExtra(AbsTagEditorActivity.EXTRA_ID, getAlbum().getId());
                startActivityForResult(intent, TAG_EDITOR_REQUEST);
                return true;
            case R.id.action_go_to_artist:
                NavigationUtil.goToArtist(this, getAlbum().getArtistId());
                return true;
            /*Sort*/
            case R.id.action_sort_order_title:
                sortOrder = AlbumSongSortOrder.SONG_A_Z;
                break;
            case R.id.action_sort_order_title_desc:
                sortOrder = AlbumSongSortOrder.SONG_Z_A;
                break;
            case R.id.action_sort_order_track_list:
                sortOrder = AlbumSongSortOrder.SONG_TRACK_LIST;
                break;
            case R.id.action_sort_order_artist_song_duration:
                sortOrder = AlbumSongSortOrder.SONG_DURATION;
                break;
        }
        if (sortOrder != null) {
            item.setChecked(true);
            setSaveSortOrder(sortOrder);
        }
        return true;
    }

    private String getSavedSortOrder() {
        return PreferenceUtil.getInstance().getAlbumDetailSongSortOrder();
    }

    private void setUpSortOrderMenu(@NonNull SubMenu sortOrder) {
        switch (getSavedSortOrder()) {
            case AlbumSongSortOrder.SONG_A_Z:
                sortOrder.findItem(R.id.action_sort_order_title).setChecked(true);
                break;
            case AlbumSongSortOrder.SONG_Z_A:
                sortOrder.findItem(R.id.action_sort_order_title_desc).setChecked(true);
                break;
            case AlbumSongSortOrder.SONG_TRACK_LIST:
                sortOrder.findItem(R.id.action_sort_order_track_list).setChecked(true);
                break;
            case AlbumSongSortOrder.SONG_DURATION:
                sortOrder.findItem(R.id.action_sort_order_artist_song_duration).setChecked(true);
                break;
        }
    }

    private void setSaveSortOrder(String sortOrder) {
        PreferenceUtil.getInstance().setAlbumDetailSongSortOrder(sortOrder);
        reload();
    }

    @Override
    public void onMediaStoreChanged() {
        super.onMediaStoreChanged();
        reload();
    }

    private void reload() {
        albumDetailsPresenter.subscribe();
    }
}