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
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatImageView;
import code.name.monkey.retromusic.R;

public class CircularImageView extends AppCompatImageView {

  private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

  // Default Values
  private static final float DEFAULT_BORDER_WIDTH = 4;
  private static final float DEFAULT_SHADOW_RADIUS = 8.0f;

  // Properties
  private float borderWidth;
  private int canvasSize;
  private float shadowRadius;
  private int shadowColor = Color.BLACK;

  // Object used to draw
  private Bitmap image;
  private Drawable drawable;
  private Paint paint;
  private Paint paintBorder;

  // region Constructor & Init Method
  public CircularImageView(final Context context) {
    this(context, null);
  }

  public CircularImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    // Init paint
    paint = new Paint();
    paint.setAntiAlias(true);

    paintBorder = new Paint();
    paintBorder.setAntiAlias(true);

    // Load the styled attributes and set their properties
    TypedArray attributes =
        context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyleAttr, 0);

    // Init Border
    if (attributes.getBoolean(R.styleable.CircularImageView_civ_border, true)) {
      float defaultBorderSize =
          DEFAULT_BORDER_WIDTH * getContext().getResources().getDisplayMetrics().density;
      setBorderWidth(
          attributes.getDimension(
              R.styleable.CircularImageView_civ_border_width, defaultBorderSize));
      setBorderColor(
          attributes.getColor(R.styleable.CircularImageView_civ_border_color, Color.WHITE));
    }

    // Init Shadow
    if (attributes.getBoolean(R.styleable.CircularImageView_civ_shadow, false)) {
      shadowRadius = DEFAULT_SHADOW_RADIUS;
      drawShadow(
          attributes.getFloat(R.styleable.CircularImageView_civ_shadow_radius, shadowRadius),
          attributes.getColor(R.styleable.CircularImageView_civ_shadow_color, shadowColor));
    }
    attributes.recycle();
  }
  // endregion

  // region Set Attr Method
  public void setBorderWidth(float borderWidth) {
    this.borderWidth = borderWidth;
    requestLayout();
    invalidate();
  }

  public void setBorderColor(int borderColor) {
    if (paintBorder != null) {
      paintBorder.setColor(borderColor);
    }
    invalidate();
  }

  public void addShadow() {
    if (shadowRadius == 0) {
      shadowRadius = DEFAULT_SHADOW_RADIUS;
    }
    drawShadow(shadowRadius, shadowColor);
    invalidate();
  }

  public void setShadowRadius(float shadowRadius) {
    drawShadow(shadowRadius, shadowColor);
    invalidate();
  }

  public void setShadowColor(int shadowColor) {
    drawShadow(shadowRadius, shadowColor);
    invalidate();
  }

  @Override
  public ScaleType getScaleType() {
    return SCALE_TYPE;
  }

  @Override
  public void setScaleType(ScaleType scaleType) {
    if (scaleType != SCALE_TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "ScaleType %s not supported. ScaleType.CENTER_CROP is used by default. So you don't need to use ScaleType.",
              scaleType));
    }
  }
  // endregion

  // region Draw Method
  @Override
  public void onDraw(Canvas canvas) {
    // Load the bitmap
    loadBitmap();

    // Check if image isn't null
    if (image == null) {
      return;
    }

    if (!isInEditMode()) {
      canvasSize = canvas.getWidth();
      if (canvas.getHeight() < canvasSize) {
        canvasSize = canvas.getHeight();
      }
    }

    // circleCenter is the x or y of the view's center
    // radius is the radius in pixels of the cirle to be drawn
    // paint contains the shader that will texture the shape
    int circleCenter = (int) (canvasSize - (borderWidth * 2)) / 2;
    // Draw Border
    canvas.drawCircle(
        circleCenter + borderWidth,
        circleCenter + borderWidth,
        circleCenter + borderWidth - (shadowRadius + shadowRadius / 2),
        paintBorder);
    // Draw CircularImageView
    canvas.drawCircle(
        circleCenter + borderWidth,
        circleCenter + borderWidth,
        circleCenter - (shadowRadius + shadowRadius / 2),
        paint);
  }

  private void loadBitmap() {
    if (this.drawable == getDrawable()) {
      return;
    }

    this.drawable = getDrawable();
    this.image = drawableToBitmap(this.drawable);
    updateShader();
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    canvasSize = w;
    if (h < canvasSize) {
      canvasSize = h;
    }
    if (image != null) {
      updateShader();
    }
  }

  private void drawShadow(float shadowRadius, int shadowColor) {
    this.shadowRadius = shadowRadius;
    this.shadowColor = shadowColor;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
      setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
    }
    paintBorder.setShadowLayer(shadowRadius, 0.0f, shadowRadius / 2, shadowColor);
  }

  private void updateShader() {
    if (image == null) {
      return;
    }

    // Crop Center Image
    image = cropBitmap(image);

    // Create Shader
    BitmapShader shader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

    // Center Image in Shader
    Matrix matrix = new Matrix();
    matrix.setScale(
        (float) canvasSize / (float) image.getWidth(),
        (float) canvasSize / (float) image.getHeight());
    shader.setLocalMatrix(matrix);

    // Set Shader in Paint
    paint.setShader(shader);
  }

  private Bitmap cropBitmap(Bitmap bitmap) {
    Bitmap bmp;
    if (bitmap.getWidth() >= bitmap.getHeight()) {
      bmp =
          Bitmap.createBitmap(
              bitmap,
              bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
              0,
              bitmap.getHeight(),
              bitmap.getHeight());
    } else {
      bmp =
          Bitmap.createBitmap(
              bitmap,
              0,
              bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
              bitmap.getWidth(),
              bitmap.getWidth());
    }
    return bmp;
  }

  private Bitmap drawableToBitmap(Drawable drawable) {
    if (drawable == null) {
      return null;
    } else if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    int intrinsicWidth = drawable.getIntrinsicWidth();
    int intrinsicHeight = drawable.getIntrinsicHeight();

    if (!(intrinsicWidth > 0 && intrinsicHeight > 0)) {
      return null;
    }

    try {
      // Create Bitmap object out of the drawable
      Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
    } catch (OutOfMemoryError e) {
      // Simply return null of failed bitmap creations
      Log.e(getClass().toString(), "Encountered OutOfMemoryError while generating bitmap!");
      return null;
    }
  }
  // endregion

  // region Mesure Method
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    /*int imageSize = (width < height) ? width : height;
    setMeasuredDimension(imageSize, imageSize);*/
    setMeasuredDimension(width, height);
  }

  private int measureWidth(int measureSpec) {
    int result;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    if (specMode == MeasureSpec.EXACTLY) {
      // The parent has determined an exact size for the child.
      result = specSize;
    } else if (specMode == MeasureSpec.AT_MOST) {
      // The child can be as large as it wants up to the specified size.
      result = specSize;
    } else {
      // The parent has not imposed any constraint on the child.
      result = canvasSize;
    }

    return result;
  }

  private int measureHeight(int measureSpecHeight) {
    int result;
    int specMode = MeasureSpec.getMode(measureSpecHeight);
    int specSize = MeasureSpec.getSize(measureSpecHeight);

    if (specMode == MeasureSpec.EXACTLY) {
      // We were told how big to be
      result = specSize;
    } else if (specMode == MeasureSpec.AT_MOST) {
      // The child can be as large as it wants up to the specified size.
      result = specSize;
    } else {
      // Measure the text (beware: ascent is a negative number)
      result = canvasSize;
    }

    return (result + 2);
  }
  // endregion
}
