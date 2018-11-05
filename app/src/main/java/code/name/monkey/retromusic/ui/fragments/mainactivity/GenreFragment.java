package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Genre;
import code.name.monkey.retromusic.mvp.contract.GenreContract;
import code.name.monkey.retromusic.mvp.presenter.GenrePresenter;
import code.name.monkey.retromusic.ui.adapter.GenreAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class GenreFragment extends
        AbsLibraryPagerRecyclerViewFragment<GenreAdapter, LinearLayoutManager> implements
        GenreContract.GenreView {

    private GenrePresenter mPresenter;

    public static GenreFragment newInstance() {
        Bundle args = new Bundle();
        GenreFragment fragment = new GenreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new GenrePresenter(this);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            getLibraryFragment().setTitle(PreferenceUtil.getInstance().tabTitles() ? R.string.library : R.string.genres);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLibraryFragment().setTitle(PreferenceUtil.getInstance().tabTitles() ? R.string.library : R.string.genres);
        if (getAdapter().getDataSet().isEmpty()) {
            mPresenter.subscribe();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @NonNull
    @Override
    protected LinearLayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @NonNull
    @Override
    protected GenreAdapter createAdapter() {
        ArrayList<Genre> dataSet = getAdapter() == null ? new ArrayList<Genre>() : getAdapter().getDataSet();
        return new GenreAdapter(getLibraryFragment().getMainActivity(), dataSet, R.layout.item_list);
    }

    @Override
    public void loading() {

    }

    @Override
    public void showData(ArrayList<Genre> songs) {
        getAdapter().swapDataSet(songs);
    }

    @Override
    public void showEmptyView() {
        getAdapter().swapDataSet(new ArrayList<Genre>());
    }

    @Override
    public void completed() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.action_sort_order);
        menu.removeItem(R.id.action_grid_size);
        menu.removeItem(R.id.action_new_playlist);
    }

    @Override
    protected int getEmptyMessage() {
        return R.string.no_genres;
    }
}
