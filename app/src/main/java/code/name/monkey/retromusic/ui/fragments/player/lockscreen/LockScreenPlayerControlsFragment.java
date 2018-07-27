package code.name.monkey.retromusic.ui.fragments.player.lockscreen;

import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.fragments.VolumeFragment;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerControlsFragment;
import code.name.monkey.retromusic.util.MusicUtil;

/**
 * @author Hemanth S (h4h13).
 */
public class LockScreenPlayerControlsFragment extends AbsPlayerControlsFragment {
    @BindView(R.id.player_play_pause_button)
    AppCompatImageButton playPauseFab;
    @BindView(R.id.player_prev_button)
    ImageButton prevButton;
    @BindView(R.id.player_next_button)
    ImageButton nextButton;
    @BindView(R.id.player_progress_slider)
    AppCompatSeekBar progressSlider;
    @BindView(R.id.player_song_total_time)
    TextView songTotalTime;
    @BindView(R.id.player_song_current_progress)
    TextView songCurrentProgress;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.text)
    AppCompatTextView text;

    private Unbinder unbinder;
    private MusicProgressViewUpdateHelper progressViewUpdateHelper;
    private int lastPlaybackControlsColor;

    public LockScreenPlayerControlsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_screen_playback_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMusicControllers();

        VolumeFragment volumeFragment = (VolumeFragment) getChildFragmentManager().findFragmentById(R.id.volume_fragment);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateSong() {
        Song song = MusicPlayerRemote.getCurrentSong();

        title.setText(song.title);
        text.setText(String.format("%s - %s", song.artistName, song.albumName));

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
    public void onServiceConnected() {
        updatePlayPauseDrawableState(false);
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
        updatePlayPauseDrawableState(true);
    }

    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    @Override
    public void setDark(int dark) {
        setProgressBarColor(progressSlider, dark);

        if (ColorUtil.isColorLight(ATHUtil.resolveColor(getContext(), android.R.attr.windowBackground))) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(getActivity(), true);
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(getActivity(), false);

        }

        updatePrevNextColor();

        boolean isDark = ColorUtil.isColorLight(dark);
        text.setTextColor(dark);
        TintHelper.setTintAuto(playPauseFab, MaterialValueHelper.getPrimaryTextColor(getContext(), isDark), false);
        TintHelper.setTintAuto(playPauseFab, dark, true);
    }

    public void setProgressBarColor(SeekBar progressBar, int newColor) {
        TintHelper.setTintAuto(progressBar, newColor, false);
        //LayerDrawable ld = (LayerDrawable) progressBar.getProgressDrawable();
        //ClipDrawable clipDrawable = (ClipDrawable) ld.findDrawableByLayerId(android.R.id.progress);
        //clipDrawable.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
    }

    private void setUpPlayPauseFab() {
        playPauseFab.post(() -> {
            if (playPauseFab != null) {
                playPauseFab.setPivotX(playPauseFab.getWidth() / 2);
                playPauseFab.setPivotY(playPauseFab.getHeight() / 2);
            }
        });
    }


    private void setUpMusicControllers() {
        setUpPlayPauseFab();
        setUpPrevNext();
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
    protected void updateShuffleState() {
        //TODO(Nothing to Implement)
    }

    @Override
    protected void updateRepeatState() {
        //TODO(Nothing to Implement)
    }

    @Override
    protected void setUpProgressSlider() {
        progressSlider.setOnSeekBarChangeListener(new SimpleOnSeekbarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress);
                    onUpdateProgressViews(MusicPlayerRemote.getSongProgressMillis(),
                            MusicPlayerRemote.getSongDurationMillis());
                }
            }
        });
    }

    public void showBouceAnimation() {
        playPauseFab.clearAnimation();

        playPauseFab.setScaleX(0.9f);
        playPauseFab.setScaleY(0.9f);
        playPauseFab.setVisibility(View.VISIBLE);
        playPauseFab.setPivotX(playPauseFab.getWidth() / 2);
        playPauseFab.setPivotY(playPauseFab.getHeight() / 2);

        playPauseFab.animate()
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(() -> playPauseFab.animate()
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .start())
                .start();
    }

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
            playPauseFab.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            playPauseFab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    @OnClick(R.id.player_play_pause_button)
    void showAnimation() {
        if (MusicPlayerRemote.isPlaying()) {
            MusicPlayerRemote.pauseSong();
        } else {
            MusicPlayerRemote.resumePlaying();
        }
        showBouceAnimation();
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
}
