package code.name.monkey.retromusic.ui.fragments.player.material;

import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler;
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerControlsFragment;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;

/**
 * @author Hemanth S (h4h13).
 */
public class MaterialControlsFragment extends AbsPlayerControlsFragment {

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
    View mVolumeContainer;

    private Unbinder unbinder;
    private int lastPlaybackControlsColor;
    private int lastDisabledPlaybackControlsColor;
    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_playback_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setUpMusicControllers();

        if (PreferenceUtil.getInstance(getContext()).getVolumeToggle()) {
            mVolumeContainer.setVisibility(View.VISIBLE);
        } else {
            mVolumeContainer.setVisibility(View.GONE);
        }
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

    @Override
    public void setDark(int dark) {
        int color = ATHUtil.resolveColor(getActivity(), android.R.attr.colorBackground);
        if (ColorUtil.isColorLight(color)) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(getActivity(), true);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(getActivity(), true);
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(getActivity(), false);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(getActivity(), false);
        }

        updateRepeatState();
        updateShuffleState();

        if (PreferenceUtil.getInstance(getContext()).getAdaptiveColor()) {
            lastPlaybackControlsColor = dark;
            text.setTextColor(dark);
        }

        updatePlayPauseColor();
        updatePrevNextColor();
    }

    private void setUpPlayPauseFab() {
        playPauseFab.setOnClickListener(new PlayPauseButtonOnClickHandler());
    }

    protected void updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying()) {
            playPauseFab.setImageResource(R.drawable.ic_pause_white_big);
        } else {
            playPauseFab.setImageResource(R.drawable.ic_play_arrow_white_big);
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

    private void updatePlayPauseColor() {
        playPauseFab.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
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

    }

    @Override
    protected void hide() {

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
