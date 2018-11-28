package code.name.monkey.retromusic.ui.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity;
import code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.views.CollapsingFAB;


public class PlayingQueueActivity extends AbsMusicServiceActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindDrawable(R.drawable.ic_keyboard_backspace_black_24dp)
    Drawable close;

    @BindView(R.id.player_queue_sub_header)
    TextView textView;

    @BindString(R.string.queue)
    String queue;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.clear_queue)
    CollapsingFAB clearQueue;

    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private PlayingQueueAdapter mPlayingQueueAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);
        ButterKnife.bind(this);

        setStatusbarColorAuto();
        setNavigationbarColorAuto();
        setTaskDescriptionColorAuto();
        setLightNavigationBar(true);

        setupToolbar();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        mPlayingQueueAdapter = new PlayingQueueAdapter(
                this,
                MusicPlayerRemote.getPlayingQueue(),
                MusicPlayerRemote.getPosition(),
                R.layout.item_queue);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mPlayingQueueAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        mLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.getPosition() + 1, 0);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    clearQueue.setShowTitle(false);
                } else if (dy < 0) {
                    clearQueue.setShowTitle(true);
                }
            }
        });
    }

    @Override
    public void onQueueChanged() {
        if (MusicPlayerRemote.getPlayingQueue().isEmpty()) {
            finish();
            return;
        }
        updateQueue();
        updateCurrentSong();
    }

    @Override
    public void onMediaStoreChanged() {
        updateQueue();
        updateCurrentSong();
    }

    @SuppressWarnings("ConstantConditions")
    private void updateCurrentSong() {
    }

    @Override
    public void onPlayingMetaChanged() {
        updateQueuePosition();
    }

    private void updateQueuePosition() {
        mPlayingQueueAdapter.setCurrent(MusicPlayerRemote.getPosition());
        resetToCurrentPosition();
    }

    private void updateQueue() {
        mPlayingQueueAdapter.swapDataSet(MusicPlayerRemote.getPlayingQueue(), MusicPlayerRemote.getPosition());
        resetToCurrentPosition();
    }

    private void resetToCurrentPosition() {
        mRecyclerView.stopScroll();
        mLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.getPosition() + 1, 0);
    }

    @Override
    protected void onPause() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.cancelDrag();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mPlayingQueueAdapter = null;
        mLayoutManager = null;
        super.onDestroy();
    }

    protected String getUpNextAndQueueTime() {
        return getResources().getString(R.string.up_next) + "  •  " + MusicUtil.getReadableDurationString(MusicPlayerRemote.getQueueDurationMillis(MusicPlayerRemote.getPosition()));
    }

    private void setupToolbar() {
        title.setTextColor(ThemeStore.textColorPrimary(this));
        textView.setText(getUpNextAndQueueTime());
        textView.setTextColor(ThemeStore.accentColor(this));

        appBarLayout.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setBackgroundColor(ThemeStore.primaryColor(this));
        toolbar.setNavigationIcon(close);
        setSupportActionBar(toolbar);
        setTitle(null);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ToolbarContentTintHelper.colorBackButton(toolbar, ThemeStore.accentColor(this));
        clearQueue.setColor(ThemeStore.accentColor(this));
    }

    @OnClick(R.id.clear_queue)
    void clearQueue() {
        MusicPlayerRemote.clearQueue();
    }
}
