package code.name.monkey.retromusic.ui.fragments;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import code.name.monkey.retromusic.R;


public enum NowPlayingScreen {

    ADAPTIVE(R.string.adaptive, R.drawable.np_adaptive, 10),
    BLUR(R.string.blur, R.drawable.np_blur, 4);

    @StringRes
    public final int titleRes;
    @DrawableRes
    public final int drawableResId;
    public final int id;

    NowPlayingScreen(@StringRes int titleRes, @DrawableRes int drawableResId, int id) {
        this.titleRes = titleRes;
        this.drawableResId = drawableResId;
        this.id = id;
    }
}
