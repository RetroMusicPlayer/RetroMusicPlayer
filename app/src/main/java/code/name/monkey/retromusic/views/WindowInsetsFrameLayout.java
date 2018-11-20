/*
 * Mantou Earth - Live your wallpaper with live earth
 * Copyright (C) 2015  XiNGRZ <xxx@oxo.ooo>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package code.name.monkey.retromusic.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WindowInsetsFrameLayout extends FrameLayout {

    private static final String TAG = "WindowInsetsFrameLayout";

    public WindowInsetsFrameLayout(Context context) {
        this(context, null);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewCompat.setOnApplyWindowInsetsListener(this, (v, insets) ->
                applySystemWindowInsets21(insets) ? insets.consumeSystemWindowInsets() : insets);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            return applySystemWindowInsets19(insets);
        }

        return super.fitSystemWindows(insets);
    }

    @TargetApi(19)
    private boolean applySystemWindowInsets19(Rect insets) {
        boolean consumed = false;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (!child.getFitsSystemWindows()) {
                continue;
            }

            Rect childInsets = new Rect(insets);

            computeInsetsWithGravity(child, childInsets);

            child.setPadding(childInsets.left, childInsets.top, childInsets.right, childInsets.bottom);

            consumed = true;
        }

        return consumed;
    }

    @TargetApi(21)
    private boolean applySystemWindowInsets21(WindowInsetsCompat insets) {
        boolean consumed = false;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (!child.getFitsSystemWindows()) {
                continue;
            }

            Rect childInsets = new Rect(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom());

            computeInsetsWithGravity(child, childInsets);

            ViewCompat.dispatchApplyWindowInsets(child, insets.replaceSystemWindowInsets(childInsets));

            consumed = true;
        }

        return consumed;
    }

    @SuppressLint("RtlHardcoded")
    private void computeInsetsWithGravity(View view, Rect insets) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();

        int gravity = GravityCompat.getAbsoluteGravity(
                lp.gravity, ViewCompat.getLayoutDirection(view));

        if (lp.width != LayoutParams.MATCH_PARENT) {
            if ((gravity & Gravity.LEFT) != Gravity.LEFT) {
                insets.left = 0;
            }

            if ((gravity & Gravity.RIGHT) != Gravity.RIGHT) {
                insets.right = 0;
            }
        }

        if (lp.height != LayoutParams.MATCH_PARENT) {
            if ((gravity & Gravity.TOP) != Gravity.TOP) {
                insets.top = 0;
            }

            if ((gravity & Gravity.BOTTOM) != Gravity.BOTTOM) {
                insets.bottom = 0;
            }
        }
    }

}