package code.name.monkey.retromusic.ui.fragments.player.full;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler;
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.fragments.VolumeFragment;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerControlsFragment;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * Created by hemanths on 20/09/17.
 */

public class FullPlaybackControlsFragment extends AbsPlayerControlsFragment {
    @BindView(R.id.player_song_current_progress)
    TextView mPlayerSongCurrentProgress;

    @BindView(R.id.player_song_total_time)
    TextView songTotalTime;

    @BindView(R.id.player_progress_slider)
    SeekBar progressSlider;
    @BindView(R.id.player_prev_button)

    ImageButton playerPrevButton;
    @BindView(R.id.player_next_button)

    ImageButton playerNextButton;
    @BindView(R.id.player_repeat_button)

    ImageButton playerRepeatButton;
    @BindView(R.id.player_shuffle_button)

    ImageButton playerShuffleButton;
    @BindView(R.id.player_play_pause_button)

    ImageButton playerPlayPauseFab;


    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.text)
    TextView mText;

    @BindView(R.id.volume_fragment_container)
    View mVolumeContainer;

    Unbinder unbinder;
    private int lastPlaybackControlsColor;
    private int lastDisabledPlaybackControlsColor;
    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_player_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpMusicControllers();

        mVolumeContainer.setVisibility(PreferenceUtil.getInstance().getVolumeToggle() ? View.VISIBLE : View.GONE);
        VolumeFragment volumeFragment = (VolumeFragment) getChildFragmentManager().findFragmentById(R.id.volume_fragment);
        volumeFragment.tintWhiteColor();

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

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        progressSlider.setMax(total);

        ObjectAnimator animator = ObjectAnimator.ofInt(progressSlider, "progress", progress);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

        mPlayerSongCurrentProgress.setText(MusicUtil.getReadableDurationString(progress));
        songTotalTime.setText(MusicUtil.getReadableDurationString(total));
    }


    public void show() {
        playerPlayPauseFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }


    public void hide() {
        if (playerPlayPauseFab != null) {
            playerPlayPauseFab.setScaleX(0f);
            playerPlayPauseFab.setScaleY(0f);
            playerPlayPauseFab.setRotation(0f);
        }
    }

    public void setDark(int dark) {
        lastPlaybackControlsColor = Color.WHITE;
        lastDisabledPlaybackControlsColor = ContextCompat.getColor(getContext(), R.color.md_grey_500);

        if (PreferenceUtil.getInstance().getAdaptiveColor()) {
            setProgressBarColor(dark);
        } else {
            int accentColor = ThemeStore.accentColor(getContext());
            setProgressBarColor(accentColor);
        }

        updateRepeatState();
        updateShuffleState();
        updatePrevNextColor();
        updateProgressTextColor();
    }

    private void setProgressBarColor(int dark) {
        LayerDrawable ld = (LayerDrawable) progressSlider.getProgressDrawable();
        ClipDrawable clipDrawable = (ClipDrawable) ld.findDrawableByLayerId(android.R.id.progress);
        clipDrawable.setColorFilter(dark, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onServiceConnected() {
        updatePlayPauseDrawableState(false);
        updateRepeatState();
        updateShuffleState();
        updateSong();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        mTitle.setText(song.title);
        mText.setText(song.artistName);
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        updateSong();
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState(true);
    }

    protected void updatePlayPauseDrawableState(boolean animate) {

        if (MusicPlayerRemote.isPlaying()) {
            playerPlayPauseFab.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            playerPlayPauseFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    private void setUpPlayPauseFab() {

        playerPlayPauseFab.setOnClickListener(new PlayPauseButtonOnClickHandler());
        playerPlayPauseFab.post(() -> {
            if (playerPlayPauseFab != null) {
                playerPlayPauseFab.setPivotX(playerPlayPauseFab.getWidth() / 2);
                playerPlayPauseFab.setPivotY(playerPlayPauseFab.getHeight() / 2);
            }
        });
    }

    private void setUpMusicControllers() {
        setUpPlayPauseFab();
        setUpPrevNext();
        setUpRepeatButton();
        setUpShuffleButton();
        setUpProgressSlider();
    }

    private void setUpPrevNext() {
        updatePrevNextColor();
        playerNextButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());
        playerPrevButton.setOnClickListener(v -> MusicPlayerRemote.back());
    }

    private void updateProgressTextColor() {
        int color = MaterialValueHelper.getSecondaryTextColor(getContext(), false);
        //songTotalTime.setTextColor(color);
        //songCurrentProgress.setTextColor(color);
    }

    private void updatePrevNextColor() {
        playerNextButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
        playerPrevButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void setUpProgressSlider() {
        progressSlider.setOnSeekBarChangeListener(new SimpleOnSeekbarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress);
                    onUpdateProgressViews(MusicPlayerRemote.getSongProgressMillis(), MusicPlayerRemote.getSongDurationMillis());
                }
            }
        });
    }


    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    private void setUpRepeatButton() {
        playerRepeatButton.setOnClickListener(v -> MusicPlayerRemote.cycleRepeatMode());
    }

    @Override
    protected void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                playerRepeatButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_ALL:
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                playerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_THIS:
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                playerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void setUpShuffleButton() {
        playerShuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    @Override
    public void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                playerShuffleButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            default:
                playerShuffleButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }
}
