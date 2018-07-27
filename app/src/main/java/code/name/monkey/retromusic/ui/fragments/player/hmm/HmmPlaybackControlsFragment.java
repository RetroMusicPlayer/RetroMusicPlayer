package code.name.monkey.retromusic.ui.fragments.player.hmm;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerControlsFragment;

/**
 * @author Hemanth S (h4h13).
 */

public class HmmPlaybackControlsFragment extends AbsPlayerControlsFragment {
    @BindView(R.id.player_repeat_button)
    ImageButton repeatButton;
    @BindView(R.id.player_shuffle_button)
    ImageButton shuffleButton;
    private Unbinder unbinder;
    private int mLastPlaybackControlsColor;
    private int mLastDisabledPlaybackControlsColor;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hmm_controls_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMusicControllers();
    }

    private void setUpMusicControllers() {
        setUpRepeatButton();
        setUpShuffleButton();
        setUpProgressSlider();
    }

    @Override
    protected void show() {

    }

    @Override
    protected void hide() {

    }

    private void setUpShuffleButton() {
        shuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    @Override
    protected void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                shuffleButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            default:
                shuffleButton.setColorFilter(mLastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
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
                repeatButton.setColorFilter(mLastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_ALL:
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                repeatButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_THIS:
                repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                repeatButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    @Override
    protected void setUpProgressSlider() {

    }

    @Override
    public void setDark(int dark) {
        if (ColorUtil.isColorLight(dark)) {
            mLastPlaybackControlsColor = MaterialValueHelper.getSecondaryTextColor(getActivity(), true);
            mLastDisabledPlaybackControlsColor = MaterialValueHelper.getSecondaryDisabledTextColor(getActivity(), true);
        } else {
            mLastPlaybackControlsColor = MaterialValueHelper.getPrimaryTextColor(getActivity(), false);
            mLastDisabledPlaybackControlsColor = MaterialValueHelper.getPrimaryDisabledTextColor(getActivity(), false);
        }

        updateRepeatState();
        updateShuffleState();
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {

    }
}
