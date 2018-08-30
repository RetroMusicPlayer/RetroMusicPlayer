package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.mvp.contract.PlaylistContract;
import code.name.monkey.retromusic.mvp.presenter.PlaylistPresenter;
import code.name.monkey.retromusic.ui.adapter.playlist.PlaylistAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;


public class PlaylistsFragment extends AbsLibraryPagerRecyclerViewFragment<PlaylistAdapter, LinearLayoutManager> implements
        PlaylistContract.PlaylistView {

    private PlaylistPresenter presenter;

    public static PlaylistsFragment newInstance() {
        Bundle args = new Bundle();
        PlaylistsFragment fragment = new PlaylistsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new PlaylistPresenter(this);
    }

    @Override
    protected LinearLayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @NonNull
    @Override
    protected PlaylistAdapter createAdapter() {
        return new PlaylistAdapter(getLibraryFragment().getMainActivity(), new ArrayList<>(),
                R.layout.item_list, getLibraryFragment());
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            getLibraryFragment().setTitle(PreferenceUtil.getInstance(getContext()).tabTitles() ? R.string.library : R.string.playlists);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLibraryFragment().setTitle(PreferenceUtil.getInstance(getContext()).tabTitles() ? R.string.library : R.string.playlists);
        if (getAdapter().getDataSet().isEmpty()) {
            presenter.subscribe();
        }
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onMediaStoreChanged() {
        super.onMediaStoreChanged();
        presenter.loadPlaylists();
    }

    @Override
    public void loading() {

    }

    @Override
    public void showEmptyView() {
        getAdapter().swapDataSet(new ArrayList<>());
    }

    @Override
    public void completed() {

    }

    @Override
    public void showData(ArrayList<Playlist> playlists) {
        getAdapter().swapDataSet(playlists);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.action_shuffle_all);
        menu.removeItem(R.id.action_sort_order);
        menu.removeItem(R.id.action_grid_size);
    }

    @Override
    protected int getEmptyMessage() {
        return R.string.no_playlists;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
