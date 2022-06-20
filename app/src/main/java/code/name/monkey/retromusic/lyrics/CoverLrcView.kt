/*
 * Copyright (C) 2017 wangchenyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package code.name.monkey.retromusic.lyrics

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Looper
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import code.name.monkey.retromusic.R
import kotlinx.coroutines.*
import java.io.File
import java.lang.Runnable
import kotlin.math.abs

/**
 * 歌词 Created by wcy on 2015/11/9.
 */
@SuppressLint("StaticFieldLeak")
class CoverLrcView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val mLrcEntryList: MutableList<LrcEntry> = ArrayList()
    private val mLrcPaint = TextPaint()
    private val mTimePaint = TextPaint()
    private var mTimeFontMetrics: Paint.FontMetrics? = null
    private var mPlayDrawable: Drawable? = null
    private var mDividerHeight = 0f
    private var mAnimationDuration: Long = 0
    private var mNormalTextColor = 0
    private var mNormalTextSize = 0f
    private var mCurrentTextColor = 0
    private var mCurrentTextSize = 0f
    private var mTimelineTextColor = 0
    private var mTimelineColor = 0
    private var mTimeTextColor = 0
    private var mDrawableWidth = 0
    private var mTimeTextWidth = 0
    private var mDefaultLabel: String? = null
    private var mLrcPadding = 0f
    private var mOnPlayClickListener: OnPlayClickListener? = null
    private var mAnimator: ValueAnimator? = null
    private var mGestureDetector: GestureDetector? = null
    private var mScroller: Scroller? = null
    private var mOffset = 0f
    private var mCurrentLine = 0
    private var isShowTimeline = false
    private var isTouching = false
    private var isFling = false
    private var mTextGravity // 歌词显示位置，靠左/居中/靠右
            = 0
    private val hideTimelineRunnable = Runnable {
        if (hasLrc() && isShowTimeline) {
            isShowTimeline = false
            smoothScrollTo(mCurrentLine)
        }
    }

    private val viewScope = CoroutineScope(Dispatchers.Main + Job())

    private val mSimpleOnGestureListener: SimpleOnGestureListener =
        object : SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                if (hasLrc() && mOnPlayClickListener != null) {
                    if (mOffset != getOffset(0)) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    mScroller!!.forceFinished(true)
                    removeCallbacks(hideTimelineRunnable)
                    isTouching = true
                    isShowTimeline = true
                    invalidate()
                    return true
                }
                return super.onDown(e)
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float,
            ): Boolean {
                if (mOffset == getOffset(0) && distanceY < 0F) {
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
                if (hasLrc()) {
                    mOffset += -distanceY
                    mOffset = mOffset.coerceAtMost(getOffset(0))
                    mOffset = mOffset.coerceAtLeast(getOffset(mLrcEntryList.size - 1))
                    invalidate()
                    parent.requestDisallowInterceptTouchEvent(true)
                    return true
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float,
            ): Boolean {
                if (hasLrc()) {
                    mScroller!!.fling(
                        0,
                        mOffset.toInt(),
                        0,
                        velocityY.toInt(),
                        0,
                        0,
                        getOffset(mLrcEntryList.size - 1).toInt(),
                        getOffset(0).toInt()
                    )
                    isFling = true
                    return true
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (hasLrc()
                    && isShowTimeline
                    && mPlayDrawable!!.bounds.contains(e.x.toInt(), e.y.toInt())
                ) {
                    val centerLine = centerLine
                    val centerLineTime = mLrcEntryList[centerLine].time
                    // onPlayClick 消费了才更新 UI
                    if (mOnPlayClickListener != null && mOnPlayClickListener!!.onPlayClick(
                            centerLineTime
                        )
                    ) {
                        isShowTimeline = false
                        removeCallbacks(hideTimelineRunnable)
                        mCurrentLine = centerLine
                        animateCurrentTextSize()
                        return true
                    }
                } else {
                    callOnClick()
                    return true
                }
                return super.onSingleTapConfirmed(e)
            }
        }

    private fun init(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LrcView)
        mCurrentTextSize = ta.getDimension(
            R.styleable.LrcView_lrcTextSize, resources.getDimension(R.dimen.lrc_text_size)
        )
        mNormalTextSize = ta.getDimension(
            R.styleable.LrcView_lrcNormalTextSize,
            resources.getDimension(R.dimen.lrc_text_size)
        )
        if (mNormalTextSize == 0f) {
            mNormalTextSize = mCurrentTextSize
        }
        mDividerHeight = ta.getDimension(
            R.styleable.LrcView_lrcDividerHeight,
            resources.getDimension(R.dimen.lrc_divider_height)
        )
        val defDuration = resources.getInteger(R.integer.lrc_animation_duration)
        mAnimationDuration =
            ta.getInt(R.styleable.LrcView_lrcAnimationDuration, defDuration).toLong()
        mAnimationDuration =
            if (mAnimationDuration < 0) defDuration.toLong() else mAnimationDuration
        mNormalTextColor = ta.getColor(
            R.styleable.LrcView_lrcNormalTextColor,
            ContextCompat.getColor(context, R.color.lrc_normal_text_color)
        )
        mCurrentTextColor = ta.getColor(
            R.styleable.LrcView_lrcCurrentTextColor,
            ContextCompat.getColor(context, R.color.lrc_current_text_color)
        )
        mTimelineTextColor = ta.getColor(
            R.styleable.LrcView_lrcTimelineTextColor,
            ContextCompat.getColor(context, R.color.lrc_timeline_text_color)
        )
        mDefaultLabel = ta.getString(R.styleable.LrcView_lrcLabel)
        mDefaultLabel =
            if (mDefaultLabel.isNullOrEmpty()) context.getString(R.string.empty) else mDefaultLabel
        mLrcPadding = ta.getDimension(R.styleable.LrcView_lrcPadding, 0f)
        mTimelineColor = ta.getColor(
            R.styleable.LrcView_lrcTimelineColor,
            ContextCompat.getColor(context, R.color.lrc_timeline_color)
        )
        val timelineHeight = ta.getDimension(
            R.styleable.LrcView_lrcTimelineHeight,
            resources.getDimension(R.dimen.lrc_timeline_height)
        )
        mPlayDrawable = ta.getDrawable(R.styleable.LrcView_lrcPlayDrawable)
        mPlayDrawable =
            if (mPlayDrawable == null) ContextCompat.getDrawable(
                context,
                R.drawable.ic_play_arrow
            ) else mPlayDrawable
        mTimeTextColor = ta.getColor(
            R.styleable.LrcView_lrcTimeTextColor,
            ContextCompat.getColor(context, R.color.lrc_time_text_color)
        )
        val timeTextSize = ta.getDimension(
            R.styleable.LrcView_lrcTimeTextSize,
            resources.getDimension(R.dimen.lrc_time_text_size)
        )
        mTextGravity = ta.getInteger(R.styleable.LrcView_lrcTextGravity, LrcEntry.GRAVITY_CENTER)
        ta.recycle()
        mDrawableWidth = resources.getDimension(R.dimen.lrc_drawable_width).toInt()
        mTimeTextWidth = resources.getDimension(R.dimen.lrc_time_width).toInt()
        mLrcPaint.isAntiAlias = true
        mLrcPaint.textSize = mCurrentTextSize
        mLrcPaint.textAlign = Paint.Align.LEFT
        mTimePaint.isAntiAlias = true
        mTimePaint.textSize = timeTextSize
        mTimePaint.textAlign = Paint.Align.CENTER
        mTimePaint.strokeWidth = timelineHeight
        mTimePaint.strokeCap = Paint.Cap.ROUND
        mTimeFontMetrics = mTimePaint.fontMetrics
        mGestureDetector = GestureDetector(context, mSimpleOnGestureListener)
        mGestureDetector!!.setIsLongpressEnabled(false)
        mScroller = Scroller(context)
    }

    fun setNormalColor(normalColor: Int) {
        mNormalTextColor = normalColor
        postInvalidate()
    }

    fun setCurrentColor(currentColor: Int) {
        mCurrentTextColor = currentColor
        postInvalidate()
    }

    fun setTimelineTextColor(timelineTextColor: Int) {
        mTimelineTextColor = timelineTextColor
        postInvalidate()
    }

    fun setTimelineColor(timelineColor: Int) {
        mTimelineColor = timelineColor
        postInvalidate()
    }

    fun setTimeTextColor(timeTextColor: Int) {
        mTimeTextColor = timeTextColor
        postInvalidate()
    }

    fun setDraggable(draggable: Boolean, onPlayClickListener: OnPlayClickListener?) {
        mOnPlayClickListener = if (draggable) {
            requireNotNull(onPlayClickListener) { "if draggable == true, onPlayClickListener must not be null" }
            onPlayClickListener
        } else {
            null
        }
    }

    fun setLabel(label: String?) {
        runOnUi {
            mDefaultLabel = label
            invalidate()
        }
    }

    fun loadLrc(lrcFile: File) {
        runOnUi {
            reset()
            viewScope.launch(Dispatchers.IO) {
                val lrcEntries = LrcUtils.parseLrc(arrayOf(lrcFile, null))
                withContext(Dispatchers.Main) {
                    onLrcLoaded(lrcEntries)
                }
            }
        }
    }

    fun loadLrc(lrcText: String?) {
        runOnUi {
            reset()
            viewScope.launch(Dispatchers.IO) {
                val lrcEntries = LrcUtils.parseLrc(arrayOf(lrcText, null))
                withContext(Dispatchers.Main) {
                    onLrcLoaded(lrcEntries)
                }
            }
        }
    }

    fun hasLrc(): Boolean {
        return mLrcEntryList.isNotEmpty()
    }

    fun updateTime(time: Long) {
        runOnUi {
            if (!hasLrc()) {
                return@runOnUi
            }
            val line = findShowLine(time + 300L)
            if (line != mCurrentLine) {
                mCurrentLine = line
                if (!isShowTimeline) {
                    smoothScrollTo(line)
                    animateCurrentTextSize()
                } else {
                    invalidate()
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            initPlayDrawable()
            initEntryList()
            if (hasLrc()) {
                smoothScrollTo(mCurrentLine, 0L)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2

        if (!hasLrc()) {
            mLrcPaint.color = mCurrentTextColor
            @Suppress("Deprecation")
            @SuppressLint("DrawAllocation") val staticLayout = StaticLayout(
                mDefaultLabel,
                mLrcPaint,
                lrcWidth.toInt(),
                Layout.Alignment.ALIGN_CENTER,
                1f,
                0f,
                false
            )
            drawText(canvas, staticLayout, centerY.toFloat())
            return
        }
        val centerLine = centerLine
        if (isShowTimeline) {
            mPlayDrawable?.draw(canvas)
            mTimePaint.color = mTimeTextColor
            val timeText = LrcUtils.formatTime(mLrcEntryList[centerLine].time)
            val timeX = (width - mTimeTextWidth / 2).toFloat()
            val timeY = centerY - (mTimeFontMetrics!!.descent + mTimeFontMetrics!!.ascent) / 2
            canvas.drawText(timeText, timeX, timeY, mTimePaint)
        }
        canvas.translate(0f, mOffset)
        var y = 0f
        for (i in mLrcEntryList.indices) {
            if (i > 0) {
                y += ((mLrcEntryList[i - 1].height + mLrcEntryList[i].height shr 1)
                        + mDividerHeight)
            }
            if (i == mCurrentLine) {
                mLrcPaint.textSize = mCurrentTextSize
                mLrcPaint.color = mCurrentTextColor
            } else if (isShowTimeline && i == centerLine) {
                mLrcPaint.color = mTimelineTextColor
            } else {
                mLrcPaint.textSize = mNormalTextSize
                mLrcPaint.color = mNormalTextColor
            }
            drawText(canvas, mLrcEntryList[i].staticLayout, y)
        }
    }

    private fun drawText(canvas: Canvas, staticLayout: StaticLayout, y: Float) {
        canvas.withSave {
            translate(mLrcPadding, y - (staticLayout.height shr 1))
            staticLayout.draw(this)
        }
    }

    fun animateCurrentTextSize() {
        val currentTextSize = mCurrentTextSize
        ValueAnimator.ofFloat(mNormalTextSize, currentTextSize).apply {
            addUpdateListener {
                mCurrentTextSize = it.animatedValue as Float
                invalidate()
            }
            duration = 300L
            start()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_CANCEL
        ) {
            isTouching = false
            if (hasLrc() && !isFling) {
                adjustCenter()
                postDelayed(hideTimelineRunnable, TIMELINE_KEEP_TIME)
            }
        }
        return mGestureDetector!!.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            mOffset = mScroller!!.currY.toFloat()
            invalidate()
        }
        if (isFling && mScroller!!.isFinished) {
            isFling = false
            if (hasLrc() && !isTouching) {
                adjustCenter()
                postDelayed(hideTimelineRunnable, TIMELINE_KEEP_TIME)
            }
        }
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(hideTimelineRunnable)
        viewScope.cancel()
        super.onDetachedFromWindow()
    }

    private fun onLrcLoaded(entryList: List<LrcEntry>?) {
        if (entryList != null && entryList.isNotEmpty()) {
            mLrcEntryList.addAll(entryList)
        }
        mLrcEntryList.sort()
        initEntryList()
        invalidate()
    }

    private fun initPlayDrawable() {
        val l = (mTimeTextWidth - mDrawableWidth) / 2
        val t = height / 2 - mDrawableWidth / 2
        val r = l + mDrawableWidth
        val b = t + mDrawableWidth
        mPlayDrawable!!.setBounds(l, t, r, b)
    }

    private fun initEntryList() {
        if (!hasLrc() || width == 0) {
            return
        }
        for (lrcEntry in mLrcEntryList) {
            lrcEntry.init(mLrcPaint, lrcWidth.toInt(), mTextGravity)
        }
        mOffset = (height / 2).toFloat()
    }

    fun reset() {
        endAnimation()
        mScroller!!.forceFinished(true)
        isShowTimeline = false
        isTouching = false
        isFling = false
        removeCallbacks(hideTimelineRunnable)
        mLrcEntryList.clear()
        mOffset = 0f
        mCurrentLine = 0
        invalidate()
    }

    private fun adjustCenter() {
        smoothScrollTo(centerLine, ADJUST_DURATION)
    }

    private fun smoothScrollTo(line: Int, duration: Long = mAnimationDuration) {
        val offset = getOffset(line)
        endAnimation()
        mAnimator = ValueAnimator.ofFloat(mOffset, offset).apply {
            this.duration = duration
            interpolator = LinearInterpolator()
            addUpdateListener { animation: ValueAnimator ->
                mOffset = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun endAnimation() {
        if (mAnimator != null && mAnimator!!.isRunning) {
            mAnimator!!.end()
        }
    }

    private fun findShowLine(time: Long): Int {
        var left = 0
        var right = mLrcEntryList.size
        while (left <= right) {
            val middle = (left + right) / 2
            val middleTime = mLrcEntryList[middle].time
            if (time < middleTime) {
                right = middle - 1
            } else {
                if (middle + 1 >= mLrcEntryList.size || time < mLrcEntryList[middle + 1].time) {
                    return middle
                }
                left = middle + 1
            }
        }
        return 0
    }

    private val centerLine: Int
        get() {
            var centerLine = 0
            var minDistance = Float.MAX_VALUE
            for (i in mLrcEntryList.indices) {
                if (abs(mOffset - getOffset(i)) < minDistance) {
                    minDistance = abs(mOffset - getOffset(i))
                    centerLine = i
                }
            }
            return centerLine
        }

    private fun getOffset(line: Int): Float {
        if (mLrcEntryList.isEmpty()) return 0F
        if (mLrcEntryList[line].offset == Float.MIN_VALUE) {
            var offset = (height / 2).toFloat()
            for (i in 1..line) {
                offset -= ((mLrcEntryList[i - 1].height + mLrcEntryList[i].height shr 1)
                        + mDividerHeight)
            }
            mLrcEntryList[line].offset = offset
        }
        return mLrcEntryList[line].offset
    }

    private val lrcWidth: Float
        get() = width - mLrcPadding * 2

    private fun runOnUi(r: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run()
        } else {
            post(r)
        }
    }

    fun interface OnPlayClickListener {
        fun onPlayClick(time: Long): Boolean
    }

    companion object {
        private const val ADJUST_DURATION: Long = 100
        private const val TIMELINE_KEEP_TIME = 4 * DateUtils.SECOND_IN_MILLIS
    }

    init {
        init(attrs)
    }
}