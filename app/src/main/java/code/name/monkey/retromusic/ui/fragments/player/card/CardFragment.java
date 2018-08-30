package code.name.monkey.retromusic.ui.fragments.player.card;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;

public class CardFragment extends AbsPlayerFragment implements PlayerAlbumCoverFragment.Callbacks {

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.recycler_view)

    RecyclerView recyclerView;
    @Nullable
    @BindView(R.id.title)
    TextView title;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private PlayingQueueAdapter playingQueueAdapter;
    private LinearLayoutManager layoutManager;
    private int lastColor;
    private CardPlaybackControlsFragment playbackControlsFragment;
    private Unbinder unbinder;

    public static PlayerFragment newInstance() {
        Bundle args = new Bundle();
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @ColorInt
    public int getPaletteColor() {
        return lastColor;
    }

    @Override
    public void onShow() {
        playbackControlsFragment.show();
    }

    @Override
    public void onHide() {
        playbackControlsFragment.hide();
        onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public int toolbarIconColor() {
        return Color.WHITE;
    }

    @Override
    public void onColorChanged(int color) {
        playbackControlsFragment.setDark(color);
        lastColor = color;
        getCallbacks().onPaletteColorChanged();

        ToolbarContentTintHelper.colorizeToolbar(toolbar, Color.WHITE, getActivity());

        if (title != null && playingQueueAdapter != null) {
            if (ColorUtil.isColorLight(color)) {
                title.setTextColor(Color.BLACK);
                playingQueueAdapter.usePalette(false);
            } else {
                title.setTextColor(Color.WHITE);
                playingQueueAdapter.usePalette(true);
            }
        }
    }

    @Override
    protected void toggleFavorite(Song song) {
        super.toggleFavorite(song);
        if (song.id == MusicPlayerRemote.getCurrentSong().id) {
            updateIsFavorite();
        }
    }

    @Override
    public void onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.getCurrentSong());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        playingQueueAdapter = null;
        layoutManager = null;
        super.onDestroyView();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSubFragments();
        setUpPlayerToolbar();
        setUpRecyclerView();
    }

    private void setUpSubFragments() {
        playbackControlsFragment =
                (CardPlaybackControlsFragment) getChildFragmentManager()
                        .findFragmentById(R.id.playback_controls_fragment);

        PlayerAlbumCoverFragment playerAlbumCoverFragment = (PlayerAlbumCoverFragment) getChildFragmentManager()
                .findFragmentById(R.id.player_album_cover_fragment);
        playerAlbumCoverFragment.setCallbacks(this);
        playerAlbumCoverFragment.removeSlideEffect();
    }

    private void setUpPlayerToolbar() {
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);

        ToolbarContentTintHelper.colorizeToolbar(toolbar, Color.WHITE, getActivity());

    }

    @Override
    public void onServiceConnected() {
        updateIsFavorite();
        //updateLyrics();
        setUpRecyclerView();
    }

    @Override
    public void onPlayingMetaChanged() {
        updateIsFavorite();
        //updateLyrics();
        updateQueuePosition();
    }

    private void setUpRecyclerView() {
        if (recyclerView != null) {
            recyclerViewDragDropManager = new RecyclerViewDragDropManager();
            final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

            playingQueueAdapter = new PlayingQueueAdapter(
                    (AppCompatActivity) getActivity(),
                    MusicPlayerRemote.getPlayingQueue(),
                    MusicPlayerRemote.getPosition(),
                    R.layout.item_song,
                    false,
                    null);
            wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(playingQueueAdapter);

            layoutManager = new LinearLayoutManager(getContext());

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(wrappedAdapter);
            recyclerView.setItemAnimator(animator);
            recyclerViewDragDropManager.attachRecyclerView(recyclerView);
            layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.getPosition() + 1, 0);
        }
    }

    @Override
    public void onQueueChanged() {
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

    private void updateQueuePosition() {
        if (playingQueueAdapter != null) {
            playingQueueAdapter.setCurrent(MusicPlayerRemote.getPosition());
        }
        // if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
        resetToCurrentPosition();
        //}
    }

    private void updateQueue() {
        if (playingQueueAdapter != null) {
            playingQueueAdapter.swapDataSet(MusicPlayerRemote.getPlayingQueue(),
                    MusicPlayerRemote.getPosition());
            resetToCurrentPosition();
        }
    }

    private void resetToCurrentPosition() {
        if (recyclerView != null) {
            recyclerView.stopScroll();
            layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.getPosition() + 1, 0);
        }

    }

    @Override
    public void onPause() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.cancelDrag();
        }

        super.onPause();
    }


}
