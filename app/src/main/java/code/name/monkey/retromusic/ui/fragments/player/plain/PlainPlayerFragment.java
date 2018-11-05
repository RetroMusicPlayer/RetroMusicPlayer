package code.name.monkey.retromusic.ui.fragments.player.plain;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment;

/**
 * @author Hemanth S (h4h13).
 */

public class PlainPlayerFragment extends AbsPlayerFragment implements
        PlayerAlbumCoverFragment.Callbacks {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;

    private Unbinder unbinder;
    private PlainPlaybackControlsFragment plainPlaybackControlsFragment;
    private int mLastColor;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        updateSong();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        title.setText(song.title);
        text.setText(song.artistName);
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        updateSong();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plain_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getPlayerActivity() != null) {
            getPlayerActivity().setDrawUnderNavigationBar();
            getPlayerActivity().setNavigationbarColorAuto();
            addSafeArea(view);
        }
        return view;
    }

    private void setUpPlayerToolbar() {
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);

        ToolbarContentTintHelper.colorizeToolbar(toolbar,
                ATHUtil.resolveColor(getContext(), R.attr.iconColor),
                getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpSubFragments();
        setUpPlayerToolbar();
        title.setSelected(true);
    }

    private void setUpSubFragments() {
        plainPlaybackControlsFragment = (PlainPlaybackControlsFragment) getChildFragmentManager()
                .findFragmentById(R.id.playback_controls_fragment);
        PlayerAlbumCoverFragment playerAlbumCoverFragment = (PlayerAlbumCoverFragment) getChildFragmentManager()
                .findFragmentById(R.id.player_album_cover_fragment);
        playerAlbumCoverFragment.setCallbacks(this);
    }

    @Override
    public int getPaletteColor() {
        return mLastColor;
    }

    @Override
    public void onShow() {
        plainPlaybackControlsFragment.show();
    }

    @Override
    public void onHide() {
        plainPlaybackControlsFragment.hide();
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
        plainPlaybackControlsFragment.setDark(color);
        mLastColor = color;
        getCallbacks().onPaletteColorChanged();
        ToolbarContentTintHelper.colorizeToolbar(toolbar,
                ATHUtil.resolveColor(getContext(), R.attr.iconColor), getActivity());
    }

    @Override
    public void onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.getCurrentSong());
    }

    @Override
    protected void toggleFavorite(Song song) {
        super.toggleFavorite(song);
        if (song.id == MusicPlayerRemote.getCurrentSong().id) {
            updateIsFavorite();
        }
    }
}
