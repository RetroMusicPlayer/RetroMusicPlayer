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
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.FontRes;

import com.google.android.material.textview.MaterialTextView;

import code.name.monkey.retromusic.R;

public class BaselineGridTextView extends MaterialTextView {

  private final float FOUR_DIP;

  private int extraBottomPadding = 0;

  private int extraTopPadding = 0;

  private @FontRes int fontResId = 0;

  private float lineHeightHint = 0f;

  private float lineHeightMultiplierHint = 1f;

  private boolean maxLinesByHeight = false;

  public BaselineGridTextView(Context context) {
    this(context, null);
  }

  public BaselineGridTextView(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.textViewStyle);
  }

  public BaselineGridTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    final TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.BaselineGridTextView, defStyleAttr, 0);

    // first check TextAppearance for line height & font attributes
    if (a.hasValue(R.styleable.BaselineGridTextView_android_textAppearance)) {
      int textAppearanceId =
          a.getResourceId(
              R.styleable.BaselineGridTextView_android_textAppearance,
              android.R.style.TextAppearance);
      TypedArray ta =
          context.obtainStyledAttributes(textAppearanceId, R.styleable.BaselineGridTextView);
      parseTextAttrs(ta);
      ta.recycle();
    }

    // then check view attrs
    parseTextAttrs(a);
    maxLinesByHeight = a.getBoolean(R.styleable.BaselineGridTextView_maxLinesByHeight, false);
    a.recycle();

    FOUR_DIP =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
    computeLineHeight();
  }

  @Override
  public int getCompoundPaddingBottom() {
    // include extra padding to make the height a multiple of 4dp
    return super.getCompoundPaddingBottom() + extraBottomPadding;
  }

  @Override
  public int getCompoundPaddingTop() {
    // include extra padding to place the first line's baseline on the grid
    return super.getCompoundPaddingTop() + extraTopPadding;
  }

  public @FontRes int getFontResId() {
    return fontResId;
  }

  public float getLineHeightHint() {
    return lineHeightHint;
  }

  public void setLineHeightHint(float lineHeightHint) {
    this.lineHeightHint = lineHeightHint;
    computeLineHeight();
  }

  public float getLineHeightMultiplierHint() {
    return lineHeightMultiplierHint;
  }

  public void setLineHeightMultiplierHint(float lineHeightMultiplierHint) {
    this.lineHeightMultiplierHint = lineHeightMultiplierHint;
    computeLineHeight();
  }

  public boolean getMaxLinesByHeight() {
    return maxLinesByHeight;
  }

  public void setMaxLinesByHeight(boolean maxLinesByHeight) {
    this.maxLinesByHeight = maxLinesByHeight;
    requestLayout();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    extraTopPadding = 0;
    extraBottomPadding = 0;
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = getMeasuredHeight();
    height += ensureBaselineOnGrid();
    height += ensureHeightGridAligned(height);
    setMeasuredDimension(getMeasuredWidth(), height);
    checkMaxLines(height, MeasureSpec.getMode(heightMeasureSpec));
  }

  /**
   * When measured with an exact height, text can be vertically clipped mid-line. Prevent this by
   * setting the {@code maxLines} property based on the available space.
   */
  private void checkMaxLines(int height, int heightMode) {
    if (!maxLinesByHeight || heightMode != MeasureSpec.EXACTLY) {
      return;
    }

    int textHeight = height - getCompoundPaddingTop() - getCompoundPaddingBottom();
    int completeLines = (int) Math.floor(textHeight / getLineHeight());
    setMaxLines(completeLines);
  }

  /** Ensures line height is a multiple of 4dp. */
  private void computeLineHeight() {
    final Paint.FontMetrics fm = getPaint().getFontMetrics();
    final float fontHeight = Math.abs(fm.ascent - fm.descent) + fm.leading;
    final float desiredLineHeight =
        (lineHeightHint > 0) ? lineHeightHint : lineHeightMultiplierHint * fontHeight;

    final int baselineAlignedLineHeight =
        (int) ((FOUR_DIP * (float) Math.ceil(desiredLineHeight / FOUR_DIP)) + 0.5f);
    setLineSpacing(baselineAlignedLineHeight - fontHeight, 1f);
  }

  /** Ensure that the first line of text sits on the 4dp grid. */
  private int ensureBaselineOnGrid() {
    float baseline = getBaseline();
    float gridAlign = baseline % FOUR_DIP;
    if (gridAlign != 0) {
      extraTopPadding = (int) (FOUR_DIP - Math.ceil(gridAlign));
    }
    return extraTopPadding;
  }

  /** Ensure that height is a multiple of 4dp. */
  private int ensureHeightGridAligned(int height) {
    float gridOverhang = height % FOUR_DIP;
    if (gridOverhang != 0) {
      extraBottomPadding = (int) (FOUR_DIP - Math.ceil(gridOverhang));
    }
    return extraBottomPadding;
  }

  private void parseTextAttrs(TypedArray a) {
    if (a.hasValue(R.styleable.BaselineGridTextView_lineHeightMultiplierHint)) {
      lineHeightMultiplierHint =
          a.getFloat(R.styleable.BaselineGridTextView_lineHeightMultiplierHint, 1f);
    }
    if (a.hasValue(R.styleable.BaselineGridTextView_lineHeightHint)) {
      lineHeightHint = a.getDimensionPixelSize(R.styleable.BaselineGridTextView_lineHeightHint, 0);
    }
    if (a.hasValue(R.styleable.BaselineGridTextView_android_fontFamily)) {
      fontResId = a.getResourceId(R.styleable.BaselineGridTextView_android_fontFamily, 0);
    }
  }
}
