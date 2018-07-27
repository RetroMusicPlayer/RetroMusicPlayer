package code.name.monkey.retromusic.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

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
import code.name.monkey.retromusic.dialogs.DeleteSongsDialog;
import code.name.monkey.retromusic.glide.ArtistGlideRequest;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
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
    FloatingActionButton shuffleButton;
    @BindView(R.id.collapsing_toolbar)
    @Nullable
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    @Nullable
    AppBarLayout appBarLayout;
    @BindView(R.id.image_container)
    @Nullable
    View imageContainer;
    @BindView(R.id.content)
    View contentContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.more_recycler_view)
    RecyclerView moreRecyclerView;
    @BindView(R.id.more_title)
    TextView moreTitle;
    @BindView(R.id.artist_image)
    ImageView artistImage;

    private AlbumDetailsPresenter albumDetailsPresenter;
    private Album album;
    private SimpleSongAdapter adapter;

    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_album);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusbar(true);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        supportPostponeEnterTransition();
        setupToolbarMarginHeight();
        setBottomBarVisibility(View.GONE);
        setLightNavigationBar(true);
        setNavigationbarColorAuto();

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

        if (toolbar != null && !PreferenceUtil.getInstance(this).getFullScreenMode()) {
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
                            setLightStatusbar(!ATHUtil.isWindowBackgroundDark(AlbumDetailsActivity.this));
                            color = ATHUtil.resolveColor(AlbumDetailsActivity.this, R.attr.iconColor);
                            break;
                        default:
                        case EXPANDED:
                        case IDLE:
                            setLightStatusbar(false);
                            color = ContextCompat.getColor(AlbumDetailsActivity.this, R.color.md_white_1000);
                            break;
                    }
                    ToolbarContentTintHelper.colorizeToolbar(toolbar, color, AlbumDetailsActivity.this);
                }
            });
        }

    }

    @OnClick({R.id.action_shuffle_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_close:
                onBackPressed();
                break;
            case R.id.menu:
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.menu_album_detail);
                MenuItem sortOrder = popupMenu.getMenu().findItem(R.id.action_sort_order);
                setUpSortOrderMenu(sortOrder.getSubMenu());
                popupMenu.setOnMenuItemClickListener(this::onOptionsItemSelected);
                popupMenu.show();
                break;
            case R.id.action_shuffle_all:
                MusicPlayerRemote.openAndShuffleQueue(album.songs, true);
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
        supportStartPostponedEnterTransition();
    }

    @Override
    public void showData(Album album) {
        if (album.songs.isEmpty()) {
            finish();
            return;
        }
        this.album = album;

        title.setText(album.getTitle());
        text.setText(String.format("%s%s • %s", album.getArtistName(), " • " + MusicUtil.getYearString(album.getYear()),
                MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(this, album.songs))));

        loadAlbumCover();

        adapter = new SimpleSongAdapter(this, this.album.songs, R.layout.item_song);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        loadMoreFrom(album);

    }

    private void loadMoreFrom(Album album) {

        ArtistGlideRequest.Builder.from(Glide.with(this),
                ArtistLoader.getArtist(this, album.getArtistId()).blockingFirst())
                .forceDownload(true)
                .generatePalette(this).build()
                .dontAnimate()
                .into(new RetroMusicColoredTarget(artistImage) {
                    @Override
                    public void onColorReady(int color) {
                        setColors(color);
                    }
                });


        ArrayList<Album> albums = ArtistLoader.getArtist(this, album.getArtistId()).blockingFirst().albums;
        albums.remove(album);
        if (!albums.isEmpty()) {
            moreTitle.setVisibility(View.VISIBLE);
            moreRecyclerView.setVisibility(View.VISIBLE);
        } else {
            return;
        }
        moreTitle.setText(String.format("More from %s", album.getArtistName()));

        AlbumAdapter albumAdapter = new HorizontalAlbumAdapter(this, albums,
                false, null);
        moreRecyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        moreRecyclerView.setAdapter(albumAdapter);
    }

    public Album getAlbum() {
        return album;
    }

    private void loadAlbumCover() {
        SongGlideRequest.Builder.from(Glide.with(this), getAlbum().safeGetFirstSong())
                .checkIgnoreMediaStore(this)
                .generatePalette(this).build()
                .dontAnimate()
                .listener(new RequestListener<Object, BitmapPaletteWrapper>() {
                    @Override
                    public boolean onException(Exception e, Object model, Target<BitmapPaletteWrapper> target,
                                               boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(BitmapPaletteWrapper resource, Object model,
                                                   Target<BitmapPaletteWrapper> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(new RetroMusicColoredTarget(image) {
                    @Override
                    public void onColorReady(int color) {
                        setColors(color);
                    }
                });
    }

    private void setColors(int color) {
        int themeColor = PreferenceUtil.getInstance(this).getAdaptiveColor() ? color : ThemeStore.accentColor(this);
        songTitle.setTextColor(themeColor);
        moreTitle.setTextColor(themeColor);

        TintHelper.setTintAuto(shuffleButton, themeColor, true);
        findViewById(R.id.root).setBackgroundColor(ThemeStore.primaryColor(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_detail, menu);
        MenuItem sortOrder = menu.findItem(R.id.action_sort_order);
        setUpSortOrderMenu(sortOrder.getSubMenu());
        return true;
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
        return PreferenceUtil.getInstance(this).getAlbumDetailSongSortOrder();
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
        PreferenceUtil.getInstance(this).setAlbumDetailSongSortOrder(sortOrder);
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