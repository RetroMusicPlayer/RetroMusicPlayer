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

package code.name.monkey.retromusic.util.color;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.util.Pair;

import java.util.WeakHashMap;

import code.name.monkey.retromusic.util.ImageUtil;

/**
 * Helper class to process legacy (Holo) notifications to make them look like quantum
 * notifications.
 *
 * @hide
 */
public class NotificationColorUtil {

    private static final String TAG = "NotificationColorUtil";
    private static final Object sLock = new Object();
    private static NotificationColorUtil sInstance;

    private final WeakHashMap<Bitmap, Pair<Boolean, Integer>> mGrayscaleBitmapCache =
            new WeakHashMap<Bitmap, Pair<Boolean, Integer>>();

    public static NotificationColorUtil getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new NotificationColorUtil();
            }
            return sInstance;
        }
    }

    /**
     * Checks whether a bitmap is grayscale. Grayscale here means "very close to a perfect gray".
     *
     * @param bitmap The bitmap to test.
     * @return Whether the bitmap is grayscale.
     */
    public boolean isGrayscale(Bitmap bitmap) {
        synchronized (sLock) {
            Pair<Boolean, Integer> cached = mGrayscaleBitmapCache.get(bitmap);
            if (cached != null) {
                if (cached.second == bitmap.getGenerationId()) {
                    return cached.first;
                }
            }
        }
        boolean result;
        int generationId;

        result = ImageUtil.isGrayscale(bitmap);
        // generationId and the check whether the Bitmap is grayscale can't be read atomically
        // here. However, since the thread is in the process of posting the notification, we can
        // assume that it doesn't modify the bitmap while we are checking the pixels.
        generationId = bitmap.getGenerationId();

        synchronized (sLock) {
            mGrayscaleBitmapCache.put(bitmap, Pair.create(result, generationId));
        }
        return result;
    }

    /**
     * Checks whether a drawable is grayscale. Grayscale here means "very close to a perfect gray".
     *
     * @param d The drawable to test.
     * @return Whether the drawable is grayscale.
     */
    public boolean isGrayscale(Drawable d) {
        if (d == null) {
            return false;
        } else if (d instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) d;
            return bd.getBitmap() != null && isGrayscale(bd.getBitmap());
        } else if (d instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) d;
            int count = ad.getNumberOfFrames();
            return count > 0 && isGrayscale(ad.getFrame(0));
        } else if (d instanceof VectorDrawable) {
            // We just assume you're doing the right thing if using vectors
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether a drawable with a resoure id is grayscale. Grayscale here means "very close to a
     * perfect gray".
     *
     * @param context The context to load the drawable from.
     * @return Whether the drawable is grayscale.
     */
    public boolean isGrayscale(Context context, int drawableResId) {
        if (drawableResId != 0) {
            try {
                return isGrayscale(context.getDrawable(drawableResId));
            } catch (Resources.NotFoundException ex) {
                Log.e(TAG, "Drawable not found: " + drawableResId);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Inverts all the grayscale colors set by {@link android.text.style.TextAppearanceSpan}s on the
     * text.
     *
     * @param charSequence The text to process.
     * @return The color inverted text.
     */


    private int processColor(int color) {
        return Color.argb(Color.alpha(color),
                255 - Color.red(color),
                255 - Color.green(color),
                255 - Color.blue(color));
    }
}