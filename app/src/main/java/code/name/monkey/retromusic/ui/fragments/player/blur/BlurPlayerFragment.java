package code.name.monkey.retromusic.ui.fragments.player.blur;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.adapter.song.PlayingQueueAdapter;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;
import com.bumptech.glide.Glide;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author Hemanth S (h4h13).
 */

public class BlurPlayerFragment extends AbsPlayerFragment implements
    PlayerAlbumCoverFragment.Callbacks {

  @BindView(R.id.player_toolbar)
  Toolbar toolbar;
  @BindView(R.id.toolbar_container)
  View toolbarContainer;
  @BindView(R.id.gradient_background)
  ImageView colorBackground;
  @BindView(R.id.status_bar)
  View statusBar;
  @Nullable
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @Nullable
  @BindView(R.id.title)
  TextView title;

  private int lastColor;
  private BlurPlaybackControlsFragment playbackControlsFragment;
  private Unbinder unbinder;

  private RecyclerView.Adapter wrappedAdapter;
  private RecyclerViewDragDropManager recyclerViewDragDropManager;
  private PlayingQueueAdapter playingQueueAdapter;
  private LinearLayoutManager layoutManager;

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
    View view = inflater.inflate(R.layout.fragment_blur, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    toggleStatusBar(statusBar);

    setUpSubFragments();
    setUpPlayerToolbar();
  }

  private void setUpSubFragments() {
    playbackControlsFragment = (BlurPlaybackControlsFragment) getChildFragmentManager()
        .findFragmentById(R.id.playback_controls_fragment);

    PlayerAlbumCoverFragment playerAlbumCoverFragment =
        (PlayerAlbumCoverFragment) getChildFragmentManager()
            .findFragmentById(R.id.player_album_cover_fragment);
    playerAlbumCoverFragment.setCallbacks(this);
  }

  private void setUpPlayerToolbar() {
    toolbar.inflateMenu(R.menu.menu_player);
    //noinspection ConstantConditions
    toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    toolbar.setOnMenuItemClickListener(this);

    ToolbarContentTintHelper.colorizeToolbar(toolbar, Color.WHITE, getActivity());
  }

  private void updateBlur() {
    Activity activity = getActivity();
    if (activity == null) {
      return;
    }

    int blurAmount = PreferenceManager.getDefaultSharedPreferences(getContext())
        .getInt("blur_amount", 25);

    colorBackground.clearColorFilter();

    SongGlideRequest.Builder.from(Glide.with(activity), MusicPlayerRemote.getCurrentSong())
        .checkIgnoreMediaStore(activity)
        .generatePalette(activity)
        .build()
        .override(320, 480)
        .transform(new BlurTransformation(getActivity(), blurAmount))
        .into(new RetroMusicColoredTarget(colorBackground) {
          @Override
          public void onColorReady(int color) {
            if (color == getDefaultFooterColor()) {
              colorBackground.setColorFilter(color);
            }
          }
        });
  }

  @Override
  public void onServiceConnected() {
    updateIsFavorite();
    updateBlur();
    setUpRecyclerView();
  }

  @Override
  public void onPlayingMetaChanged() {
    updateIsFavorite();
    updateBlur();
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
      // if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
      resetToCurrentPosition();
      //}
    }
  }

  private void updateQueue() {
    if (playingQueueAdapter != null) {
      playingQueueAdapter
          .swapDataSet(MusicPlayerRemote.getPlayingQueue(), MusicPlayerRemote.getPosition());
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
