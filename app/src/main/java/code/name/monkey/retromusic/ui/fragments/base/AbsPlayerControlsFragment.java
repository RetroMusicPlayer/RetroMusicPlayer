package code.name.monkey.retromusic.ui.fragments.base;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

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

    public void showBouceAnimation(View view) {
        view.clearAnimation();

        view.setScaleX(0.9f);
        view.setScaleY(0.9f);
        view.setVisibility(View.VISIBLE);
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight() / 2);

        view.animate()
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(() -> view.animate()
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .start())
                .start();
    }

}
