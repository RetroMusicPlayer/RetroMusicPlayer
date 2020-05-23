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
package code.name.monkey.retromusic.lyrics


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Looper
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.R
import java.util.*

/**
 * Desc : 歌词
 * Author : Lauzy
 * Date : 2017/10/13
 * Blog : http://www.jianshu.com/u/e76853f863a9
 * Email : freedompaladin@gmail.com
 */
class LrcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mLrcData: MutableList<Lrc>? = null
    private var mTextPaint: TextPaint? = null
    private var mDefaultContent: String? = null
    private var mCurrentLine: Int = 0
    private var mOffset: Float = 0.toFloat()
    private var mLastMotionX: Float = 0.toFloat()
    private var mLastMotionY: Float = 0.toFloat()
    private var mScaledTouchSlop: Int = 0
    private var mOverScroller: OverScroller? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mMaximumFlingVelocity: Int = 0
    private var mMinimumFlingVelocity: Int = 0
    private var mLrcTextSize: Float = 0.toFloat()
    private var mLrcLineSpaceHeight: Float = 0.toFloat()
    private var mTouchDelay: Int = 0
    private var mNormalColor: Int = 0
    private var mCurrentPlayLineColor: Int = 0
    private var mNoLrcTextSize: Float = 0.toFloat()
    private var mNoLrcTextColor: Int = 0

    //是否拖拽中，否的话响应onClick事件
    private var isDragging: Boolean = false

    //用户开始操作
    private var isUserScroll: Boolean = false
    private var isAutoAdjustPosition = true
    private var mPlayDrawable: Drawable? = null
    private var isShowTimeIndicator: Boolean = false
    private var mPlayRect: Rect? = null
    private var mIndicatorPaint: Paint? = null
    private var mIndicatorLineWidth: Float = 0.toFloat()
    private var mIndicatorTextSize: Float = 0.toFloat()
    private var mCurrentIndicateLineTextColor: Int = 0
    private var mIndicatorLineColor: Int = 0
    private var mIndicatorMargin: Float = 0.toFloat()
    private var mIconLineGap: Float = 0.toFloat()
    private var mIconWidth: Float = 0.toFloat()
    private var mIconHeight: Float = 0.toFloat()
    private var isEnableShowIndicator = true
    private var mIndicatorTextColor: Int = 0
    private var mIndicatorTouchDelay: Int = 0
    private val mLrcMap = HashMap<String, StaticLayout>()
    private val mHideIndicatorRunnable = Runnable {
        isShowTimeIndicator = false
        invalidateView()
    }
    private val mStaticLayoutHashMap = HashMap<String, StaticLayout>()
    private val mScrollRunnable = Runnable {
        isUserScroll = false
        scrollToPosition(mCurrentLine)
    }
    private var mOnPlayIndicatorLineListener: OnPlayIndicatorLineListener? = null

    private val lrcWidth: Int
        get() = width - paddingLeft - paddingRight

    private val lrcHeight: Int
        get() = height

    private val isLrcEmpty: Boolean
        get() = mLrcData == null || lrcCount == 0

    private val lrcCount: Int
        get() = mLrcData!!.size

    //itemOffset 和 mOffset 最小即当前位置
    val indicatePosition: Int
        get() {
            var pos = 0
            var min = java.lang.Float.MAX_VALUE
            for (i in mLrcData!!.indices) {
                val offsetY = getItemOffsetY(i)
                val abs = Math.abs(offsetY - mOffset)
                if (abs < min) {
                    min = abs
                    pos = i
                }
            }
            return pos
        }

    var playDrawable: Drawable?
        get() = mPlayDrawable
        set(playDrawable) {
            mPlayDrawable = playDrawable
            mPlayDrawable!!.bounds = mPlayRect!!
            invalidateView()
        }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LrcView)
        mLrcTextSize =
            typedArray.getDimension(R.styleable.LrcView_lrcTextSize, sp2px(context, 15f).toFloat())
        mLrcLineSpaceHeight = typedArray.getDimension(
            R.styleable.LrcView_lrcLineSpaceSize,
            dp2px(context, 20f).toFloat()
        )
        mTouchDelay = typedArray.getInt(R.styleable.LrcView_lrcTouchDelay, 3500)
        mIndicatorTouchDelay = typedArray.getInt(R.styleable.LrcView_indicatorTouchDelay, 2500)
        mNormalColor = typedArray.getColor(R.styleable.LrcView_lrcNormalTextColor, Color.GRAY)
        mCurrentPlayLineColor =
            typedArray.getColor(R.styleable.LrcView_lrcCurrentTextColor, Color.BLUE)
        mNoLrcTextSize = typedArray.getDimension(
            R.styleable.LrcView_noLrcTextSize,
            dp2px(context, 20f).toFloat()
        )
        mNoLrcTextColor = typedArray.getColor(R.styleable.LrcView_noLrcTextColor, Color.BLACK)
        mIndicatorLineWidth = typedArray.getDimension(
            R.styleable.LrcView_indicatorLineHeight,
            dp2px(context, 0.5f).toFloat()
        )
        mIndicatorTextSize = typedArray.getDimension(
            R.styleable.LrcView_indicatorTextSize,
            sp2px(context, 13f).toFloat()
        )
        mIndicatorTextColor =
            typedArray.getColor(R.styleable.LrcView_indicatorTextColor, Color.GRAY)
        mCurrentIndicateLineTextColor =
            typedArray.getColor(R.styleable.LrcView_currentIndicateLrcColor, Color.GRAY)
        mIndicatorLineColor =
            typedArray.getColor(R.styleable.LrcView_indicatorLineColor, Color.GRAY)
        mIndicatorMargin = typedArray.getDimension(
            R.styleable.LrcView_indicatorStartEndMargin,
            dp2px(context, 5f).toFloat()
        )
        mIconLineGap =
            typedArray.getDimension(R.styleable.LrcView_iconLineGap, dp2px(context, 3f).toFloat())
        mIconWidth = typedArray.getDimension(
            R.styleable.LrcView_playIconWidth,
            dp2px(context, 20f).toFloat()
        )
        mIconHeight = typedArray.getDimension(
            R.styleable.LrcView_playIconHeight,
            dp2px(context, 20f).toFloat()
        )
        mPlayDrawable = typedArray.getDrawable(R.styleable.LrcView_playIcon)
        mPlayDrawable = if (mPlayDrawable == null) ContextCompat.getDrawable(
            context,
            R.drawable.ic_play_arrow_white_24dp
        ) else mPlayDrawable
        typedArray.recycle()

        setupConfigs(context)
    }

    private fun setupConfigs(context: Context) {
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMaximumFlingVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinimumFlingVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
        mOverScroller = OverScroller(context, DecelerateInterpolator())
        mOverScroller!!.setFriction(0.1f)
        //        ViewConfiguration.getScrollFriction();  默认摩擦力 0.015f

        mTextPaint = TextPaint()
        mTextPaint!!.apply {
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
            textSize = mLrcTextSize
            /* if (BuildConfig.FLAVOR != "nofont") {
                 typeface = ResourcesCompat.getFont(context, R.font.circular)
             }*/
        }
        mDefaultContent = DEFAULT_CONTENT

        mIndicatorPaint = Paint()
        mIndicatorPaint!!.isAntiAlias = true
        mIndicatorPaint!!.strokeWidth = mIndicatorLineWidth
        mIndicatorPaint!!.color = mIndicatorLineColor
        mPlayRect = Rect()
        mIndicatorPaint!!.textSize = mIndicatorTextSize
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            mPlayRect!!.left = mIndicatorMargin.toInt()
            mPlayRect!!.top = (height / 2 - mIconHeight / 2).toInt()
            mPlayRect!!.right = (mPlayRect!!.left + mIconWidth).toInt()
            mPlayRect!!.bottom = (mPlayRect!!.top + mIconHeight).toInt()
            mPlayDrawable!!.bounds = mPlayRect!!
        }
    }

    fun setLrcData(lrcData: MutableList<Lrc>) {
        resetView(DEFAULT_CONTENT)
        mLrcData = lrcData
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isLrcEmpty) {
            drawEmptyText(canvas)
            return
        }
        val indicatePosition = indicatePosition
        mTextPaint!!.textSize = mLrcTextSize
        mTextPaint!!.textAlign = Paint.Align.LEFT
        var y = (lrcHeight / 2).toFloat()
        val x = dip2px(context, 16f).toFloat()
        for (i in 0 until lrcCount) {
            if (i > 0) {
                y += (getTextHeight(i - 1) + getTextHeight(i)) / 2 + mLrcLineSpaceHeight
            }
            if (mCurrentLine == i) {
                mTextPaint!!.color = mCurrentPlayLineColor
            } else if (indicatePosition == i && isShowTimeIndicator) {
                mTextPaint!!.color = mCurrentIndicateLineTextColor
            } else {
                mTextPaint!!.color = mNormalColor
            }
            drawLrc(canvas, x, y, i)
        }

        if (isShowTimeIndicator) {
            mPlayDrawable!!.draw(canvas)
            val time = mLrcData!![indicatePosition].time
            val timeWidth = mIndicatorPaint!!.measureText(LrcHelper.formatTime(time))
            mIndicatorPaint!!.color = mIndicatorLineColor
            canvas.drawLine(
                mPlayRect!!.right + mIconLineGap, (height / 2).toFloat(),
                width - timeWidth * 1.3f, (height / 2).toFloat(), mIndicatorPaint!!
            )
            val baseX = (width - timeWidth * 1.1f).toInt()
            val baseline =
                (height / 2).toFloat() - (mIndicatorPaint!!.descent() - mIndicatorPaint!!.ascent()) / 2 - mIndicatorPaint!!.ascent()
            mIndicatorPaint!!.color = mIndicatorTextColor
            canvas.drawText(
                LrcHelper.formatTime(time),
                baseX.toFloat(),
                baseline,
                mIndicatorPaint!!
            )
        }
    }

    private fun dip2px(context: Context, dpVale: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpVale * scale + 0.5f).toInt()
    }

    private fun drawLrc(canvas: Canvas, x: Float, y: Float, i: Int) {
        val text = mLrcData!![i].text
        var staticLayout: StaticLayout? = mLrcMap[text]
        if (staticLayout == null) {
            mTextPaint!!.textSize = mLrcTextSize
            staticLayout = StaticLayout(
                text,
                mTextPaint,
                lrcWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1f,
                0f,
                false
            )
            mLrcMap[text] = staticLayout
        }
        canvas.save()
        canvas.translate(x, y - (staticLayout.height / 2).toFloat() - mOffset)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    //中间空文字
    private fun drawEmptyText(canvas: Canvas) {
        mTextPaint!!.textAlign = Paint.Align.LEFT
        mTextPaint!!.color = mNoLrcTextColor
        mTextPaint!!.textSize = mNoLrcTextSize
        canvas.save()
        val staticLayout = StaticLayout(
            mDefaultContent, mTextPaint,
            lrcWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
        )
        val margin = dip2px(context, 16f).toFloat();
        canvas.translate(margin, margin)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    fun updateTime(time: Long) {
        if (isLrcEmpty) {
            return
        }
        val linePosition = getUpdateTimeLinePosition(time)
        if (mCurrentLine != linePosition) {
            mCurrentLine = linePosition
            if (isUserScroll) {
                invalidateView()
                return
            }
            ViewCompat.postOnAnimation(this@LrcView, mScrollRunnable)
        }
    }

    private fun getUpdateTimeLinePosition(time: Long): Int {
        var linePos = 0
        for (i in 0 until lrcCount) {
            val lrc = mLrcData!![i]
            if (time >= lrc.time) {
                if (i == lrcCount - 1) {
                    linePos = lrcCount - 1
                } else if (time < mLrcData!![i + 1].time) {
                    linePos = i
                    break
                }
            }
        }
        return linePos
    }

    private fun scrollToPosition(linePosition: Int) {
        val scrollY = getItemOffsetY(linePosition)
        val animator = ValueAnimator.ofFloat(mOffset, scrollY)
        animator.addUpdateListener { animation ->
            mOffset = animation.animatedValue as Float
            invalidateView()
        }
        animator.duration = 300
        animator.start()
    }

    private fun getItemOffsetY(linePosition: Int): Float {
        var tempY = 0f
        for (i in 1..linePosition) {
            tempY += (getTextHeight(i - 1) + getTextHeight(i)) / 2 + mLrcLineSpaceHeight
        }
        return tempY
    }

    private fun getTextHeight(linePosition: Int): Float {
        val text = mLrcData!![linePosition].text
        var staticLayout: StaticLayout? = mStaticLayoutHashMap[text]
        if (staticLayout == null) {
            mTextPaint!!.textSize = mLrcTextSize
            staticLayout = StaticLayout(
                text, mTextPaint,
                lrcWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
            )
            mStaticLayoutHashMap[text] = staticLayout
        }
        return staticLayout.height.toFloat()
    }

    private fun overScrolled(): Boolean {
        return mOffset > getItemOffsetY(lrcCount - 1) || mOffset < 0
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isLrcEmpty) {
            return super.onTouchEvent(event)
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                removeCallbacks(mScrollRunnable)
                removeCallbacks(mHideIndicatorRunnable)
                if (!mOverScroller!!.isFinished) {
                    mOverScroller!!.abortAnimation()
                }
                mLastMotionX = event.x
                mLastMotionY = event.y
                isUserScroll = true
                isDragging = false
            }

            MotionEvent.ACTION_MOVE -> {
                var moveY = event.y - mLastMotionY
                if (Math.abs(moveY) > mScaledTouchSlop) {
                    isDragging = true
                    isShowTimeIndicator = isEnableShowIndicator
                }
                if (isDragging) {

                    //                    if (mOffset < 0) {
                    //                        mOffset = Math.max(mOffset, -getTextHeight(0) - mLrcLineSpaceHeight);
                    //                    }
                    val maxHeight = getItemOffsetY(lrcCount - 1)
                    //                    if (mOffset > maxHeight) {
                    //                        mOffset = Math.min(mOffset, maxHeight + getTextHeight(getLrcCount() - 1) + mLrcLineSpaceHeight);
                    //                    }
                    if (mOffset < 0 || mOffset > maxHeight) {
                        moveY /= 3.5f
                    }
                    mOffset -= moveY
                    mLastMotionY = event.y
                    invalidateView()
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (!isDragging && (!isShowTimeIndicator || !onClickPlayButton(event))) {
                    isShowTimeIndicator = false
                    invalidateView()
                    performClick()
                }
                handleActionUp(event)
            }
        }
        //        return isDragging || super.onTouchEvent(event);
        return true
    }

    private fun handleActionUp(event: MotionEvent) {
        if (isEnableShowIndicator) {
            ViewCompat.postOnAnimationDelayed(
                this@LrcView,
                mHideIndicatorRunnable,
                mIndicatorTouchDelay.toLong()
            )
        }
        if (isShowTimeIndicator && mPlayRect != null && onClickPlayButton(event)) {
            isShowTimeIndicator = false
            invalidateView()
            if (mOnPlayIndicatorLineListener != null) {
                mOnPlayIndicatorLineListener!!.onPlay(
                    mLrcData!![indicatePosition].time,
                    mLrcData!![indicatePosition].text
                )
            }
        }
        if (overScrolled() && mOffset < 0) {
            scrollToPosition(0)
            if (isAutoAdjustPosition) {
                ViewCompat.postOnAnimationDelayed(
                    this@LrcView,
                    mScrollRunnable,
                    mTouchDelay.toLong()
                )
            }
            return
        }

        if (overScrolled() && mOffset > getItemOffsetY(lrcCount - 1)) {
            scrollToPosition(lrcCount - 1)
            if (isAutoAdjustPosition) {
                ViewCompat.postOnAnimationDelayed(
                    this@LrcView,
                    mScrollRunnable,
                    mTouchDelay.toLong()
                )
            }
            return
        }

        mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumFlingVelocity.toFloat())
        val YVelocity = mVelocityTracker!!.yVelocity
        val absYVelocity = Math.abs(YVelocity)
        if (absYVelocity > mMinimumFlingVelocity) {
            mOverScroller!!.fling(
                0, mOffset.toInt(), 0, (-YVelocity).toInt(), 0,
                0, 0, getItemOffsetY(lrcCount - 1).toInt(),
                0, getTextHeight(0).toInt()
            )
            invalidateView()
        }
        releaseVelocityTracker()
        if (isAutoAdjustPosition) {
            ViewCompat.postOnAnimationDelayed(this@LrcView, mScrollRunnable, mTouchDelay.toLong())
        }
    }

    private fun onClickPlayButton(event: MotionEvent): Boolean {
        val left = mPlayRect!!.left.toFloat()
        val right = mPlayRect!!.right.toFloat()
        val top = mPlayRect!!.top.toFloat()
        val bottom = mPlayRect!!.bottom.toFloat()
        val x = event.x
        val y = event.y
        return (mLastMotionX > left && mLastMotionX < right && mLastMotionY > top
                && mLastMotionY < bottom && x > left && x < right && y > top && y < bottom)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mOverScroller!!.computeScrollOffset()) {
            mOffset = mOverScroller!!.currY.toFloat()
            invalidateView()
        }
    }

    private fun releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    fun resetView(defaultContent: String) {
        if (mLrcData != null) {
            mLrcData!!.clear()
        }
        mLrcMap.clear()
        mStaticLayoutHashMap.clear()
        mCurrentLine = 0
        mOffset = 0f
        isUserScroll = false
        isDragging = false
        mDefaultContent = defaultContent
        removeCallbacks(mScrollRunnable)
        invalidate()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    fun sp2px(context: Context, spVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spVal, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 暂停（手动滑动歌词后，不再自动回滚至当前播放位置）
     */
    fun pause() {
        isAutoAdjustPosition = false
        invalidateView()
    }

    /**
     * 恢复（继续自动回滚）
     */
    fun resume() {
        isAutoAdjustPosition = true
        ViewCompat.postOnAnimationDelayed(this@LrcView, mScrollRunnable, mTouchDelay.toLong())
        invalidateView()
    }


    /*------------------Config-------------------*/

    private fun invalidateView() {
        if (Looper.getMainLooper().thread === Thread.currentThread()) {
            invalidate()
        } else {
            postInvalidate()
        }
    }

    fun setOnPlayIndicatorLineListener(onPlayIndicatorLineListener: OnPlayIndicatorLineListener) {
        mOnPlayIndicatorLineListener = onPlayIndicatorLineListener
    }

    fun setEmptyContent(defaultContent: String) {
        mDefaultContent = defaultContent
        invalidateView()
    }

    fun setLrcTextSize(lrcTextSize: Float) {
        mLrcTextSize = lrcTextSize
        invalidateView()
    }

    fun setLrcLineSpaceHeight(lrcLineSpaceHeight: Float) {
        mLrcLineSpaceHeight = lrcLineSpaceHeight
        invalidateView()
    }

    fun setTouchDelay(touchDelay: Int) {
        mTouchDelay = touchDelay
        invalidateView()
    }

    fun setNormalColor(@ColorInt normalColor: Int) {
        mNormalColor = normalColor
        invalidateView()
    }

    fun setCurrentPlayLineColor(@ColorInt currentPlayLineColor: Int) {
        mCurrentPlayLineColor = currentPlayLineColor
        invalidateView()
    }

    fun setNoLrcTextSize(noLrcTextSize: Float) {
        mNoLrcTextSize = noLrcTextSize
        invalidateView()
    }

    fun setNoLrcTextColor(@ColorInt noLrcTextColor: Int) {
        mNoLrcTextColor = noLrcTextColor
        invalidateView()
    }

    fun setIndicatorLineWidth(indicatorLineWidth: Float) {
        mIndicatorLineWidth = indicatorLineWidth
        invalidateView()
    }

    fun setIndicatorTextSize(indicatorTextSize: Float) {
        //        mIndicatorTextSize = indicatorTextSize;
        mIndicatorPaint!!.textSize = indicatorTextSize
        invalidateView()
    }

    fun setCurrentIndicateLineTextColor(currentIndicateLineTextColor: Int) {
        mCurrentIndicateLineTextColor = currentIndicateLineTextColor
        invalidateView()
    }

    fun setIndicatorLineColor(indicatorLineColor: Int) {
        mIndicatorLineColor = indicatorLineColor
        invalidateView()
    }

    fun setIndicatorMargin(indicatorMargin: Float) {
        mIndicatorMargin = indicatorMargin
        invalidateView()
    }

    fun setIconLineGap(iconLineGap: Float) {
        mIconLineGap = iconLineGap
        invalidateView()
    }

    fun setIconWidth(iconWidth: Float) {
        mIconWidth = iconWidth
        invalidateView()
    }

    fun setIconHeight(iconHeight: Float) {
        mIconHeight = iconHeight
        invalidateView()
    }

    fun setEnableShowIndicator(enableShowIndicator: Boolean) {
        isEnableShowIndicator = enableShowIndicator
        invalidateView()
    }

    fun setIndicatorTextColor(indicatorTextColor: Int) {
        mIndicatorTextColor = indicatorTextColor
        invalidateView()
    }

    interface OnPlayIndicatorLineListener {
        fun onPlay(time: Long, content: String)
    }

    companion object {
        private const val DEFAULT_CONTENT = "Empty"
    }
}