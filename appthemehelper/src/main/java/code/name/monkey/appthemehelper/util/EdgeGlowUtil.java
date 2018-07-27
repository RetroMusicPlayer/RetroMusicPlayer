package code.name.monkey.appthemehelper.util;

import android.annotation.TargetApi;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import code.name.monkey.appthemehelper.BuildConfig;

import java.lang.reflect.Field;

public class EdgeGlowUtil {

    protected EdgeGlowUtil() {
    }

    // Invalidation methods

    private static Field EDGE_GLOW_FIELD_EDGE;
    private static Field EDGE_GLOW_FIELD_GLOW;
    private static Field EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT;

    private static void invalidateEdgeEffectFields() {
        if (EDGE_GLOW_FIELD_EDGE != null && EDGE_GLOW_FIELD_GLOW != null &&
                EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT != null) {
            EDGE_GLOW_FIELD_EDGE.setAccessible(true);
            EDGE_GLOW_FIELD_GLOW.setAccessible(true);
            EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true);
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Field edge = null, glow = null;
            Class cls = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                try {
                    cls = Class.forName("android.widget.EdgeGlow");
                } catch (ClassNotFoundException e) {
                    if (BuildConfig.DEBUG) e.printStackTrace();
                }
            } else {
                cls = EdgeEffect.class;
            }
            if (cls != null) {
                for (Field f : cls.getDeclaredFields()) {
                    switch (f.getName()) {
                        case "mEdge":
                            f.setAccessible(true);
                            edge = f;
                            break;
                        case "mGlow":
                            f.setAccessible(true);
                            glow = f;
                            break;
                    }
                }
            }
            EDGE_GLOW_FIELD_EDGE = edge;
            EDGE_GLOW_FIELD_GLOW = glow;
        } else {
            EDGE_GLOW_FIELD_EDGE = null;
            EDGE_GLOW_FIELD_GLOW = null;
        }

        Field efc = null;
        try {
            efc = EdgeEffectCompat.class.getDeclaredField("mEdgeEffect");
        } catch (NoSuchFieldException e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        }
        EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT = efc;
    }

    private static Field SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    private static Field SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static void invalidateScrollViewFields() {
        if (SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null && SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);
            return;
        }
        final Class<?> cls = ScrollView.class;
        for (Field f : cls.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = f;
                    break;
            }
        }
    }

    private static Field NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP;
    private static Field NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static void invalidateNestedScrollViewFields() {
        if (NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP != null && NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);
            return;
        }
        final Class<?> cls = ATHUtil.inClassPath("android.support.v4.widget.NestedScrollView");
        for (Field f : cls.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM = f;
                    break;
            }
        }
    }

    private static Field LIST_VIEW_FIELD_EDGE_GLOW_TOP;
    private static Field LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static void invalidateListViewFields() {
        if (LIST_VIEW_FIELD_EDGE_GLOW_TOP != null && LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            LIST_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);
            return;
        }
        final Class<?> cls = AbsListView.class;
        for (Field f : cls.getDeclaredFields()) {
            switch (f.getName()) {
                case "mEdgeGlowTop":
                    f.setAccessible(true);
                    LIST_VIEW_FIELD_EDGE_GLOW_TOP = f;
                    break;
                case "mEdgeGlowBottom":
                    f.setAccessible(true);
                    LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM = f;
                    break;
            }
        }
    }

    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP;
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT;
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT;
    private static Field RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM;

    private static void invalidateRecyclerViewFields() {
        if (RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP != null && RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT != null &&
                RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT != null && RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM != null) {
            RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.setAccessible(true);
            RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.setAccessible(true);
            RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.setAccessible(true);
            RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.setAccessible(true);
            return;
        }
        final Class<?> cls = ATHUtil.inClassPath("android.support.v7.widget.RecyclerView");
        for (Field f : cls.getDeclaredFields()) {
            switch (f.getName()) {
                case "mTopGlow":
                    f.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP = f;
                    break;
                case "mBottomGlow":
                    f.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM = f;
                    break;
                case "mLeftGlow":
                    f.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT = f;
                    break;
                case "mRightGlow":
                    f.setAccessible(true);
                    RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT = f;
                    break;
            }
        }
    }

    private static Field VIEW_PAGER_FIELD_EDGE_GLOW_LEFT;
    private static Field VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT;

    private static void invalidateViewPagerFields() {
        if (VIEW_PAGER_FIELD_EDGE_GLOW_LEFT != null && VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT != null) {
            VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.setAccessible(true);
            VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.setAccessible(true);
            return;
        }
        final Class<?> cls = ATHUtil.inClassPath("android.support.v4.view.ViewPager");
        for (Field f : cls.getDeclaredFields()) {
            switch (f.getName()) {
                case "mLeftEdge":
                    f.setAccessible(true);
                    VIEW_PAGER_FIELD_EDGE_GLOW_LEFT = f;
                    break;
                case "mRightEdge":
                    f.setAccessible(true);
                    VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT = f;
                    break;
            }
        }
    }

    // Setter methods

    public static void setEdgeGlowColor(@NonNull ScrollView scrollView, @ColorInt int color) {
        invalidateScrollViewFields();
        try {
            Object ee;
            ee = SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
            setEffectColor(ee, color);
            ee = SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
            setEffectColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    public static void setEdgeGlowColor(@NonNull NestedScrollView scrollView, @ColorInt int color) {
        invalidateNestedScrollViewFields();
        try {
            Object ee;
            ee = NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
            setEffectColor(ee, color);
            ee = NESTED_SCROLL_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
            setEffectColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    public static void setEdgeGlowColor(@NonNull AbsListView listView, @ColorInt int color) {
        invalidateListViewFields();
        try {
            Object ee;
            ee = LIST_VIEW_FIELD_EDGE_GLOW_TOP.get(listView);
            setEffectColor(ee, color);
            ee = LIST_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(listView);
            setEffectColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    public static void setEdgeGlowColor(@NonNull RecyclerView scrollView, final @ColorInt int color, @Nullable RecyclerView.OnScrollListener scrollListener) {
        invalidateRecyclerViewFields();
        invalidateRecyclerViewFields();
        if (scrollListener == null) {
            scrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    EdgeGlowUtil.setEdgeGlowColor(recyclerView, color, this);
                }
            };
            scrollView.addOnScrollListener(scrollListener);
        }
        try {
            Object ee;
            ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_TOP.get(scrollView);
            setEffectColor(ee, color);
            ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_BOTTOM.get(scrollView);
            setEffectColor(ee, color);
            ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_LEFT.get(scrollView);
            setEffectColor(ee, color);
            ee = RECYCLER_VIEW_FIELD_EDGE_GLOW_RIGHT.get(scrollView);
            setEffectColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    public static void setEdgeGlowColor(@NonNull ViewPager pager, @ColorInt int color) {
        invalidateViewPagerFields();
        try {
            Object ee;
            ee = VIEW_PAGER_FIELD_EDGE_GLOW_LEFT.get(pager);
            setEffectColor(ee, color);
            ee = VIEW_PAGER_FIELD_EDGE_GLOW_RIGHT.get(pager);
            setEffectColor(ee, color);
        } catch (Exception ex) {
            if (BuildConfig.DEBUG) ex.printStackTrace();
        }
    }

    // Utilities

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setEffectColor(Object edgeEffect, @ColorInt int color) {
        invalidateEdgeEffectFields();
        if (edgeEffect instanceof EdgeEffectCompat) {
            // EdgeEffectCompat
            try {
                EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.setAccessible(true);
                edgeEffect = EDGE_EFFECT_COMPAT_FIELD_EDGE_EFFECT.get(edgeEffect);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
        if (edgeEffect == null)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // EdgeGlow
            try {
                EDGE_GLOW_FIELD_EDGE.setAccessible(true);
                final Drawable mEdge = (Drawable) EDGE_GLOW_FIELD_EDGE.get(edgeEffect);
                EDGE_GLOW_FIELD_GLOW.setAccessible(true);
                final Drawable mGlow = (Drawable) EDGE_GLOW_FIELD_GLOW.get(edgeEffect);
                mEdge.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mGlow.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                mEdge.setCallback(null); // free up any references
                mGlow.setCallback(null); // free up any references
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // EdgeEffect
            ((EdgeEffect) edgeEffect).setColor(color);
        }
    }
}
