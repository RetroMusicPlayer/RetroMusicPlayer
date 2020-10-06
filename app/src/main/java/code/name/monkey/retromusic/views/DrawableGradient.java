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

import android.graphics.drawable.GradientDrawable;

public class DrawableGradient extends GradientDrawable {
  public DrawableGradient(Orientation orientations, int[] colors, int shape) {
    super(orientations, colors);
    try {
      setShape(shape);
      setGradientType(GradientDrawable.LINEAR_GRADIENT);
      setCornerRadius(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public DrawableGradient SetTransparency(int transparencyPercent) {
    this.setAlpha(255 - ((255 * transparencyPercent) / 100));
    return this;
  }
}
