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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.retromusic.R;

public class CollapsingFAB extends FrameLayout {
    @ColorInt
    int color = Color.WHITE;

    String title;
    Drawable icon;

    boolean showTitle;

    ImageView shuffleIcon;
    TextView textView;
    MaterialCardView cardView;

    public CollapsingFAB(@NonNull Context context) {
        this(context, null);
    }

    public CollapsingFAB(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingFAB(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CollapsingFAB, 0, 0);
        icon = attributes.getDrawable(R.styleable.CollapsingFAB_setIcon);
        color = attributes.getColor(R.styleable.CollapsingFAB_shuffleBackgroundColor, 0);
        showTitle = attributes.getBoolean(R.styleable.CollapsingFAB_showTitle, false);
        title = attributes.getString(R.styleable.CollapsingFAB_setText);

        View view = inflate(context, R.layout.collapsing_floating_action_button, this);
        shuffleIcon = view.findViewById(R.id.icon);
        shuffleIcon.setImageDrawable(icon);

        textView = view.findViewById(R.id.shuffle_text);
        textView.setText(title);
        textView.setVisibility(showTitle ? VISIBLE : GONE);
        cardView = view.findViewById(R.id.container);
        attributes.recycle();
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        TransitionManager.beginDelayedTransition(this, new AutoTransition());
        textView.setVisibility(showTitle ? VISIBLE : GONE);
        invalidate();
        requestLayout();
    }

    public void setColor(int color) {
        this.color = color;
        int textColor = MaterialValueHelper.INSTANCE.getPrimaryTextColor(getContext(), ColorUtil.INSTANCE.isColorLight(color));
        shuffleIcon.setColorFilter(textColor);
        textView.setTextColor(textColor);
        cardView.setCardBackgroundColor(ColorStateList.valueOf(color));
        postInvalidate();
    }
}
