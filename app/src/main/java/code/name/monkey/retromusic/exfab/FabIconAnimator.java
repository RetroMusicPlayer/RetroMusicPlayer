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

package code.name.monkey.retromusic.exfab;

/**
 * Created by hemanths on 3/20/19
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import code.name.monkey.retromusic.R;

public class FabIconAnimator {

    private static final String ROTATION_Y_PROPERTY = "rotationY";

    private static final float TWITCH_END = 20F;
    private static final float TWITCH_START = 0F;
    private static final int DURATION = 200;
    private final MaterialButton button;
    private final ConstraintLayout container;
    @DrawableRes
    private int currentIcon;
    @StringRes
    private int currentText;
    private boolean isAnimating;
    private final Transition.TransitionListener listener = new Transition.TransitionListener() {
        public void onTransitionStart(Transition transition) {
            isAnimating = true;
        }

        public void onTransitionEnd(Transition transition) {
            isAnimating = false;
        }

        public void onTransitionCancel(Transition transition) {
            isAnimating = false;
        }

        public void onTransitionPause(Transition transition) {
        }

        public void onTransitionResume(Transition transition) {
        }
    };

    public FabIconAnimator(ConstraintLayout container) {
        this.container = container;
        this.button = container.findViewById(R.id.fab);
    }

    public void update(@DrawableRes int icon, @StringRes int text) {
        boolean isSame = currentIcon == icon && currentText == text;
        currentIcon = icon;
        currentText = text;
        animateChange(icon, text, isSame);
    }

    public void setOnClickListener(@Nullable View.OnClickListener clickListener) {
        if (clickListener == null) {
            button.setOnClickListener(null);
            return;
        }
        AtomicBoolean flag = new AtomicBoolean(true);
        button.setOnClickListener(view -> {
            if (!flag.getAndSet(false)) return;
            clickListener.onClick(view);
            button.postDelayed(() -> flag.set(true), 2000);
        });
    }

    private boolean isExtended() { // R.dimen.triple_and_half_margin is 56 dp.
        return button.getLayoutParams().height != button.getResources().getDimensionPixelSize(R.dimen.triple_and_half_margin);
    }

    public void setExtended(boolean extended) {
        setExtended(extended, false);
    }

    private void animateChange(@DrawableRes int icon, @StringRes int text, boolean isSame) {
        boolean extended = isExtended();
        button.setText(text);
        button.setIconResource(icon);
        setExtended(extended, !isSame);
        if (!extended) twitch();
    }

    private void setExtended(boolean extended, boolean force) {
        if (isAnimating || (extended && isExtended() && !force)) return;

        ConstraintSet set = new ConstraintSet();
        set.clone(container.getContext(), extended ? R.layout.fab_extended : R.layout.fab_collapsed);

        TransitionManager.beginDelayedTransition(container, new AutoTransition()
                .addListener(listener).setDuration(150));

        if (extended) button.setText(currentText);
        else button.setText("");

        set.applyTo(container);
    }

    private void twitch() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator twitchA = animateProperty(ROTATION_Y_PROPERTY, TWITCH_START, TWITCH_END);
        ObjectAnimator twitchB = animateProperty(ROTATION_Y_PROPERTY, TWITCH_END, TWITCH_START);

        set.play(twitchB).after(twitchA);
        set.start();
    }

    @NonNull
    private ObjectAnimator animateProperty(String property, float start, float end) {
        return ObjectAnimator.ofFloat(container, property, start, end).setDuration(DURATION);
    }
}