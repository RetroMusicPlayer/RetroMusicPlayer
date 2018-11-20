package code.name.monkey.retromusic.ui.fragments.player.cardblur;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.glide.BlurTransformation;
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget;
import code.name.monkey.retromusic.glide.SongGlideRequest;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment;
import code.name.monkey.retromusic.ui.fragments.player.normal.PlayerFragment;

public class CardBlurFragment extends AbsPlayerFragment implements
        PlayerAlbumCoverFragment.Callbacks {

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;

    @BindView(R.id.gradient_background)
    ImageView colorBackground;

    private int lastColor;
    private CardBlurPlaybackControlsFragment playbackControlsFragment;
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
    public void onResume() {
        super.onResume();
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

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
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
        View view = inflater.inflate(R.layout.fragment_card_blur_player, container, false);
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
        playbackControlsFragment = (CardBlurPlaybackControlsFragment) getChildFragmentManager()
                .findFragmentById(R.id.playback_controls_fragment);

        PlayerAlbumCoverFragment playerAlbumCoverFragment =
                (PlayerAlbumCoverFragment) getChildFragmentManager().findFragmentById(R.id.player_album_cover_fragment);
        if (playerAlbumCoverFragment != null) {
            playerAlbumCoverFragment.setCallbacks(this);
            playerAlbumCoverFragment.removeEffect();
        }

    }

    private void setUpPlayerToolbar() {
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);

        ToolbarContentTintHelper.colorizeToolbar(toolbar, Color.WHITE, getActivity());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
    }

    @Override
    public void onServiceConnected() {
        updateIsFavorite();
        updateBlur();
        updateSong();
    }

    @Override
    public void onPlayingMetaChanged() {
        updateIsFavorite();
        updateBlur();
        updateSong();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        toolbar.setTitle(song.title);
        toolbar.setSubtitle(song.artistName);
    }

    private void updateBlur() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        int blurAmount = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getInt("new_blur_amount", 25);

        colorBackground.clearColorFilter();
        SongGlideRequest.Builder.from(Glide.with(activity), MusicPlayerRemote.getCurrentSong())
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity)
                .build()
                .transform(new BlurTransformation.Builder(getActivity()).blurRadius(blurAmount).build())
                .into(new RetroMusicColoredTarget(colorBackground) {
                    @Override
                    public void onColorReady(int color) {
                        if (color == getDefaultFooterColor()) {
                            colorBackground.setColorFilter(color);
                        }
                    }
                });
    }
}
