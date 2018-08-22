package code.name.monkey.retromusic.ui.fragments.player.flat;

import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper.Callback;
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler;
import code.name.monkey.retromusic.misc.SimpleOnSeekbarChangeListener;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.views.PlayPauseDrawable;

public class FlatPlaybackControlsFragment extends AbsMusicServiceFragment implements Callback {

    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.playback_controls)
    ViewGroup viewGroup;
    @BindView(R.id.player_song_total_time)
    TextView mSongTotalTime;
    @BindView(R.id.player_song_current_progress)
    TextView mPlayerSongCurrentProgress;
    @BindView(R.id.player_repeat_button)
    ImageButton mPlayerRepeatButton;
    @BindView(R.id.player_shuffle_button)
    ImageButton mPlayerShuffleButton;
    @BindView(R.id.player_play_pause_button)
    ImageView mPlayerPlayPauseFab;
    Unbinder unbinder;
    @BindView(R.id.player_progress_slider)
    SeekBar progressSlider;
    @BindView(R.id.volume_fragment_container)
    View mVolumeContainer;
    private int lastPlaybackControlsColor;
    private int lastDisabledPlaybackControlsColor;
    private MusicProgressViewUpdateHelper progressViewUpdateHelper;
    private PlayPauseDrawable playerFabPlayPauseDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flat_player_playback_controls, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMusicControllers();

        mVolumeContainer.setVisibility(PreferenceUtil.getInstance(getContext()).getVolumeToggle() ? View.VISIBLE : View.GONE);
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
        mSongTotalTime.setText(MusicUtil.getReadableDurationString(total));
    }


    public void show() {
        mPlayerPlayPauseFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }


    public void hide() {
        if (mPlayerPlayPauseFab != null) {
            mPlayerPlayPauseFab.setScaleX(0f);
            mPlayerPlayPauseFab.setScaleY(0f);
            mPlayerPlayPauseFab.setRotation(0f);
        }
    }

    public void setDark(int dark) {
        int color = ATHUtil.resolveColor(getActivity(), android.R.attr.colorBackground);
        boolean isDark = ColorUtil.isColorLight(color);
        if (isDark) {
            lastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(getActivity(), true);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(getActivity(), true);
        } else {
            lastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(getActivity(), false);
            lastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(getActivity(), false);
        }
        int accentColor = ThemeStore.accentColor(getContext());
        boolean b = PreferenceUtil.getInstance(getContext()).getAdaptiveColor();
        updateTextColors(b ? dark : accentColor);
        setProgressBarColor(b ? dark : accentColor);


        updateRepeatState();
        updateShuffleState();
    }

    private void setProgressBarColor(int dark) {
        TintHelper.setTintAuto(progressSlider, dark, false);
        //LayerDrawable ld = (LayerDrawable) progressSlider.getProgressDrawable();
        //ClipDrawable clipDrawable = (ClipDrawable) ld.findDrawableByLayerId(android.R.id.progress);
        //clipDrawable.setColorFilter(dark, PorterDuff.Mode.SRC_IN);
    }

    private void updateTextColors(int color) {
        boolean isDark = ColorUtil.isColorLight(color);
        int darkColor = ColorUtil.darkenColor(color);
        int colorPrimary = MaterialValueHelper.getPrimaryTextColor(getContext(), isDark);
        int colorSecondary = MaterialValueHelper.getSecondaryTextColor(getContext(), ColorUtil.isColorLight(darkColor));

        TintHelper.setTintAuto(mPlayerPlayPauseFab, colorPrimary, false);
        TintHelper.setTintAuto(mPlayerPlayPauseFab, color, true);

        mTitle.setBackgroundColor(color);
        mTitle.setTextColor(colorPrimary);
        mText.setBackgroundColor(darkColor);
        mText.setTextColor(colorSecondary);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
            playerFabPlayPauseDrawable.setPause(animate);
        } else {
            playerFabPlayPauseDrawable.setPlay(animate);
        }
    }

    private void setUpPlayPauseFab() {
        playerFabPlayPauseDrawable = new PlayPauseDrawable(getActivity());

        mPlayerPlayPauseFab.setImageDrawable(
                playerFabPlayPauseDrawable); // Note: set the drawable AFTER TintHelper.setTintAuto() was called
        //playPauseFab.setColorFilter(MaterialValueHelper.getPrimaryTextColor(getContext(), ColorUtil.isColorLight(fabColor)), PorterDuff.Mode.SRC_IN);
        mPlayerPlayPauseFab.setOnClickListener(new PlayPauseButtonOnClickHandler());
        mPlayerPlayPauseFab.post(() -> {
            if (mPlayerPlayPauseFab != null) {
                mPlayerPlayPauseFab.setPivotX(mPlayerPlayPauseFab.getWidth() / 2);
                mPlayerPlayPauseFab.setPivotY(mPlayerPlayPauseFab.getHeight() / 2);
            }
        });
    }

    private void setUpMusicControllers() {
        setUpPlayPauseFab();
        setUpRepeatButton();
        setUpShuffleButton();
        setUpProgressSlider();
    }

    private void updateSong() {
        //TransitionManager.beginDelayedTransition(viewGroup, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        Song song = MusicPlayerRemote.getCurrentSong();
        mTitle.setText(song.title);
        mText.setText(song.artistName);

    }

    private void setUpProgressSlider() {
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
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    private void setUpRepeatButton() {
        mPlayerRepeatButton.setOnClickListener(v -> MusicPlayerRemote.cycleRepeatMode());
    }

    private void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                mPlayerRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                mPlayerRepeatButton
                        .setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_ALL:
                mPlayerRepeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                mPlayerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_THIS:
                mPlayerRepeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                mPlayerRepeatButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void setUpShuffleButton() {
        mPlayerShuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    private void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                mPlayerShuffleButton.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            default:
                mPlayerShuffleButton
                        .setColorFilter(lastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }
}
