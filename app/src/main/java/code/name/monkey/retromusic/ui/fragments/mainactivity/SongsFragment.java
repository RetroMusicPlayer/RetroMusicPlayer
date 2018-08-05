package code.name.monkey.retromusic.ui.fragments.mainactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.mvp.contract.SongContract;
import code.name.monkey.retromusic.mvp.presenter.SongPresenter;
import code.name.monkey.retromusic.ui.adapter.song.ShuffleButtonSongAdapter;
import code.name.monkey.retromusic.ui.adapter.song.SongAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment;
import code.name.monkey.retromusic.util.PreferenceUtil;
import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class SongsFragment extends
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<SongAdapter, GridLayoutManager> implements
    SongContract.SongView {

  private SongPresenter presenter;

  public SongsFragment() {
    // Required empty public constructor
  }

  public static SongsFragment newInstance() {
    Bundle args = new Bundle();
    SongsFragment fragment = new SongsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    presenter = new SongPresenter(this);
  }

  @NonNull
  @Override
  protected GridLayoutManager createLayoutManager() {
    return new GridLayoutManager(getActivity(), getGridSize());
  }

  @Override
  protected int getEmptyMessage() {
    return R.string.no_songs;
  }

  @NonNull
  @Override
  protected SongAdapter createAdapter() {
    int itemLayoutRes = getItemLayoutRes();
    notifyLayoutResChanged(itemLayoutRes);
    boolean usePalette = loadUsePalette();
    ArrayList<Song> dataSet =
        getAdapter() == null ? new ArrayList<Song>() : getAdapter().getDataSet();

    if (getGridSize() <= getMaxGridSizeForList()) {
      return new ShuffleButtonSongAdapter(getLibraryFragment().getMainActivity(), dataSet,
          itemLayoutRes, usePalette, getLibraryFragment());
    }
    return new SongAdapter(getLibraryFragment().getMainActivity(), dataSet, itemLayoutRes,
        usePalette, getLibraryFragment());
  }

  @Override
  public void onMediaStoreChanged() {
    presenter.loadSongs();
  }

  @Override
  protected int loadGridSize() {
    return PreferenceUtil.getInstance(getActivity()).getSongGridSize(getActivity());
  }

  @Override
  protected void saveGridSize(int gridSize) {
    PreferenceUtil.getInstance(getActivity()).setSongGridSize(gridSize);
  }

  @Override
  protected int loadGridSizeLand() {
    return PreferenceUtil.getInstance(getActivity()).getSongGridSizeLand(getActivity());
  }

  @Override
  protected void saveGridSizeLand(int gridSize) {
    PreferenceUtil.getInstance(getActivity()).setSongGridSizeLand(gridSize);
  }

  @Override
  public void saveUsePalette(boolean usePalette) {
    PreferenceUtil.getInstance(getActivity()).setSongColoredFooters(usePalette);
  }

  @Override
  public boolean loadUsePalette() {
    return PreferenceUtil.getInstance(getActivity()).songColoredFooters();
  }

  @Override
  public void setUsePalette(boolean usePalette) {
    getAdapter().usePalette(usePalette);
  }

  @Override
  protected void setGridSize(int gridSize) {
    getLayoutManager().setSpanCount(gridSize);
    getAdapter().notifyDataSetChanged();
  }

  @Override
  public void onResume() {
    super.onResume();
    getLibraryFragment().setTitle(
        PreferenceUtil.getInstance(getContext()).tabTitles() ? R.string.library : R.string.songs);
    if (getAdapter().getDataSet().isEmpty()) {
      presenter.subscribe();
    }
  }

  @Override
  public void setMenuVisibility(boolean menuVisible) {
    super.setMenuVisibility(menuVisible);
    if (menuVisible) {
      getLibraryFragment().setTitle(
          PreferenceUtil.getInstance(getContext()).tabTitles() ? R.string.library
              : R.string.songs);
    }
  }

  @Override
  public void onDestroy() {
    presenter.unsubscribe();
    super.onDestroy();
  }

  @Override
  public void loading() {

  }

  @Override
  public void showData(ArrayList<Song> songs) {
    getAdapter().swapDataSet(songs);
  }

  @Override
  public void showEmptyView() {
    getAdapter().swapDataSet(new ArrayList<Song>());
  }

  @Override
  public void completed() {

  }

  @Override
  protected String loadSortOrder() {
    return PreferenceUtil.getInstance(getActivity()).getSongSortOrder();
  }

  @Override
  protected void saveSortOrder(String sortOrder) {
    PreferenceUtil.getInstance(getActivity()).setSongSortOrder(sortOrder);
  }

  @Override
  protected void setSortOrder(String sortOrder) {
    presenter.loadSongs();
  }
}
