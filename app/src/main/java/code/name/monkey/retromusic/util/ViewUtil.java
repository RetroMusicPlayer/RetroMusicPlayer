package code.name.monkey.retromusic.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;

public class ViewUtil {

    public final static int RETRO_MUSIC_ANIM_TIME = 1000;

    public static Animator createTextColorTransition(final TextView v, @ColorInt final int startColor, @ColorInt final int endColor) {
        return createColorAnimator(v, "textColor", startColor, endColor);
    }

    private static Animator createColorAnimator(Object target, String propertyName, @ColorInt int startColor, @ColorInt int endColor) {
        ObjectAnimator animator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator = ObjectAnimator.ofArgb(target, propertyName, startColor, endColor);
        } else {
            animator = ObjectAnimator.ofInt(target, propertyName, startColor, endColor);
            animator.setEvaluator(new ArgbEvaluator());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator.setInterpolator(new PathInterpolator(0.4f, 0f, 1f, 1f));
        }
        animator.setDuration(RETRO_MUSIC_ANIM_TIME);
        return animator;
    }

    public static void setStatusBarHeight(final Context context, View statusBar) {
        ViewGroup.LayoutParams lp = statusBar.getLayoutParams();
        lp.height = getStatusBarHeight(context);
        statusBar.requestLayout();
    }

    public static int getStatusBarHeight(final Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean hitTest(View v, int x, int y) {
        final int tx = (int) (ViewCompat.getTranslationX(v) + 0.5f);
        final int ty = (int) (ViewCompat.getTranslationY(v) + 0.5f);
        final int left = v.getLeft() + tx;
        final int right = v.getRight() + tx;
        final int top = v.getTop() + ty;
        final int bottom = v.getBottom() + ty;

        return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
    }

    public static void setUpFastScrollRecyclerViewColor(Context context,
                                                        FastScrollRecyclerView recyclerView, int accentColor) {
        recyclerView.setPopupBgColor(accentColor);
        recyclerView.setPopupTextColor(
                MaterialValueHelper.INSTANCE.getPrimaryTextColor(context, ColorUtil.INSTANCE.isColorLight(accentColor)));
        recyclerView.setThumbColor(accentColor);
        recyclerView.setTrackColor(Color.TRANSPARENT);
        //recyclerView.setTrackColor(ColorUtil.withAlpha(ATHUtil.resolveColor(context, R.attr.colorControlNormal), 0.12f));
    }

    public static float convertDpToPixel(float dp, Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * metrics.density;
    }

    @NotNull
    public static Animator createBackgroundColorTransition(@Nullable View colorGradientBackground, int lastColor, int newColor) {
        return null;
    }
}