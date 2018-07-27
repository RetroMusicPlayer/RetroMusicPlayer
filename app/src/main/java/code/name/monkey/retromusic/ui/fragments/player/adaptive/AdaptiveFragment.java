package code.name.monkey.retromusic.ui.fragments.player.adaptive;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment.Callbacks;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;

public class AdaptiveFragment extends AbsPlayerFragment implements Callbacks {

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;
    @BindView(R.id.status_bar)
    View statusBar;

    private int lastColor;
    private AdaptivePlaybackControlsFragment playbackControlsFragment;
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
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public int toolbarIconColor() {
        return ATHUtil.resolveColor(getContext(), R.attr.iconColor);
    }

    @Override
    public void onColorChanged(int color) {
        playbackControlsFragment.setDark(color);
        lastColor = color;
        getCallbacks().onPaletteColorChanged();

        ToolbarContentTintHelper.colorizeToolbar(toolbar,
                ATHUtil.resolveColor(getContext(), R.attr.iconColor), getActivity());

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
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adaptive_player, container, false);
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
        playbackControlsFragment =
                (AdaptivePlaybackControlsFragment) getChildFragmentManager()
                        .findFragmentById(R.id.playback_controls_fragment);

        PlayerAlbumCoverFragment playerAlbumCoverFragment =
                (PlayerAlbumCoverFragment) getChildFragmentManager()
                        .findFragmentById(R.id.player_album_cover_fragment);
        playerAlbumCoverFragment.setCallbacks(this);
        playerAlbumCoverFragment.removeSlideEffect();
    }

    private void setUpPlayerToolbar() {
        int primaryColor = ATHUtil.resolveColor(getContext(), R.attr.iconColor);
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);

        ToolbarContentTintHelper.colorizeToolbar(toolbar, primaryColor, getActivity());
        toolbar.setTitleTextColor(primaryColor);
        toolbar.setSubtitleTextColor(ThemeStore.textColorSecondary(getContext()));
    }

    @Override
    public void onServiceConnected() {
        updateIsFavorite();
        updateSong();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        toolbar.setTitle(song.title);
        toolbar.setSubtitle(song.artistName);
    }

    @Override
    public void onPlayingMetaChanged() {
        updateIsFavorite();
        updateSong();
    }

}
