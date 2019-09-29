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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.retromusic.R;

/**
 * Created by hemanths on 3/23/19
 */
public class OptionMenuItemView extends FrameLayout {

    TextView textView;
    AppCompatImageView iconImageView;

    public OptionMenuItemView(@NonNull Context context) {
        this(context, null);
    }

    public OptionMenuItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public OptionMenuItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public OptionMenuItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        int accentColor = ThemeStore.Companion.accentColor(context);
        setBackground(ContextCompat.getDrawable(context, R.drawable.menu_item_background));

        setClickable(true);
        setFocusable(true);

        inflate(context, R.layout.item_option_menu, this);

        setBackgroundTintList(ColorStateList.valueOf(ColorUtil.INSTANCE.adjustAlpha(accentColor, 0.22f)));

        textView = findViewById(R.id.title);
        iconImageView = findViewById(R.id.icon);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.OptionMenuItemView, 0, 0);

        String title = attributes.getString(R.styleable.OptionMenuItemView_optionTitle);
        textView.setText(title);

        Drawable icon = attributes.getDrawable(R.styleable.OptionMenuItemView_optionIcon);
        iconImageView.setImageDrawable(icon);

        attributes.recycle();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            int accentColor = ThemeStore.Companion.accentColor(getContext());
            textView.setTextColor(accentColor);
            iconImageView.setImageTintList(ColorStateList.valueOf(accentColor));
        }
    }
}