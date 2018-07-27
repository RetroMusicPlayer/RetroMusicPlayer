package code.name.monkey.retromusic.ui.fragments.player.card;

import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.appthemehelper.util.TintHelper;
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
import code.name.monkey.retromusic.views.PlayPauseDrawable;

public class CardPlaybackControlsFragment extends AbsPlayerControlsFragment {
    @BindView(R.id.player_play_pause_button)
    ImageButton playPauseButton;
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
    @BindView(R.id.menu)
    View menuView;
    @BindView(R.id.image_text_container)
    CardView colorContainer;

    @BindView(R.id.image)
    ImageView playImageView;
    @BindView(R.id.playback_controls)
    View playbackControls;

    private Unbinder unbinder;
    private PlayPauseDrawable playPauseDrawable;
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
        View view = inflater.inflate(R.layout.fragment_card_player_playback_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setUpMusicControllers();

        hideVolumeIfAvailable();

        setupControls();
    }

    private void setupControls() {
        playImageView.setImageResource(R.drawable.ic_play_circle_filled_white_24dp);
        //noinspection ConstantConditions
        int iconPadding = getActivity().getResources().getDimensionPixelSize(R.dimen.list_item_image_icon_padding);
        playImageView.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);

        menuView.setVisibility(View.GONE);

        int primaryColor = ThemeStore.primaryColor(getContext());
        playbackControls.setBackgroundColor(primaryColor);
        colorContainer.setCardBackgroundColor(primaryColor);
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
        playImageView.setColorFilter(dark, PorterDuff.Mode.SRC_IN);
        if (ColorUtil.isColorLight(ATHUtil.resolveColor(getContext(), android.R.attr.windowBackground))) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(getActivity(), true);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(getActivity(), true);
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(getActivity(), false);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(getActivity(), false);
        }

        updateRepeatState();
        updateShuffleState();
        updatePrevNextColor();
        updatePlayPauseColor();
        updateProgressTextColor();

        TintHelper.setTintAuto(playPauseButton, MaterialValueHelper.getPrimaryTextColor(getContext(), ColorUtil.isColorLight(dark)), false);
        TintHelper.setTintAuto(playPauseButton, dark, true);
    }

    private void updatePlayPauseColor() {
        //playPauseButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    private void setUpPlayPauseButton() {
        playPauseDrawable = new PlayPauseDrawable(getActivity());
        playPauseButton.setImageDrawable(playPauseDrawable);
        updatePlayPauseColor();
        playPauseButton.setOnClickListener(new PlayPauseButtonOnClickHandler());
        playPauseButton.post(() -> {
            if (playPauseButton != null) {
                playPauseButton.setPivotX(playPauseButton.getWidth() / 2);
                playPauseButton.setPivotY(playPauseButton.getHeight() / 2);
            }
        });
    }

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
            playPauseDrawable.setPause(animate);
        } else {
            playPauseDrawable.setPlay(animate);
        }
    }

    private void setUpMusicControllers() {
        setUpPlayPauseButton();
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

    private void updateProgressTextColor() {
        int color = MaterialValueHelper.getPrimaryTextColor(getContext(), false);
        songTotalTime.setTextColor(color);
        songCurrentProgress.setTextColor(color);
    }

    @Override
    protected void show() {
        //Ignore
    }

    @Override
    protected void hide() {
        //Ignore
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

    public void showBouceAnimation() {
        playPauseButton.clearAnimation();

        playPauseButton.setScaleX(0.9f);
        playPauseButton.setScaleY(0.9f);
        playPauseButton.setVisibility(View.VISIBLE);
        playPauseButton.setPivotX(playPauseButton.getWidth() / 2);
        playPauseButton.setPivotY(playPauseButton.getHeight() / 2);

        playPauseButton.animate()
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(() -> playPauseButton.animate()
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .start())
                .start();
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

    public void hideVolumeIfAvailable() {
        volumeContainer.setVisibility(PreferenceUtil.getInstance(getContext()).getVolumeToggle() ? View.VISIBLE : View.GONE);
    }


}
