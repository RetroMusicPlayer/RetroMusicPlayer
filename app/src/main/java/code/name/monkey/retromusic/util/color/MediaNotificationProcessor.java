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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.VisibleForTesting;
import androidx.palette.graphics.Palette;

import java.util.List;

import code.name.monkey.appthemehelper.util.ColorUtil;

/**
 * @author Hemanth S (h4h13).
 */
public class MediaNotificationProcessor {

    /**
     * The fraction below which we select the vibrant instead of the light/dark vibrant color
     */
    private static final float POPULATION_FRACTION_FOR_MORE_VIBRANT = 1.0f;
    /**
     * Minimum saturation that a muted color must have if there exists if deciding between two
     * colors
     */
    private static final float MIN_SATURATION_WHEN_DECIDING = 0.19f;
    /**
     * Minimum fraction that any color must have to be picked up as a text color
     */
    private static final double MINIMUM_IMAGE_FRACTION = 0.002;
    /**
     * The population fraction to select the dominant color as the text color over a the colored
     * ones.
     */
    private static final float POPULATION_FRACTION_FOR_DOMINANT = 0.01f;
    /**
     * The population fraction to select a white or black color as the background over a color.
     */
    private static final float POPULATION_FRACTION_FOR_WHITE_OR_BLACK = 2.5f;
    private static final float BLACK_MAX_LIGHTNESS = 0.08f;
    private static final float WHITE_MIN_LIGHTNESS = 0.90f;
    private static final int RESIZE_BITMAP_AREA = 150 * 150;
    private final ImageGradientColorizer mColorizer;
    private final Context mContext;
    /**
     * The context of the notification. This is the app context of the package posting the
     * notification.
     */
    private final Context mPackageContext;
    private float[] mFilteredBackgroundHsl = null;
    private Palette.Filter mBlackWhiteFilter = (rgb, hsl) -> !isWhiteOrBlack(hsl);
    private boolean mIsLowPriority;
    private onColorThing onColorThing;

    public MediaNotificationProcessor(Context context, Context packageContext, onColorThing thing) {
        this(context, packageContext, new ImageGradientColorizer());
        onColorThing = thing;
    }

    @VisibleForTesting
    MediaNotificationProcessor(Context context, Context packageContext,
                               ImageGradientColorizer colorizer) {
        mContext = context;
        mPackageContext = packageContext;
        mColorizer = colorizer;
    }

