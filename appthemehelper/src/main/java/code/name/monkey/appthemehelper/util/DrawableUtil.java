package code.name.monkey.appthemehelper.util;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.ColorInt;


public final class DrawableUtil {

    public static TransitionDrawable createTransitionDrawable(@ColorInt int startColor, @ColorInt int endColor) {
        return createTransitionDrawable(new ColorDrawable(startColor), new ColorDrawable(endColor));
    }

    public static TransitionDrawable createTransitionDrawable(Drawable start, Drawable end) {
        final Drawable[] drawables = new Drawable[2];

        drawables[0] = start;
        drawables[1] = end;

        return new TransitionDrawable(drawables);
    }

    private DrawableUtil() {
    }
}
