package code.name.monkey.retromusic.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment;

public class PlayingQueueFragment extends AbsMusicServiceFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private PlayingQueueAdapter mPlayingQueueAdapter;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        mPlayingQueueAdapter = new PlayingQueueAdapter(
                (AppCompatActivity) getActivity(),
                MusicPlayerRemote.getPlayingQueue(),
                MusicPlayerRemote.getPosition(),
                R.layout.item_queue);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mPlayingQueueAdapter);

        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        mLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.getPosition() + 1, 0);
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

    @Override
    public void onPlayingMetaChanged() {
        //updateCurrentSong();
        //updateIsFavorite();
        updateQueuePosition();
        //updateLyrics();
    }

    private void updateQueuePosition() {
        mPlayingQueueAdapter.setCurrent(MusicPlayerRemote.getPosition());
        // if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
        resetToCurrentPosition();
        //}
    }

    private void updateQueue() {
        mPlayingQueueAdapter
                .swapDataSet(MusicPlayerRemote.getPlayingQueue(), MusicPlayerRemote.getPosition());
        resetToCurrentPosition();
    }

    private void resetToCurrentPosition() {
        mRecyclerView.stopScroll();
        mLayoutManager.scrollToPositionWithOffset(MusicPlayerRemote.getPosition() + 1, 0);
    }

    @Override
    public void onPause() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.cancelDrag();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
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
        super.onDestroyView();
        unbinder.unbind();
    }
}
