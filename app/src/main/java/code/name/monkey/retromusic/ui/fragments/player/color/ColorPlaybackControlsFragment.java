package code.name.monkey.retromusic.ui.fragments.player.color;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.TintHelper;
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

public class ColorPlaybackControlsFragment extends AbsPlayerControlsFragment {

    @BindView(R.id.player_play_pause_button)
    ImageButton playPauseFab;

    @BindView(R.id.player_prev_button)
    ImageButton prevButton;

    @BindView(R.id.player_next_button)
    ImageButton nextButton;

    @BindView(R.id.player_repeat_button)
    ImageButton repeatButton;

    @BindView(R.id.player_shuffle_button)
    ImageButton shuffleButton;

    @BindView(R.id.player_progress_slider)
    AppCompatSeekBar progressSlider;

    @BindView(R.id.player_song_total_time)
    TextView songTotalTime;

    @BindView(R.id.player_song_current_progress)
    TextView songCurrentProgress;

    @BindView(R.id.title)
    AppCompatTextView title;

    @BindView(R.id.text)
    TextView text;

    @BindView(R.id.volume_fragment_container)
    View volumeContainer;

    private Unbinder unbinder;
    private int lastPlaybackControlsColor;
    private int lastDisabledPlaybackControlsColor;
    private MusicProgressViewUpdateHelper progressViewUpdateHelper;
    private VolumeFragment volumeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color_player_playback_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setUpMusicControllers();

        if (PreferenceUtil.getInstance().getVolumeToggle()) {
            volumeContainer.setVisibility(View.VISIBLE);
        } else {
            volumeContainer.setVisibility(View.GONE);
        }

        volumeFragment = (VolumeFragment) getChildFragmentManager().findFragmentById(R.id.volume_fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();
        title.setText(song.title);
        text.setText(song.artistName);
    }

    @Override
    public void onServiceConnected() {
        updatePlayPauseDrawableState();
        updateRepeatState();
        updateShuffleState();
        updateSong();
    }

    @Override
    public void onPlayingMetaChanged() {
        super.onPlayingMetaChanged();
        updateSong();
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState();
    }

    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }


    public void setDark(int textColor, int background) {
        setDark(textColor);
        TintHelper.setTintAuto(playPauseFab, background, false);
        TintHelper.setTintAuto(playPauseFab, textColor, true);
    }

    @Override
    public void setDark(int color) {
        lastPlaybackControlsColor = color;
        lastDisabledPlaybackControlsColor = ColorUtil.withAlpha(color, 0.5f);

        title.setTextColor(lastPlaybackControlsColor);
        text.setTextColor(lastDisabledPlaybackControlsColor);

        TintHelper.setTintAuto(progressSlider, lastPlaybackControlsColor, false);

        volumeFragment.setTintable(lastPlaybackControlsColor);

        songCurrentProgress.setTextColor(lastDisabledPlaybackControlsColor);
        songTotalTime.setTextColor(lastDisabledPlaybackControlsColor);

        updateRepeatState();
        updateShuffleState();
        updatePrevNextColor();
    }


    private void setUpPlayPauseFab() {
        TintHelper.setTintAuto(playPauseFab, Color.WHITE, true);
        TintHelper.setTintAuto(playPauseFab, Color.BLACK, false);
        playPauseFab.setOnClickListener(new PlayPauseButtonOnClickHandler());
    }


    protected void updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying()) {
            playPauseFab.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            playPauseFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
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
        nextButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());
        prevButton.setOnClickListener(v -> MusicPlayerRemote.back());
    }

    private void updatePrevNextColor() {
        nextButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
        prevButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    private void setUpShuffleButton() {
        shuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    @Override
    protected void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                shuffleButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            default:
                shuffleButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void setUpRepeatButton() {
        repeatButton.setOnClickListener(v -> MusicPlayerRemote.cycleRepeatMode());
    }

    @Override
    protected void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                repeatButton.setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_ALL:
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                repeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_THIS:
                repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                repeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }


    @Override
    protected void show() {
        playPauseFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(360f)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    @Override
    protected void hide() {
        if (playPauseFab != null) {
            playPauseFab.setScaleX(0f);
            playPauseFab.setScaleY(0f);
            playPauseFab.setRotation(0f);
        }
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
    public void onUpdateProgressViews(int progress, int total) {
        progressSlider.setMax(total);

        ObjectAnimator animator = ObjectAnimator.ofInt(progressSlider, "progress", progress);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

        songTotalTime.setText(MusicUtil.getReadableDurationString(total));
        songCurrentProgress.setText(MusicUtil.getReadableDurationString(progress));
    }

    public void hideVolumeIfAvailable() {
        volumeContainer.setVisibility(PreferenceUtil.getInstance().getVolumeToggle() ? View.VISIBLE : View.GONE);
    }
}
