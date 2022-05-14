/*
 * Copyright (c) 2020 Hemanth Savarala.
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
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import code.name.monkey.retromusic.R;

/**
 * SeekArc.java
 *
 * <p>This is a class that functions much like a SeekBar but follows a circle path instead of a
 * straight line.
 *
 * @author Neil Davies
 */
public class SeekArc extends View {

  private static final String TAG = SeekArc.class.getSimpleName();
  private static int INVALID_PROGRESS_VALUE = -1;
  // The initial rotational offset -90 means we start at 12 o'clock
  private final int mAngleOffset = -90;
  private Paint mArcPaint;
  // Internal variables
  private int mArcRadius = 0;
  private RectF mArcRect = new RectF();
  /** The Width of the background arc for the SeekArc */
  private int mArcWidth = 2;
  /** Will the progress increase clockwise or anti-clockwise */
  private boolean mClockwise = true;
  /** is the control enabled/touchable */
  private boolean mEnabled = true;
  /** The Maximum value that this SeekArc can be set to */
  private int mMax = 100;

  private OnSeekArcChangeListener mOnSeekArcChangeListener;
  /** The Current value that the SeekArc is set to */
  private int mProgress = 0;

  private Paint mProgressPaint;
  private float mProgressSweep = 0;
  /** The width of the progress line for this SeekArc */
  private int mProgressWidth = 4;
  /** The rotation of the SeekArc- 0 is twelve o'clock */
  private int mRotation = 0;
  /** Give the SeekArc rounded edges */
  private boolean mRoundedEdges = false;
  /** The Angle to start drawing this Arc from */
  private int mStartAngle = 0;
  /** The Angle through which to draw the arc (Max is 360) */
  private int mSweepAngle = 360;
  /** The Drawable for the seek arc thumbnail */
  private Drawable mThumb;

  private int mThumbXPos;
  private int mThumbYPos;
  private double mTouchAngle;
  private float mTouchIgnoreRadius;
  /** Enable touch inside the SeekArc */
  private boolean mTouchInside = true;

  private int mTranslateX;
  private int mTranslateY;

  public SeekArc(Context context) {
    super(context);
    init(context, null, 0);
  }

