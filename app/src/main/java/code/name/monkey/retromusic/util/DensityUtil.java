package code.name.monkey.retromusic.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by hefuyi on 16/7/30.
 */
public class DensityUtil {

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int dip2px(Context context, float dpVale) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourcesId);
        return height;
    }

    /**
     * Converts sp to px
     *
     * @param context Context
     * @param sp  the value in sp
     * @return int
     */
    public static int dip2sp(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}