package code.name.monkey.retromusic.misc;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import butterknife.BindInt;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.RetroUtil;

/**
 * {@link View.OnClickListener} used to translate the product grid sheet downward on
 * the Y-axis when the navigation icon in the toolbar is pressed.
 */
public class NavigationIconClickListener implements View.OnClickListener {

    private final AnimatorSet animatorSet = new AnimatorSet();
    @BindInt(R.integer.options_height)
    int options;
    private Context context;
    private View sheet, menu;
    private Interpolator interpolator;
    private int height;
    private boolean backdropShown = false;
    private Drawable openIcon;
    private Drawable closeIcon;

    public NavigationIconClickListener(Context context, View sheet, View menu, @Nullable Interpolator interpolator) {
        this(context, sheet, menu, interpolator, null, null);
    }

    public NavigationIconClickListener(Context context, View sheet, View menu, @Nullable Interpolator interpolator,
                                       @Nullable Drawable openIcon, @Nullable Drawable closeIcon) {
        this.context = context;
        this.sheet = sheet;
        this.menu = menu;
        this.interpolator = interpolator;
        this.openIcon = openIcon;
        this.closeIcon = closeIcon;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
    }

    @Override
    public void onClick(View view) {
        backdropShown = !backdropShown;

        // Cancel the existing animations
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();

        if (!(view instanceof ImageView)) {
            throw new IllegalArgumentException("updateIcon() must be called on an ImageView");
        }
        updateIcon((ImageView) view);
        final int translateY = (int) RetroUtil.convertDpToPixel(RetroUtil.isLandscape(view.getResources()) ? 3 * 48 : 5 * 48, view.getContext());

        ObjectAnimator animator = ObjectAnimator.ofFloat(sheet, "translationY", backdropShown ? translateY : 0);
        animator.setDuration(500);
        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }
        animatorSet.play(animator);
        animator.start();
    }

    private void updateIcon(ImageView view) {
        if (openIcon != null && closeIcon != null) {
            view.setImageTintList(ColorStateList.valueOf(ATHUtil.resolveColor(context, R.attr.iconColor)));
            if (backdropShown) {
                view.setImageDrawable(closeIcon);
            } else {
                view.setImageDrawable(openIcon);
            }
        }
    }
}