  public SeekArc(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs, R.attr.seekArcStyle);
  }

  public SeekArc(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs, defStyle);
  }

  public int getArcColor() {
    return mArcPaint.getColor();
  }

  public void setArcColor(int color) {
    mArcPaint.setColor(color);
    invalidate();
  }

  public int getArcRotation() {
    return mRotation;
  }

  public void setArcRotation(int mRotation) {
    this.mRotation = mRotation;
    updateThumbPosition();
  }

  public int getArcWidth() {
    return mArcWidth;
  }

  public void setArcWidth(int mArcWidth) {
    this.mArcWidth = mArcWidth;
    mArcPaint.setStrokeWidth(mArcWidth);
  }

  public int getMax() {
    return mMax;
  }

  public void setMax(int mMax) {
    this.mMax = mMax;
  }

  public int getProgress() {
    return mProgress;
  }

  public void setProgress(int progress) {
    updateProgress(progress, false);
  }

  public int getProgressColor() {
    return mProgressPaint.getColor();
  }

  public void setProgressColor(int color) {
    mProgressPaint.setColor(color);
    invalidate();
  }

  public int getProgressWidth() {
    return mProgressWidth;
  }

  public void setProgressWidth(int mProgressWidth) {
    this.mProgressWidth = mProgressWidth;
    mProgressPaint.setStrokeWidth(mProgressWidth);
  }

  public int getStartAngle() {
    return mStartAngle;
  }

  public void setStartAngle(int mStartAngle) {
    this.mStartAngle = mStartAngle;
    updateThumbPosition();
  }

  public int getSweepAngle() {
    return mSweepAngle;
  }

  public void setSweepAngle(int mSweepAngle) {
    this.mSweepAngle = mSweepAngle;
    updateThumbPosition();
  }

  public boolean isClockwise() {
    return mClockwise;
  }

  public void setClockwise(boolean isClockwise) {
    mClockwise = isClockwise;
  }

  public boolean isEnabled() {
    return mEnabled;
  }

  public void setEnabled(boolean enabled) {
    this.mEnabled = enabled;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mEnabled) {
      this.getParent().requestDisallowInterceptTouchEvent(true);

      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          onStartTrackingTouch();
          updateOnTouch(event);
          break;
        case MotionEvent.ACTION_MOVE:
          updateOnTouch(event);
          break;
        case MotionEvent.ACTION_UP:
          onStopTrackingTouch();
          setPressed(false);
          this.getParent().requestDisallowInterceptTouchEvent(false);
          break;
        case MotionEvent.ACTION_CANCEL:
          onStopTrackingTouch();
          setPressed(false);
          this.getParent().requestDisallowInterceptTouchEvent(false);
          break;
      }
      return true;
    }
    return false;
  }

  /**
   * Sets a listener to receive notifications of changes to the SeekArc's progress level. Also
   * provides notifications of when the user starts and stops a touch gesture within the SeekArc.
   *
   * @param l The seek bar notification listener
   * @see SeekArc.OnSeekBarChangeListener
   */
  public void setOnSeekArcChangeListener(OnSeekArcChangeListener l) {
    mOnSeekArcChangeListener = l;
  }

  public void setRoundedEdges(boolean isEnabled) {
    mRoundedEdges = isEnabled;
    if (mRoundedEdges) {
      mArcPaint.setStrokeCap(Paint.Cap.ROUND);
      mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    } else {
      mArcPaint.setStrokeCap(Paint.Cap.SQUARE);
      mProgressPaint.setStrokeCap(Paint.Cap.SQUARE);
    }
  }

  public void setTouchInSide(boolean isEnabled) {
    int thumbHalfheight = (int) mThumb.getIntrinsicHeight() / 2;
    int thumbHalfWidth = (int) mThumb.getIntrinsicWidth() / 2;
    mTouchInside = isEnabled;
    if (mTouchInside) {
      mTouchIgnoreRadius = (float) mArcRadius / 4;
    } else {
      // Don't use the exact radius makes interaction too tricky
      mTouchIgnoreRadius = mArcRadius - Math.min(thumbHalfWidth, thumbHalfheight);
    }
  }

  @Override
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (mThumb != null && mThumb.isStateful()) {
      int[] state = getDrawableState();
      mThumb.setState(state);
    }
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (!mClockwise) {
      canvas.scale(-1, 1, mArcRect.centerX(), mArcRect.centerY());
    }

    // Draw the arcs
    final int arcStart = mStartAngle + mAngleOffset + mRotation;
    final int arcSweep = mSweepAngle;
    canvas.drawArc(mArcRect, arcStart, arcSweep, false, mArcPaint);
    canvas.drawArc(mArcRect, arcStart, mProgressSweep, false, mProgressPaint);

    if (mEnabled) {
      // Draw the thumb nail
      canvas.translate(mTranslateX - mThumbXPos, mTranslateY - mThumbYPos);
      mThumb.draw(canvas);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
    final int min = Math.min(width, height);
    float top = 0;
    float left = 0;
    int arcDiameter = 0;

    mTranslateX = (int) (width * 0.5f);
    mTranslateY = (int) (height * 0.5f);

    arcDiameter = min - getPaddingLeft();
    mArcRadius = arcDiameter / 2;
    top = height / 2 - (arcDiameter / 2);
    left = width / 2 - (arcDiameter / 2);
    mArcRect.set(left, top, left + arcDiameter, top + arcDiameter);

    int arcStart = (int) mProgressSweep + mStartAngle + mRotation + 90;
    mThumbXPos = (int) (mArcRadius * Math.cos(Math.toRadians(arcStart)));
    mThumbYPos = (int) (mArcRadius * Math.sin(Math.toRadians(arcStart)));

    setTouchInSide(mTouchInside);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private int getProgressForAngle(double angle) {
    int touchProgress = (int) Math.round(valuePerDegree() * angle);

    touchProgress = (touchProgress < 0) ? INVALID_PROGRESS_VALUE : touchProgress;
    touchProgress = (touchProgress > mMax) ? INVALID_PROGRESS_VALUE : touchProgress;
    return touchProgress;
  }

  private double getTouchDegrees(float xPos, float yPos) {
    float x = xPos - mTranslateX;
    float y = yPos - mTranslateY;
    // invert the x-coord if we are rotating anti-clockwise
    x = (mClockwise) ? x : -x;
    // convert to arc Angle
    double angle = Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2) - Math.toRadians(mRotation));
    if (angle < 0) {
      angle = 360 + angle;
    }
    angle -= mStartAngle;
    return angle;
  }

  private boolean ignoreTouch(float xPos, float yPos) {
    boolean ignore = false;
    float x = xPos - mTranslateX;
    float y = yPos - mTranslateY;

    float touchRadius = (float) Math.sqrt(((x * x) + (y * y)));
    if (touchRadius < mTouchIgnoreRadius) {
      ignore = true;
    }
    return ignore;
  }

  private void init(Context context, AttributeSet attrs, int defStyle) {

    Log.d(TAG, "Initialising SeekArc");
    final Resources res = getResources();
    float density = context.getResources().getDisplayMetrics().density;

    // Defaults, may need to link this into theme settings
    int arcColor = res.getColor(R.color.progress_gray);
    int progressColor = res.getColor(R.color.default_blue_light);
    int thumbHalfheight = 0;
    int thumbHalfWidth = 0;
    mThumb = res.getDrawable(R.drawable.switch_thumb_material);
    // Convert progress width to pixels for current density
    mProgressWidth = (int) (mProgressWidth * density);

    if (attrs != null) {
      // Attribute initialization
      final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekArc, defStyle, 0);

      Drawable thumb = a.getDrawable(R.styleable.SeekArc_thumb);
      if (thumb != null) {
        mThumb = thumb;
      }

      thumbHalfheight = (int) mThumb.getIntrinsicHeight() / 2;
      thumbHalfWidth = (int) mThumb.getIntrinsicWidth() / 2;
      mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);

      mMax = a.getInteger(R.styleable.SeekArc_max, mMax);
      mProgress = a.getInteger(R.styleable.SeekArc_seekProgress, mProgress);
      mProgressWidth = (int) a.getDimension(R.styleable.SeekArc_progressWidth, mProgressWidth);
      mArcWidth = (int) a.getDimension(R.styleable.SeekArc_arcWidth, mArcWidth);
      mStartAngle = a.getInt(R.styleable.SeekArc_startAngle, mStartAngle);
      mSweepAngle = a.getInt(R.styleable.SeekArc_sweepAngle, mSweepAngle);
      mRotation = a.getInt(R.styleable.SeekArc_rotation, mRotation);
      mRoundedEdges = a.getBoolean(R.styleable.SeekArc_roundEdges, mRoundedEdges);
      mTouchInside = a.getBoolean(R.styleable.SeekArc_touchInside, mTouchInside);
      mClockwise = a.getBoolean(R.styleable.SeekArc_clockwise, mClockwise);
      mEnabled = a.getBoolean(R.styleable.SeekArc_enabled, mEnabled);

      arcColor = a.getColor(R.styleable.SeekArc_arcColor, arcColor);
      progressColor = a.getColor(R.styleable.SeekArc_progressColor, progressColor);

      a.recycle();
    }

    mProgress = (mProgress > mMax) ? mMax : mProgress;
    mProgress = (mProgress < 0) ? 0 : mProgress;

    mSweepAngle = (mSweepAngle > 360) ? 360 : mSweepAngle;
    mSweepAngle = (mSweepAngle < 0) ? 0 : mSweepAngle;

    mProgressSweep = (float) mProgress / mMax * mSweepAngle;

    mStartAngle = (mStartAngle > 360) ? 0 : mStartAngle;
    mStartAngle = (mStartAngle < 0) ? 0 : mStartAngle;

    mArcPaint = new Paint();
    mArcPaint.setColor(arcColor);
    mArcPaint.setAntiAlias(true);
    mArcPaint.setStyle(Paint.Style.STROKE);
    mArcPaint.setStrokeWidth(mArcWidth);
    // mArcPaint.setAlpha(45);

    mProgressPaint = new Paint();
    mProgressPaint.setColor(progressColor);
    mProgressPaint.setAntiAlias(true);
    mProgressPaint.setStyle(Paint.Style.STROKE);
    mProgressPaint.setStrokeWidth(mProgressWidth);

    if (mRoundedEdges) {
      mArcPaint.setStrokeCap(Paint.Cap.ROUND);
      mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
    }
  }

  private void onProgressRefresh(int progress, boolean fromUser) {
    updateProgress(progress, fromUser);
  }

  private void onStartTrackingTouch() {
    if (mOnSeekArcChangeListener != null) {
      mOnSeekArcChangeListener.onStartTrackingTouch(this);
    }
  }

  private void onStopTrackingTouch() {
    if (mOnSeekArcChangeListener != null) {
      mOnSeekArcChangeListener.onStopTrackingTouch(this);
    }
  }

  private void updateOnTouch(MotionEvent event) {
    boolean ignoreTouch = ignoreTouch(event.getX(), event.getY());
    if (ignoreTouch) {
      return;
    }
    setPressed(true);
    mTouchAngle = getTouchDegrees(event.getX(), event.getY());
    int progress = getProgressForAngle(mTouchAngle);
    onProgressRefresh(progress, true);
  }

  private void updateProgress(int progress, boolean fromUser) {

    if (progress == INVALID_PROGRESS_VALUE) {
      return;
    }

    progress = (progress > mMax) ? mMax : progress;
    progress = (progress < 0) ? 0 : progress;
    mProgress = progress;

    if (mOnSeekArcChangeListener != null) {
      mOnSeekArcChangeListener.onProgressChanged(this, progress, fromUser);
    }

    mProgressSweep = (float) progress / mMax * mSweepAngle;

    updateThumbPosition();

    invalidate();
  }

  private void updateThumbPosition() {
    int thumbAngle = (int) (mStartAngle + mProgressSweep + mRotation + 90);
    mThumbXPos = (int) (mArcRadius * Math.cos(Math.toRadians(thumbAngle)));
    mThumbYPos = (int) (mArcRadius * Math.sin(Math.toRadians(thumbAngle)));
  }

  private float valuePerDegree() {
    return (float) mMax / mSweepAngle;
  }

  public interface OnSeekArcChangeListener {

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter to
     * distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekArc The SeekArc whose progress has changed
     * @param progress The current progress level. This will be in the range 0..max where max was
     *     set by {@link ProgressArc#setMax(int)}. (The default value for max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser);

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this to
     * disable advancing the seekbar.
     *
     * @param seekArc The SeekArc in which the touch gesture began
     */
    void onStartTrackingTouch(SeekArc seekArc);

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this to
     * re-enable advancing the seekarc.
     *
     * @param seekArc The SeekArc in which the touch gesture began
     */
    void onStopTrackingTouch(SeekArc seekArc);
  }
}
