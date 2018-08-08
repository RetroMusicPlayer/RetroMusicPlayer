package code.name.monkey.retromusic.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.afollestad.materialcab.MaterialCab;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.menu.PlaylistMenuHelper;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.loaders.PlaylistLoader;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.PlaylistSong;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.contract.PlaylistSongsContract;
import code.name.monkey.retromusic.mvp.presenter.PlaylistSongsPresenter;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity;
import code.name.monkey.retromusic.ui.adapter.song.OrderablePlaylistSongAdapter;
import code.name.monkey.retromusic.ui.adapter.song.PlaylistSongAdapter;
import code.name.monkey.retromusic.ui.adapter.song.SongAdapter;
import code.name.monkey.retromusic.util.PlaylistsUtil;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import code.name.monkey.retromusic.util.ViewUtil;

public class PlaylistDetailActivity extends AbsSlidingMusicPanelActivity implements CabHolder,
        PlaylistSongsContract.PlaylistSongsView {

    @NonNull
    public static String EXTRA_PLAYLIST = "extra_playlist";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(android.R.id.empty)
    TextView empty;

    @BindView(R.id.action_shuffle)
    FloatingActionButton shuffleButton;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.status_bar)
    View statusBar;

    private Playlist playlist;
    private MaterialCab cab;
    private SongAdapter adapter;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private PlaylistSongsPresenter songsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusBar(true);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        RetroUtil.statusBarHeight(statusBar);
        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        setBottomBarVisibility(View.GONE);

        playlist = getIntent().getExtras().getParcelable(EXTRA_PLAYLIST);

        if (playlist != null) {
            songsPresenter = new PlaylistSongsPresenter(this, playlist);
        }

        setUpToolBar();
        setUpRecyclerView();
    }

    public void showHeartAnimation() {
        shuffleButton.clearAnimation();

        shuffleButton.setScaleX(0.9f);
        shuffleButton.setScaleY(0.9f);
        shuffleButton.setVisibility(View.VISIBLE);
        shuffleButton.setPivotX(shuffleButton.getWidth() / 2);
        shuffleButton.setPivotY(shuffleButton.getHeight() / 2);

        shuffleButton.animate()
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(() -> shuffleButton.animate()
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .start())
                .start();
    }


    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_playlist_detail);
    }

    private void setUpRecyclerView() {
        ViewUtil.setUpFastScrollRecyclerViewColor(this, ((FastScrollRecyclerView) recyclerView),
                ThemeStore.accentColor(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (playlist instanceof AbsCustomPlaylist) {
            adapter = new PlaylistSongAdapter(this, new ArrayList<Song>(), R.layout.item_list, false,
                    this);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerViewDragDropManager = new RecyclerViewDragDropManager();
            final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
            adapter = new OrderablePlaylistSongAdapter(this, new ArrayList<PlaylistSong>(),
                    R.layout.item_list, false, this, (fromPosition, toPosition) -> {
                if (PlaylistsUtil
                        .moveItem(PlaylistDetailActivity.this, playlist.id, fromPosition, toPosition)) {
                    Song song = adapter.getDataSet().remove(fromPosition);
                    adapter.getDataSet().add(toPosition, song);
                    adapter.notifyItemMoved(fromPosition, toPosition);
                }
            });
            wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(adapter);

            recyclerView.setAdapter(wrappedAdapter);
            recyclerView.setItemAnimator(animator);

            recyclerViewDragDropManager.attachRecyclerView(recyclerView);
        }
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        songsPresenter.subscribe();
    }

    private void setUpToolBar() {
        title.setTextColor(ThemeStore.textColorPrimary(this));
        TintHelper.setTintAuto(shuffleButton, ThemeStore.accentColor(this), true);

        int primaryColor = ThemeStore.primaryColor(this);
        toolbar.setBackgroundColor(primaryColor);
        appBarLayout.setBackgroundColor(primaryColor);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        title.setText(playlist.name);

        setTitle(null);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                playlist instanceof AbsCustomPlaylist ? R.menu.menu_smart_playlist_detail
                        : R.menu.menu_playlist_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return PlaylistMenuHelper.handleMenuClick(this, playlist, item);
    }

    @NonNull
    @Override
    public MaterialCab openCab(final int menu, final MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) {
            cab.finish();
        }
        cab = new MaterialCab(this, R.id.cab_stub)
                .setMenu(menu)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(
                        RetroColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor(this)))
                .start(callback);
        return cab;
    }

    @Override
    public void onBackPressed() {
        if (cab != null && cab.isActive()) {
            cab.finish();
        } else {
            recyclerView.stopScroll();
            super.onBackPressed();
        }
    }

    @Override
    public void onMediaStoreChanged() {
        super.onMediaStoreChanged();

        if (!(playlist instanceof AbsCustomPlaylist)) {
            // Playlist deleted
            if (!PlaylistsUtil.doesPlaylistExist(this, playlist.id)) {
                finish();
                return;
            }

            // Playlist renamed
            final String playlistName = PlaylistsUtil.getNameForPlaylist(this, playlist.id);
            if (!playlistName.equals(playlist.name)) {
                playlist = PlaylistLoader.getPlaylist(this, playlist.id).blockingFirst();
                setToolbarTitle(playlist.name);
            }
        }
        songsPresenter.subscribe();
    }

    private void setToolbarTitle(String title) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(title);
    }

    private void checkIsEmpty() {
        empty.setVisibility(
                adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE
        );
    }

    @Override
    public void onPause() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.cancelDrag();
        }
        super.onPause();
        songsPresenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.release();
            recyclerViewDragDropManager = null;
        }

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        adapter = null;

        super.onDestroy();
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        songsPresenter.subscribe();
    }

    @Override
    public void loading() {
    }

    @Override
    public void showEmptyView() {
        empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void completed() {
    }

    @Override
    public void showData(ArrayList<Song> songs) {
        adapter.swapDataSet(songs);
    }

    @OnClick(R.id.action_shuffle)
    public void onViewClicked() {
        showHeartAnimation();
        if (adapter.getDataSet().isEmpty()) {
            return;
        }
        MusicPlayerRemote.openAndShuffleQueue(adapter.getDataSet(), true);
    }
}
