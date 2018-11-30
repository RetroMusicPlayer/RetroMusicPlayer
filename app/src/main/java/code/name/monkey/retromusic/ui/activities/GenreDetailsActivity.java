package code.name.monkey.retromusic.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialcab.MaterialCab;
import com.google.android.material.appbar.AppBarLayout;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.menu.GenreMenuHelper;
import code.name.monkey.retromusic.interfaces.CabHolder;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.contract.GenreDetailsContract;
import code.name.monkey.retromusic.mvp.presenter.GenreDetailsPresenter;
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity;
import code.name.monkey.retromusic.ui.adapter.song.SongAdapter;
import code.name.monkey.retromusic.util.RetroColorUtil;
import code.name.monkey.retromusic.util.ViewUtil;
import code.name.monkey.retromusic.views.CollapsingFAB;

/**
 * @author Hemanth S (h4h13).
 */

public class GenreDetailsActivity extends AbsSlidingMusicPanelActivity implements GenreDetailsContract.GenreDetailsView, CabHolder {
    public static final String EXTRA_GENRE_ID = "extra_genre_id";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(android.R.id.empty)
    TextView empty;

    @BindView(R.id.action_shuffle)
    CollapsingFAB shuffleButton;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.title)
    TextView title;

    private Genre genre;
    private GenreDetailsPresenter presenter;
    private SongAdapter songAdapter;
    private MaterialCab cab;

    private void checkIsEmpty() {
        empty.setVisibility(
                songAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusBar();
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        toggleBottomNavigationView(true);
        setLightNavigationBar(true);


        genre = getIntent().getParcelableExtra(EXTRA_GENRE_ID);
        presenter = new GenreDetailsPresenter(this, genre.getId());

        setUpToolBar();
        setupRecyclerView();
    }

    @OnClick({R.id.action_shuffle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_shuffle:
                MusicPlayerRemote.INSTANCE.openAndShuffleQueue(songAdapter.getDataSet(), true);
                break;
        }
    }

    private void setUpToolBar() {
        title.setText(genre.getName());
        title.setTextColor(ThemeStore.textColorPrimary(this));

        int primaryColor = ThemeStore.primaryColor(this);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setBackgroundColor(primaryColor);
        appBarLayout.setBackgroundColor(primaryColor);
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this));
        setTitle(null);
        setSupportActionBar(toolbar);
        shuffleButton.setColor(ThemeStore.accentColor(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    protected View createContentView() {
        return wrapSlidingMusicPanel(R.layout.activity_playlist_detail);
    }


    @Override
    public void loading() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void completed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_genre_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return GenreMenuHelper.INSTANCE.handleMenuClick(this, genre, item);
    }

    private void setupRecyclerView() {
        ViewUtil.setUpFastScrollRecyclerViewColor(this,
                ((FastScrollRecyclerView) recyclerView), ThemeStore.accentColor(this));
        songAdapter = new SongAdapter(this, new ArrayList<>(), R.layout.item_list, false, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    shuffleButton.setShowTitle(false);
                } else if (dy < 0) {
                    shuffleButton.setShowTitle(true);
                }
            }
        });
        songAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
            }
        });
    }

    @Override
    public void showData(ArrayList<Song> songs) {
        songAdapter.swapDataSet(songs);
    }

    @NonNull
    @Override
    public MaterialCab openCab(final int menu, final MaterialCab.Callback callback) {
        if (cab != null && cab.isActive()) cab.finish();
        cab = new MaterialCab(this, R.id.cab_stub)
                .setMenu(menu)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(RetroColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor(this)))
                .start(callback);
        return cab;
    }

    @Override
    public void onBackPressed() {
        if (cab != null && cab.isActive()) cab.finish();
        else {
            recyclerView.stopScroll();
            super.onBackPressed();
        }
    }

    @Override
    public void onMediaStoreChanged() {
        super.onMediaStoreChanged();
        presenter.subscribe();
    }
}
