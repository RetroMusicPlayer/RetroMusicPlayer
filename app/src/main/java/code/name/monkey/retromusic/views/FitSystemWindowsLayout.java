/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

public class FitSystemWindowsLayout extends FrameLayout {
    private boolean mFit = false;

    public FitSystemWindowsLayout(final Context context) {
        super(context);
    }

    public FitSystemWindowsLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public FitSystemWindowsLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isFit() {
        return mFit;
    }

    public void setFit(final boolean fit) {
        if (mFit == fit) {
            return;
        }

        mFit = fit;
        requestApplyInsets();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean fitSystemWindows(final Rect insets) {
        if (mFit) {
            setPadding(
                    insets.left,
                    insets.top,
                    insets.right,
                    insets.bottom
            );
            return true;
        } else {
            setPadding(0, 0, 0, 0);
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(final WindowInsets insets) {
        if (mFit) {
            setPadding(
                    insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom()
            );
            return insets.consumeSystemWindowInsets();
        } else {
            setPadding(0, 0, 0, 0);
            return insets;
        }
    }
}