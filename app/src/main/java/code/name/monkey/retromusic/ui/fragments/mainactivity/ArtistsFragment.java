package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Artist;
import code.name.monkey.retromusic.mvp.contract.ArtistContract;
import code.name.monkey.retromusic.mvp.presenter.ArtistPresenter;
import code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class ArtistsFragment extends
        AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager> implements
        ArtistContract.ArtistView {

    public static final String TAG = ArtistsFragment.class.getSimpleName();
    private ArtistPresenter presenter;

    public static ArtistsFragment newInstance() {

        Bundle args = new Bundle();

        ArtistsFragment fragment = new ArtistsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ArtistPresenter(this);
    }

    @NonNull
    @Override
    protected GridLayoutManager createLayoutManager() {
        return new GridLayoutManager(getActivity(), getGridSize());
    }

    @NonNull
    @Override
    protected ArtistAdapter createAdapter() {
        int itemLayoutRes = getItemLayoutRes();
        notifyLayoutResChanged(itemLayoutRes);
        if (itemLayoutRes != R.layout.item_list) {
            itemLayoutRes = PreferenceUtil.getInstance(getContext()).getArtistGridStyle(getContext());
        }
        ArrayList<Artist> dataSet =
                getAdapter() == null ? new ArrayList<>() : getAdapter().getDataSet();
        return new ArtistAdapter(getLibraryFragment().getMainActivity(), dataSet, itemLayoutRes,
                loadUsePalette(), getLibraryFragment());
    }

    @Override
    protected int getEmptyMessage() {
        return R.string.no_artists;
    }

    @Override
    public void onMediaStoreChanged() {
        presenter.loadArtists();
    }

    @Override
    protected int loadGridSize() {
        return PreferenceUtil.getInstance(getActivity()).getArtistGridSize(getActivity());
    }

    @Override
    protected void saveGridSize(int gridSize) {
        PreferenceUtil.getInstance(getActivity()).setArtistGridSize(gridSize);
    }

    @Override
    protected int loadGridSizeLand() {
        return PreferenceUtil.getInstance(getActivity()).getArtistGridSizeLand(getActivity());
    }

    @Override
    protected void saveGridSizeLand(int gridSize) {
        PreferenceUtil.getInstance(getActivity()).setArtistGridSizeLand(gridSize);
    }

    @Override
    protected void saveUsePalette(boolean usePalette) {
        PreferenceUtil.getInstance(getActivity()).setArtistColoredFooters(usePalette);
    }

    @Override
    public boolean loadUsePalette() {
        return PreferenceUtil.getInstance(getActivity()).artistColoredFooters();
    }

    @Override
    protected void setUsePalette(boolean usePalette) {
        getAdapter().usePalette(usePalette);
    }

    @Override
    protected void setGridSize(int gridSize) {
        getLayoutManager().setSpanCount(gridSize);
        getAdapter().notifyDataSetChanged();
    }


    @Override
    protected String loadSortOrder() {
        return PreferenceUtil.getInstance(getActivity()).getArtistSortOrder();
    }

    @Override
    protected void saveSortOrder(String sortOrder) {
        PreferenceUtil.getInstance(getActivity()).setArtistSortOrder(sortOrder);
    }

    @Override
    protected void setSortOrder(String sortOrder) {
        presenter.loadArtists();
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            getLibraryFragment().getToolbar().setTitle(
                    PreferenceUtil.getInstance(getContext()).tabTitles() ? R.string.library
                            : R.string.artists);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLibraryFragment().getToolbar().setTitle(
                PreferenceUtil.getInstance(getContext()).tabTitles() ? R.string.library : R.string.artists);
        if (getAdapter().getDataSet().isEmpty()) {
            presenter.subscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
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
    public void showData(ArrayList<Artist> artists) {
        getAdapter().swapDataSet(artists);
    }

}
