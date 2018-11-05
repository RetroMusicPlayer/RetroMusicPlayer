package code.name.monkey.retromusic.ui.fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment;
import code.name.monkey.retromusic.util.NavigationUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MiniPlayerFragment extends AbsMusicServiceFragment implements MusicProgressViewUpdateHelper.Callback {

    @BindView(R.id.mini_player_title)
    TextView miniPlayerTitle;

    @BindView(R.id.mini_player_play_pause_button)
    ImageView miniPlayerPlayPauseButton;

    @BindView(R.id.action_next)
    View next;

    @BindView(R.id.action_prev)
    View previous;

    @BindView(R.id.action_playing_queue)
    View playingQueue;

    @BindView(R.id.progress_bar)
    MaterialProgressBar progressBar;

    private Unbinder unbinder;
    private MusicProgressViewUpdateHelper progressViewUpdateHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mini_player, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //noinspection ConstantConditions
        view.setBackgroundColor(ThemeStore.primaryColor(getContext()));
        view.setOnTouchListener(new FlingPlayBackController(getActivity()));
        view.setOnClickListener(v -> NavigationUtil.gotoNowPlayingActivity(getContext(), Pair.create(v, getString(R.string.transition_mini_player))));
        setUpMiniPlayer();

        if (RetroUtil.isTablet(getResources())) {
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
            playingQueue.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(PreferenceUtil.getInstance().isExtraMiniExtraControls() ? View.VISIBLE : View.GONE);
            playingQueue.setVisibility(PreferenceUtil.getInstance().isExtraMiniExtraControls() ? View.GONE : View.VISIBLE);
            previous.setVisibility(PreferenceUtil.getInstance().isExtraMiniExtraControls() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressWarnings({"ConstantConditions"})
    private void setUpMiniPlayer() {
        setUpPlayPauseButton();
        progressBar.setProgressTintList(ColorStateList.valueOf(ThemeStore.accentColor(getActivity())));
    }

    private void setUpPlayPauseButton() {
        //noinspection ConstantConditions
        miniPlayerPlayPauseButton.setOnClickListener(new PlayPauseButtonOnClickHandler());
    }

    private void updateSongTitle() {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        Song song = MusicPlayerRemote.getCurrentSong();
        if (song == null) {
            return;
        }
        SpannableString title = new SpannableString(song.title);
        title.setSpan(new ForegroundColorSpan(ThemeStore.textColorPrimary(getContext())), 0, title.length(), 0);

        SpannableString text = new SpannableString(song.artistName);
        text.setSpan(new ForegroundColorSpan(ThemeStore.textColorSecondary(getContext())), 0, text.length(), 0);

        builder.append(title).append(" • ").append(text);

        miniPlayerTitle.setSelected(true);
        miniPlayerTitle.setText(builder);
    }

    @Override
    public void onServiceConnected() {
        updateSongTitle();
        updatePlayPauseDrawableState();
    }

    @Override
    public void onPlayingMetaChanged() {
        updateSongTitle();
    }

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState();
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        progressBar.setMax(total);
        ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progress);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
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

    protected void updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying()) {
            miniPlayerPlayPauseButton.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            miniPlayerPlayPauseButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    public void setColor(int playerFragmentColor) {
        //noinspection ConstantConditions
        getView().setBackgroundColor(playerFragmentColor);
    }

    @OnClick({R.id.action_prev, R.id.action_playing_queue, R.id.action_next})
    void onClicks(View view) {
        switch (view.getId()) {
            case R.id.action_playing_queue:
                NavigationUtil.goToPlayingQueue(getActivity());
                break;
            case R.id.action_next:
                MusicPlayerRemote.playNextSong();
                break;
            case R.id.action_prev:
                MusicPlayerRemote.back();
                break;
        }
    }

    public static class FlingPlayBackController implements View.OnTouchListener {

        GestureDetector flingPlayBackController;

        public FlingPlayBackController(Context context) {
            flingPlayBackController = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                               float velocityY) {
                            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                                if (velocityX < 0) {
                                    MusicPlayerRemote.playNextSong();
                                    return true;
                                } else if (velocityX > 0) {
                                    MusicPlayerRemote.playPreviousSong();
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return flingPlayBackController.onTouchEvent(event);
        }
    }
}
