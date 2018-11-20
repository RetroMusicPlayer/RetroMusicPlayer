package code.name.monkey.retromusic.ui.fragments.player.hmm;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.internal.MDTintHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.fragments.MiniPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerFragment;
import code.name.monkey.retromusic.ui.fragments.player.PlayerAlbumCoverFragment;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * @author Hemanth S (h4h13).
 */

public class HmmPlayerFragment extends AbsPlayerFragment implements
        MusicProgressViewUpdateHelper.Callback, PlayerAlbumCoverFragment.Callbacks {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.player_song_total_time)
    TextView totalTime;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.player_toolbar)
    Toolbar toolbar;

    private MusicProgressViewUpdateHelper progressViewUpdateHelper;
    private Unbinder unBinder;
    private int lastColor;
    private HmmPlaybackControlsFragment hmmPlaybackControlsFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressViewUpdateHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        progressViewUpdateHelper.stop();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        title.setText(song.title);
        text.setText(String.format("%s \nby -%s", song.albumName, song.artistName));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hmm_player, container, false);
        unBinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setOnClickListener(new PlayPauseButtonOnClickHandler());
        progressBar.setOnTouchListener(new MiniPlayerFragment.FlingPlayBackController(getActivity()));

        setUpPlayerToolbar();
        setUpSubFragments();
    }

    private void setUpSubFragments() {
        hmmPlaybackControlsFragment = (HmmPlaybackControlsFragment) getChildFragmentManager()
                .findFragmentById(R.id.playback_controls_fragment);
        PlayerAlbumCoverFragment playerAlbumCoverFragment = (PlayerAlbumCoverFragment) getChildFragmentManager()
                .findFragmentById(R.id.player_album_cover_fragment);
        playerAlbumCoverFragment.setCallbacks(this);

    }

    @Override
    public int getPaletteColor() {
        return lastColor;
    }

    @Override
    public void onShow() {
        hmmPlaybackControlsFragment.show();
    }

    @Override
    public void onHide() {
        hmmPlaybackControlsFragment.hide();
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
        return MaterialValueHelper
                .getSecondaryTextColor(getContext(), ColorUtil.isColorLight(lastColor));
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        progressBar.setMax(total);

        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progress);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animator);

        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();

        totalTime.setText(String.format("%s/%s", MusicUtil.getReadableDurationString(total),
                MusicUtil.getReadableDurationString(progress)));
    }

    private void setUpPlayerToolbar() {
        toolbar.inflateMenu(R.menu.menu_player);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(this);
    }


    @Override
    protected void toggleFavorite(Song song) {
        super.toggleFavorite(song);
        if (song.id == MusicPlayerRemote.getCurrentSong().id) {
            updateIsFavorite();
        }
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        updateSong();
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        updateSong();
    }

    @Override
    public void onColorChanged(int color) {
        lastColor = PreferenceUtil.getInstance().getAdaptiveColor() ? color :
                ThemeStore.accentColor(getContext());
        getCallbacks().onPaletteColorChanged();
        hmmPlaybackControlsFragment.setDark(lastColor);
        setProgressBarColor(lastColor);

        int iconColor = MaterialValueHelper
                .getSecondaryTextColor(getContext(), ColorUtil.isColorLight(lastColor));
        ToolbarContentTintHelper.colorizeToolbar(toolbar, iconColor, getActivity());
    }

    private void setProgressBarColor(int color) {
        MDTintHelper.setTint(progressBar, color);
    }

    @Override
    public void onFavoriteToggled() {
        toggleFavorite(MusicPlayerRemote.getCurrentSong());
    }

}
