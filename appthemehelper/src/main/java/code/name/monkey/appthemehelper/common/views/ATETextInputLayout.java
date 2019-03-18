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

package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.appthemehelper.ThemeStore;

/**
 * Created by hemanths on 3/12/19
 */
public class ATETextInputLayout extends TextInputLayout {
    public ATETextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    public ATETextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ATETextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE);
        setBoxStrokeColor(ThemeStore.Companion.textColorSecondary(context));
        final float scale = context.getResources().getDisplayMetrics().density;
        int border = (int) (8 * scale + 0.5f);
        setBoxCornerRadii(border, border, border, border);
        setHintAnimationEnabled(true);
        setHintEnabled(true);

    }
}
