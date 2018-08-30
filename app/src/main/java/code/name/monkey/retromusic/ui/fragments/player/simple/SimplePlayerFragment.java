package code.name.monkey.retromusic.ui.fragments.player.simple;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SimplePlayerFragment extends AbsPlayerFragment implements
        PlayerAlbumCoverFragment.Callbacks {

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private SimplePlaybackControlsFragment simplePlaybackControlsFragment;
    private int lastColor;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSubFragments();
        setUpPlayerToolbar();
    }

    private void setUpSubFragments() {
        PlayerAlbumCoverFragment playerAlbumCoverFragment = (PlayerAlbumCoverFragment)
                getChildFragmentManager().findFragmentById(R.id.player_album_cover_fragment);
        playerAlbumCoverFragment.setCallbacks(this);
        simplePlaybackControlsFragment = (SimplePlaybackControlsFragment)
                getChildFragmentManager().findFragmentById(R.id.playback_controls_fragment);

    }

    @Override
    public int getPaletteColor() {
        return lastColor;
    }

    @Override
    public void onShow() {
        simplePlaybackControlsFragment.show();
    }

    @Override
    public void onHide() {
        simplePlaybackControlsFragment.hide();
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
        lastColor = color;
        getCallbacks().onPaletteColorChanged();
        simplePlaybackControlsFragment.setDark(color);
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

    private void setUpPlayerToolbar() {
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);

        ToolbarContentTintHelper.colorizeToolbar(toolbar,
                ATHUtil.resolveColor(getContext(), R.attr.iconColor),
                getActivity());
    }
}
