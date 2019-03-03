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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * A utility class to colorize bitmaps with a color gradient and a special blending mode
 */
public class ImageGradientColorizer {

    public Bitmap colorize(Drawable drawable, int backgroundColor, boolean isRtl) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        int size = Math.min(width, height);
        int widthInset = (width - size) / 2;
        int heightInset = (height - size) / 2;
        drawable = drawable.mutate();
        drawable.setBounds(-widthInset, -heightInset, width - widthInset, height - heightInset);
        Bitmap newBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        // Values to calculate the luminance of a color
        float lr = 0.2126f;
        float lg = 0.7152f;
        float lb = 0.0722f;
        // Extract the red, green, blue components of the color extraction color in
        // float and int form
        int tri = Color.red(backgroundColor);
        int tgi = Color.green(backgroundColor);
        int tbi = Color.blue(backgroundColor);
        float tr = tri / 255f;
        float tg = tgi / 255f;
        float tb = tbi / 255f;
        // Calculate the luminance of the color extraction color
        float cLum = (tr * lr + tg * lg + tb * lb) * 255;
        ColorMatrix m = new ColorMatrix(new float[]{
                lr, lg, lb, 0, tri - cLum,
                lr, lg, lb, 0, tgi - cLum,
                lr, lg, lb, 0, tbi - cLum,
                0, 0, 0, 1, 0,
        });
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LinearGradient linearGradient = new LinearGradient(0, 0, size, 0,
                new int[]{0, Color.argb(0.5f, 1, 1, 1), Color.BLACK},
                new float[]{0.0f, 0.4f, 1.0f}, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        Bitmap fadeIn = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas fadeInCanvas = new Canvas(fadeIn);
        drawable.clearColorFilter();
        drawable.draw(fadeInCanvas);
        if (isRtl) {
            // Let's flip the gradient
            fadeInCanvas.translate(size, 0);
            fadeInCanvas.scale(-1, 1);
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        fadeInCanvas.drawPaint(paint);
        Paint coloredPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coloredPaint.setColorFilter(new ColorMatrixColorFilter(m));
        coloredPaint.setAlpha((int) (0.5f * 255));
        canvas.drawBitmap(fadeIn, 0, 0, coloredPaint);
        linearGradient = new LinearGradient(0, 0, size, 0,
                new int[]{0, Color.argb(0.5f, 1, 1, 1), Color.BLACK},
                new float[]{0.0f, 0.6f, 1.0f}, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        fadeInCanvas.drawPaint(paint);
        canvas.drawBitmap(fadeIn, 0, 0, null);
        return newBitmap;
    }
}