    /**
     * Processes a builder of a media notification and calculates the appropriate colors that should
     * be used.
     *
     * @param notification the notification that is being processed
     * @param builder      the recovered builder for the notification. this will be modified
     */
    public int processNotification(Bitmap image) {
        Bitmap bitmap;
        Drawable drawable = new BitmapDrawable(mPackageContext.getResources(), image);

        int backgroundColor = 0;

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        int area = width * height;
        if (area > RESIZE_BITMAP_AREA) {
            double factor = Math.sqrt((float) RESIZE_BITMAP_AREA / area);
            width = (int) (factor * width);
            height = (int) (factor * height);
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        // for the background we only take the left side of the image to ensure
        // a smooth transition
        Palette.Builder paletteBuilder = Palette.from(bitmap)
                .setRegion(0, 0, bitmap.getWidth() / 2, bitmap.getHeight())
                .clearFilters() // we want all colors, red / white / black ones too!
                .resizeBitmapArea(RESIZE_BITMAP_AREA);
        Palette palette = paletteBuilder.generate();
        backgroundColor = findBackgroundColorAndFilter(palette);
        // we want most of the full region again, slightly shifted to the right


        float textColorStartWidthFraction = 0.4f;
        paletteBuilder.setRegion((int) (bitmap.getWidth() * textColorStartWidthFraction), 0,
                bitmap.getWidth(),
                bitmap.getHeight());
        if (mFilteredBackgroundHsl != null) {
            paletteBuilder.addFilter((rgb, hsl) -> {
                // at least 10 degrees hue difference
                float diff = Math.abs(hsl[0] - mFilteredBackgroundHsl[0]);
                return diff > 10 && diff < 350;
            });
        }
        paletteBuilder.addFilter(mBlackWhiteFilter);
        palette = paletteBuilder.generate();
        int foregroundColor = selectForegroundColor(backgroundColor, palette);

        onColorThing.bothColor(backgroundColor, foregroundColor);
        return backgroundColor;
    }

    private int selectForegroundColor(int backgroundColor, Palette palette) {
        if (ColorUtil.INSTANCE.isColorLight(backgroundColor)) {
            return selectForegroundColorForSwatches(palette.getDarkVibrantSwatch(),
                    palette.getVibrantSwatch(),
                    palette.getDarkMutedSwatch(),
                    palette.getMutedSwatch(),
                    palette.getDominantSwatch(),
                    Color.BLACK);
        } else {
            return selectForegroundColorForSwatches(palette.getLightVibrantSwatch(),
                    palette.getVibrantSwatch(),
                    palette.getLightMutedSwatch(),
                    palette.getMutedSwatch(),
                    palette.getDominantSwatch(),
                    Color.WHITE);
        }
    }

    private int selectForegroundColorForSwatches(Palette.Swatch moreVibrant,
                                                 Palette.Swatch vibrant, Palette.Swatch moreMutedSwatch, Palette.Swatch mutedSwatch,
                                                 Palette.Swatch dominantSwatch, int fallbackColor) {
        Palette.Swatch coloredCandidate = selectVibrantCandidate(moreVibrant, vibrant);
        if (coloredCandidate == null) {
            coloredCandidate = selectMutedCandidate(mutedSwatch, moreMutedSwatch);
        }
        if (coloredCandidate != null) {
            if (dominantSwatch == coloredCandidate) {
                return coloredCandidate.getRgb();
            } else if ((float) coloredCandidate.getPopulation() / dominantSwatch.getPopulation()
                    < POPULATION_FRACTION_FOR_DOMINANT
                    && dominantSwatch.getHsl()[1] > MIN_SATURATION_WHEN_DECIDING) {
                return dominantSwatch.getRgb();
            } else {
                return coloredCandidate.getRgb();
            }
        } else if (hasEnoughPopulation(dominantSwatch)) {
            return dominantSwatch.getRgb();
        } else {
            return fallbackColor;
        }
    }

    private Palette.Swatch selectMutedCandidate(Palette.Swatch first,
                                                Palette.Swatch second) {
        boolean firstValid = hasEnoughPopulation(first);
        boolean secondValid = hasEnoughPopulation(second);
        if (firstValid && secondValid) {
            float firstSaturation = first.getHsl()[1];
            float secondSaturation = second.getHsl()[1];
            float populationFraction = first.getPopulation() / (float) second.getPopulation();
            if (firstSaturation * populationFraction > secondSaturation) {
                return first;
            } else {
                return second;
            }
        } else if (firstValid) {
            return first;
        } else if (secondValid) {
            return second;
        }
        return null;
    }

    private Palette.Swatch selectVibrantCandidate(Palette.Swatch first, Palette.Swatch second) {
        boolean firstValid = hasEnoughPopulation(first);
        boolean secondValid = hasEnoughPopulation(second);
        if (firstValid && secondValid) {
            int firstPopulation = first.getPopulation();
            int secondPopulation = second.getPopulation();
            if (firstPopulation / (float) secondPopulation
                    < POPULATION_FRACTION_FOR_MORE_VIBRANT) {
                return second;
            } else {
                return first;
            }
        } else if (firstValid) {
            return first;
        } else if (secondValid) {
            return second;
        }
        return null;
    }

    private boolean hasEnoughPopulation(Palette.Swatch swatch) {
        // We want a fraction that is at least 1% of the image
        return swatch != null
                && (swatch.getPopulation() / (float) RESIZE_BITMAP_AREA > MINIMUM_IMAGE_FRACTION);
    }

    private int findBackgroundColorAndFilter(Palette palette) {
        // by default we use the dominant palette
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
        if (dominantSwatch == null) {
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null;
            return Color.WHITE;
        }
        if (!isWhiteOrBlack(dominantSwatch.getHsl())) {
            mFilteredBackgroundHsl = dominantSwatch.getHsl();
            return dominantSwatch.getRgb();
        }
        // Oh well, we selected black or white. Lets look at the second color!
        List<Palette.Swatch> swatches = palette.getSwatches();
        float highestNonWhitePopulation = -1;
        Palette.Swatch second = null;
        for (Palette.Swatch swatch : swatches) {
            if (swatch != dominantSwatch
                    && swatch.getPopulation() > highestNonWhitePopulation
                    && !isWhiteOrBlack(swatch.getHsl())) {
                second = swatch;
                highestNonWhitePopulation = swatch.getPopulation();
            }
        }
        if (second == null) {
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null;
            return dominantSwatch.getRgb();
        }
        if (dominantSwatch.getPopulation() / highestNonWhitePopulation
                > POPULATION_FRACTION_FOR_WHITE_OR_BLACK) {
            // The dominant swatch is very dominant, lets take it!
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null;
            return dominantSwatch.getRgb();
        } else {
            mFilteredBackgroundHsl = second.getHsl();
            return second.getRgb();
        }
    }

    private boolean isWhiteOrBlack(float[] hsl) {
        return isBlack(hsl) || isWhite(hsl);
    }

    /**
     * @return true if the color represents a color which is close to black.
     */
    private boolean isBlack(float[] hslColor) {
        return hslColor[2] <= BLACK_MAX_LIGHTNESS;
    }

    /**
     * @return true if the color represents a color which is close to white.
     */
    private boolean isWhite(float[] hslColor) {
        return hslColor[2] >= WHITE_MIN_LIGHTNESS;
    }

    public void setIsLowPriority(boolean isLowPriority) {
        mIsLowPriority = isLowPriority;
    }

    public interface onColorThing {
        void bothColor(int i, int i2);
    }

    /**
     * The fraction below which we select the vibrant instead of the light/dark vibrant color
     *//*
    private static final float POPULATION_FRACTION_FOR_MORE_VIBRANT = 1.0f;
    *//**
     * Minimum saturation that a muted color must have if there exists if deciding between two colors
     *//*
    private static final float MIN_SATURATION_WHEN_DECIDING = 0.19f;
    *//**
     * Minimum fraction that any color must have to be picked up as a text color
     *//*
    private static final double MINIMUM_IMAGE_FRACTION = 0.002;
    *//**
     * The population fraction to select the dominant color as the text color over a the colored
     * ones.
     *//*
    private static final float POPULATION_FRACTION_FOR_DOMINANT = 0.01f;
    *//**
     * The population fraction to select a white or black color as the background over a color.
     *//*
    private static final float POPULATION_FRACTION_FOR_WHITE_OR_BLACK = 2.5f;

    private static final float BLACK_MAX_LIGHTNESS = 0.08f;
    private static final float WHITE_MIN_LIGHTNESS = 0.90f;
    private static final int RESIZE_BITMAP_AREA = 150 * 150;
    private static float[] mFilteredBackgroundHsl = null;
    private final ImageGradientColorizer mColorizer;
    private final Context mContext;
    *//**
     * The context of the notification. This is the app context of the package posting the
     * notification.
     *//*
    private final Context mPackageContext;
    private static Palette.Filter mBlackWhiteFilter = (rgb, hsl) -> !isWhiteOrBlack(hsl);
    private boolean mIsLowPriority;

    public MediaNotificationProcessor(Context context, Context packageContext) {
        this(context, packageContext, new ImageGradientColorizer());
    }

    @VisibleForTesting
    MediaNotificationProcessor(Context context, Context packageContext,
                               ImageGradientColorizer colorizer) {
        mContext = context;
        mPackageContext = packageContext;
        mColorizer = colorizer;
    }

    @Nullable
    public static Palette.Builder generatePalette(Bitmap bitmap) {
        return bitmap == null ? null : Palette.from(bitmap).clearFilters().resizeBitmapArea(RESIZE_BITMAP_AREA);
    }

    public static int getBackgroundColor(Palette.Builder builder) {
        return findBackgroundColorAndFilter(builder.generate());
    }

    public static int getTextColor(Palette.Builder builder) {
        int backgroundColor = 0;
        if (mFilteredBackgroundHsl != null) {
            builder.addFilter((rgb, hsl) -> {
                // at least 10 degrees hue difference
                float diff = Math.abs(hsl[0] - mFilteredBackgroundHsl[0]);
                return diff > 10 && diff < 350;
            });
        }
        builder.addFilter(mBlackWhiteFilter);
        Palette palette = builder.generate();
        return selectForegroundColor(backgroundColor, palette);
    }

    private static int selectForegroundColor(int backgroundColor, Palette palette) {
        if (ColorUtil.isColorLight(backgroundColor)) {
            return selectForegroundColorForSwatches(palette.getDarkVibrantSwatch(),
                    palette.getVibrantSwatch(),
                    palette.getDarkMutedSwatch(),
                    palette.getMutedSwatch(),
                    palette.getDominantSwatch(),
                    Color.BLACK);
        } else {
            return selectForegroundColorForSwatches(palette.getLightVibrantSwatch(),
                    palette.getVibrantSwatch(),
                    palette.getLightMutedSwatch(),
                    palette.getMutedSwatch(),
                    palette.getDominantSwatch(),
                    Color.WHITE);
        }
    }

    private static int selectForegroundColorForSwatches(Palette.Swatch moreVibrant,
                                                        Palette.Swatch vibrant, Palette.Swatch moreMutedSwatch, Palette.Swatch mutedSwatch,
                                                        Palette.Swatch dominantSwatch, int fallbackColor) {
        Palette.Swatch coloredCandidate = selectVibrantCandidate(moreVibrant, vibrant);
        if (coloredCandidate == null) {
            coloredCandidate = selectMutedCandidate(mutedSwatch, moreMutedSwatch);
        }
        if (coloredCandidate != null) {
            if (dominantSwatch == coloredCandidate) {
                return coloredCandidate.getRgb();
            } else if ((float) coloredCandidate.getPopulation() / dominantSwatch.getPopulation()
                    < POPULATION_FRACTION_FOR_DOMINANT
                    && dominantSwatch.getHsl()[1] > MIN_SATURATION_WHEN_DECIDING) {
                return dominantSwatch.getRgb();
            } else {
                return coloredCandidate.getRgb();
            }
        } else if (hasEnoughPopulation(dominantSwatch)) {
            return dominantSwatch.getRgb();
        } else {
            return fallbackColor;
        }
    }

    private static Palette.Swatch selectMutedCandidate(Palette.Swatch first,
                                                       Palette.Swatch second) {
        boolean firstValid = hasEnoughPopulation(first);
        boolean secondValid = hasEnoughPopulation(second);
        if (firstValid && secondValid) {
            float firstSaturation = first.getHsl()[1];
            float secondSaturation = second.getHsl()[1];
            float populationFraction = first.getPopulation() / (float) second.getPopulation();
            if (firstSaturation * populationFraction > secondSaturation) {
                return first;
            } else {
                return second;
            }
        } else if (firstValid) {
            return first;
        } else if (secondValid) {
            return second;
        }
        return null;
    }

    private static Palette.Swatch selectVibrantCandidate(Palette.Swatch first,
                                                         Palette.Swatch second) {
        boolean firstValid = hasEnoughPopulation(first);
        boolean secondValid = hasEnoughPopulation(second);
        if (firstValid && secondValid) {
            int firstPopulation = first.getPopulation();
            int secondPopulation = second.getPopulation();
            if (firstPopulation / (float) secondPopulation
                    < POPULATION_FRACTION_FOR_MORE_VIBRANT) {
                return second;
            } else {
                return first;
            }
        } else if (firstValid) {
            return first;
        } else if (secondValid) {
            return second;
        }
        return null;
    }

    private static boolean hasEnoughPopulation(Palette.Swatch swatch) {
        // We want a fraction that is at least 1% of the image
        return swatch != null
                && (swatch.getPopulation() / (float) RESIZE_BITMAP_AREA > MINIMUM_IMAGE_FRACTION);
    }

    public static int findBackgroundColorAndFilter(Palette palette) {
        // by default we use the dominant palette
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
        if (dominantSwatch == null) {
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null;
            return Color.WHITE;
        }
        if (!isWhiteOrBlack(dominantSwatch.getHsl())) {
            mFilteredBackgroundHsl = dominantSwatch.getHsl();
            return dominantSwatch.getRgb();
        }
        // Oh well, we selected black or white. Lets look at the second color!
        List<Swatch> swatches = palette.getSwatches();
        float highestNonWhitePopulation = -1;
        Palette.Swatch second = null;
        for (Palette.Swatch swatch : swatches) {
            if (swatch != dominantSwatch
                    && swatch.getPopulation() > highestNonWhitePopulation
                    && !isWhiteOrBlack(swatch.getHsl())) {
                second = swatch;
                highestNonWhitePopulation = swatch.getPopulation();
            }
        }
        if (second == null) {
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null;
            return dominantSwatch.getRgb();
        }
        if (dominantSwatch.getPopulation() / highestNonWhitePopulation
                > POPULATION_FRACTION_FOR_WHITE_OR_BLACK) {
            // The dominant swatch is very dominant, lets take it!
            // We're not filtering on white or black
            mFilteredBackgroundHsl = null;
            return dominantSwatch.getRgb();
        } else {
            mFilteredBackgroundHsl = second.getHsl();
            return second.getRgb();
        }
    }

    private static boolean isWhiteOrBlack(float[] hsl) {
        return isBlack(hsl) || isWhite(hsl);
    }

    *//**
     * @return true if the color represents a color which is close to black.
     *//*
    private static boolean isBlack(float[] hslColor) {
        return hslColor[2] <= BLACK_MAX_LIGHTNESS;
    }

    *//**
     * @return true if the color represents a color which is close to white.
     *//*
    private static boolean isWhite(float[] hslColor) {
        return hslColor[2] >= WHITE_MIN_LIGHTNESS;
    }

    public void setIsLowPriority(boolean isLowPriority) {
        mIsLowPriority = isLowPriority;
    }*/
}
