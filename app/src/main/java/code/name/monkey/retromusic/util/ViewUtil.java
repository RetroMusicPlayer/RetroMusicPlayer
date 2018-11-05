package code.name.monkey.retromusic.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.core.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;

public class ViewUtil {

    public final static int RETRO_MUSIC_ANIM_TIME = 1000;

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
                MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(accentColor)));
        recyclerView.setThumbColor(accentColor);
        recyclerView.setTrackColor(Color.TRANSPARENT);
        //recyclerView.setTrackColor(ColorUtil.withAlpha(ATHUtil.resolveColor(context, R.attr.colorControlNormal), 0.12f));
    }

    public static float convertDpToPixel(float dp, Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * metrics.density;
    }
}