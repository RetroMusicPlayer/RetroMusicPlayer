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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;

import code.name.monkey.retromusic.R;


public class PlayPauseDrawable extends Drawable {
    private static final long PLAY_PAUSE_ANIMATION_DURATION = 250;

    private static final Property<PlayPauseDrawable, Float> PROGRESS =
            new Property<PlayPauseDrawable, Float>(Float.class, "progress") {
                @Override
                public Float get(@NonNull PlayPauseDrawable d) {
                    return d.getProgress();
                }

                @Override
                public void set(@NonNull PlayPauseDrawable d, Float value) {
                    d.setProgress(value);
                }
            };

    private final Path leftPauseBar = new Path();
    private final Path rightPauseBar = new Path();
    private final Paint paint = new Paint();
    private final float pauseBarWidth;
    private final float pauseBarHeight;
    private final float pauseBarDistance;

    private float width;
    private float height;

    private float progress;
    private boolean isPlay;
    private boolean isPlaySet;

    private Animator animator;

    public PlayPauseDrawable(@NonNull Context context) {
        final Resources res = context.getResources();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        pauseBarWidth = res.getDimensionPixelSize(R.dimen.pause_bar_width);
        pauseBarHeight = res.getDimensionPixelSize(R.dimen.pause_bar_height);
        pauseBarDistance = res.getDimensionPixelSize(R.dimen.pause_bar_distance);
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    @Override
    protected void onBoundsChange(@NonNull final Rect bounds) {
        super.onBoundsChange(bounds);
        if (bounds.width() > 0 && bounds.height() > 0) {
            width = bounds.width();
            height = bounds.height();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        leftPauseBar.rewind();
        rightPauseBar.rewind();

        // The current distance between the two pause bars.
        final float barDist = lerp(pauseBarDistance, 0f, progress);
        // The current width of each pause bar.
        float rawBarWidth = lerp(pauseBarWidth, pauseBarHeight / 1.75f, progress);
        // We have to round the bar width when finishing the progress to prevent the gap
        // that might occur onDraw because of a pixel is lost when casting float to int instead of rounding it.
        final float barWidth = progress == 1f ? Math.round(rawBarWidth) : rawBarWidth;
        // The current position of the left pause bar's top left coordinate.
        final float firstBarTopLeft = lerp(0f, barWidth, progress);
        // The current position of the right pause bar's top right coordinate.
        final float secondBarTopRight = lerp(2f * barWidth + barDist, barWidth + barDist, progress);

        // Draw the left pause bar. The left pause bar transforms into the
        // top half of the play button triangle by animating the position of the
        // rectangle's top left coordinate and expanding its bottom width.
        leftPauseBar.moveTo(0f, 0f);
        leftPauseBar.lineTo(firstBarTopLeft, -pauseBarHeight);
        leftPauseBar.lineTo(barWidth, -pauseBarHeight);
        leftPauseBar.lineTo(barWidth, 0f);
        leftPauseBar.close();

        // Draw the right pause bar. The right pause bar transforms into the
        // bottom half of the play button triangle by animating the position of the
        // rectangle's top right coordinate and expanding its bottom width.
        rightPauseBar.moveTo(barWidth + barDist, 0f);
        rightPauseBar.lineTo(barWidth + barDist, -pauseBarHeight);
        rightPauseBar.lineTo(secondBarTopRight, -pauseBarHeight);
        rightPauseBar.lineTo(2 * barWidth + barDist, 0f);
        rightPauseBar.close();

        final int saveCount = canvas.save();

        // Translate the play button a tiny bit to the right so it looks more centered.
        canvas.translate(lerp(0f, pauseBarHeight / 8f, progress), 0f);

        // (1) Pause --> Play: rotate 0 to 90 degrees clockwise.
        // (2) Play --> Pause: rotate 90 to 180 degrees clockwise.
        final float rotationProgress = isPlay ? 1f - progress : progress;
        final float startingRotation = isPlay ? 90f : 0f;
        canvas.rotate(lerp(startingRotation, startingRotation + 90f, rotationProgress), width / 2f, height / 2f);

        // Position the pause/play button in the center of the drawable's bounds.
        canvas.translate(Math.round(width / 2f - ((2f * barWidth + barDist) / 2f)), Math.round(height / 2f + (pauseBarHeight / 2f)));

        // Draw the two bars that form the animated pause/play button.
        canvas.drawPath(leftPauseBar, paint);
        canvas.drawPath(rightPauseBar, paint);

        canvas.restoreToCount(saveCount);
    }

    @NonNull
    private Animator getPausePlayAnimator() {
        isPlaySet = !isPlaySet;
        final Animator anim = ObjectAnimator.ofFloat(this, PROGRESS, isPlay ? 1f : 0f, isPlay ? 0f : 1f);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPlay = !isPlay;
            }
        });
        return anim;
    }

    private float getProgress() {
        return progress;
    }

    private void setProgress(float progress) {
        this.progress = progress;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setPlay(boolean animate) {
        if (animate) {
            if (!isPlaySet) {
                togglePlayPause();
            }
        } else {
            isPlaySet = true;
            isPlay = true;
            setProgress(1f);
        }
    }

    public void setPause(boolean animate) {
        if (animate) {
            if (isPlaySet) {
                togglePlayPause();
            }
        } else {
            isPlaySet = false;
            isPlay = false;
            setProgress(0f);
        }
    }

    public void togglePlayPause() {
        if (animator != null) {
            animator.cancel();
        }

        animator = getPausePlayAnimator();
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
        animator.start();
    }
}
