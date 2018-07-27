package code.name.monkey.retromusic.ui.fragments.base;

import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;

/**
 * Created by hemanths on 24/09/17.
 */

public abstract class AbsPlayerControlsFragment extends AbsMusicServiceFragment
        implements MusicProgressViewUpdateHelper.Callback {

    protected abstract void show();

    protected abstract void hide();

    protected abstract void updateShuffleState();

    protected abstract void updateRepeatState();

    protected abstract void setUpProgressSlider();

    public abstract void setDark(int color);
}
