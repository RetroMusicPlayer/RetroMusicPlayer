package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Album;
import code.name.monkey.retromusic.mvp.contract.AlbumContract;
import code.name.monkey.retromusic.mvp.presenter.AlbumPresenter;
import code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class AlbumsFragment extends AbsLibraryPagerRecyclerViewCustomGridSizeFragment<AlbumAdapter, GridLayoutManager> implements AlbumContract.AlbumView {

    public static final String TAG = AlbumsFragment.class.getSimpleName();

    private AlbumPresenter presenter;

    public static AlbumsFragment newInstance() {
        Bundle args = new Bundle();
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected GridLayoutManager createLayoutManager() {
        return new GridLayoutManager(getActivity(), getGridSize());
    }

    @NonNull
    @Override
    protected AlbumAdapter createAdapter() {
        int itemLayoutRes = getItemLayoutRes();
        notifyLayoutResChanged(itemLayoutRes);
        if (itemLayoutRes != R.layout.item_list) {
            //noinspection ConstantConditions
            itemLayoutRes = PreferenceUtil.getInstance().getAlbumGridStyle(getContext());
        }
        ArrayList<Album> dataSet = getAdapter() == null ? new ArrayList<>() : getAdapter().getDataSet();
        return new AlbumAdapter(getLibraryFragment().getMainActivity(), dataSet, itemLayoutRes, loadUsePalette(), getLibraryFragment());
    }

    @Override
    protected int getEmptyMessage() {
        return R.string.no_albums;
    }

    @Override
    public boolean loadUsePalette() {
        //noinspection ConstantConditions
        return PreferenceUtil.getInstance().albumColoredFooters();
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
    protected void setSortOrder(String sortOrder) {
        presenter.loadAlbums();
    }

    @Override
    protected String loadSortOrder() {
        //noinspection ConstantConditions
        return PreferenceUtil.getInstance().getAlbumSortOrder();
    }

    @Override
    protected void saveSortOrder(String sortOrder) {
        //noinspection ConstantConditions
        PreferenceUtil.getInstance().setAlbumSortOrder(sortOrder);
    }

    @Override
    protected int loadGridSize() {
        //noinspection ConstantConditions
        return PreferenceUtil.getInstance().getAlbumGridSize(getActivity());
    }

    @Override
    protected void saveGridSize(int gridSize) {
        //noinspection ConstantConditions
        PreferenceUtil.getInstance().setAlbumGridSize(gridSize);
    }

    @Override
    protected int loadGridSizeLand() {
        //noinspection ConstantConditions
        return PreferenceUtil.getInstance().getAlbumGridSizeLand(getActivity());
    }

    @Override
    protected void saveGridSizeLand(int gridSize) {
        //noinspection ConstantConditions
        PreferenceUtil.getInstance().setAlbumGridSizeLand(gridSize);
    }

    @Override
    protected void saveUsePalette(boolean usePalette) {
        //noinspection ConstantConditions
        PreferenceUtil.getInstance().setAlbumColoredFooters(usePalette);
    }

    @Override
    public void onMediaStoreChanged() {
        presenter.loadAlbums();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AlbumPresenter(this);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            //noinspection ConstantConditions
            getLibraryFragment().setTitle(
                    PreferenceUtil.getInstance().tabTitles() ? R.string.library
                            : R.string.albums);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //noinspection ConstantConditions
        getLibraryFragment().setTitle(
                PreferenceUtil.getInstance().tabTitles() ? R.string.library : R.string.albums);
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
    public void showData(ArrayList<Album> albums) {
        getAdapter().swapDataSet(albums);
    }

}
