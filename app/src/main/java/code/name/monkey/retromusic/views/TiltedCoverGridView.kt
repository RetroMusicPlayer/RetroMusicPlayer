/*
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package code.name.monkey.retromusic.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.model.Song
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.hypot

/**
 * A wall of album covers drawn at an angle, darkened, slowly scrolling sideways.
 * Rows alternate direction. Everything is drawn in [onDraw] from a few small bitmaps, so it
 * stays cheap; the animation stops when the view is hidden, detached, or when the speed is 0
 * or the system has animations turned off.
 */
class TiltedCoverGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var covers: List<Bitmap> = emptyList()
    private val targets = ArrayList<CustomTarget<Bitmap>>()
    private var loadedKey: List<Long> = emptyList()
    private var loading = false

    private val tilePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val shadePaint = Paint()
    private val fadePaint = Paint()
    private val src = Rect()
    private val dst = RectF()

    private var angleDeg = 15f
    private var darkness = 0.5f
    private var speedDpPerSec = 20f

    private var offsetPx = 0f
    private var lastFrameNanos = 0L
    private val density = resources.displayMetrics.density

    /** Applies the look settings. Safe to call repeatedly. */
    fun configure(angleDegrees: Int, darknessPercent: Int, speedDpPerSecond: Int) {
        angleDeg = angleDegrees.coerceIn(0, 45).toFloat()
        darkness = (darknessPercent.coerceIn(0, 90)) / 100f
        speedDpPerSec = speedDpPerSecond.coerceAtLeast(0).toFloat()
        shadePaint.color = Color.argb((darkness * 255).toInt(), 0, 0, 0)
        lastFrameNanos = 0L
        invalidate()
    }

    /** Loads the covers of [songs]. Does nothing if the same songs are already shown. */
    fun setCovers(songs: List<Song>) {
        val key = songs.map { it.id }
        if (key == loadedKey) return
        loadedKey = key
        clearTargets()
        if (songs.isEmpty()) {
            loading = false
            covers = emptyList()
            invalidate()
            return
        }
        loading = true
        val size = (140 * density).toInt()
        val loaded = arrayOfNulls<Bitmap>(songs.size)
        var finished = 0
        fun onOneFinished() {
            finished++
            if (finished == songs.size) {
                loading = false
                covers = loaded.filterNotNull()
                invalidate()
            }
        }
        songs.forEachIndexed { index, song ->
            val target = object : CustomTarget<Bitmap>(size, size) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Glide may recycle its own bitmap later, so keep a copy.
                    loaded[index] = resource.copy(Bitmap.Config.ARGB_8888, false)
                    onOneFinished()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    onOneFinished()
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            }
            targets.add(target)
            Glide.with(context.applicationContext)
                .asBitmap()
                .load(RetroGlideExtension.getSongModel(song))
                .centerCrop()
                .into(target)
        }
    }

    private fun clearTargets() {
        val glide = Glide.with(context.applicationContext)
        targets.forEach { glide.clear(it) }
        targets.clear()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val tv = TypedValue()
        val surface = if (context.theme.resolveAttribute(android.R.attr.colorBackground, tv, true)) {
            tv.data
        } else {
            Color.BLACK
        }
        fadePaint.shader = LinearGradient(
            0f, h * 0.55f, 0f, h.toFloat(),
            Color.TRANSPARENT, surface, Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        val list = covers
        if (list.isNotEmpty() && w > 0f && h > 0f) {
            drawGrid(canvas, list, w, h)
        }
        canvas.drawRect(0f, 0f, w, h, shadePaint)
        canvas.drawRect(0f, h * 0.55f, w, h, fadePaint)

        if (shouldAnimate(list)) {
            val now = System.nanoTime()
            if (lastFrameNanos != 0L) {
                val dt = ((now - lastFrameNanos) / 1_000_000_000f).coerceAtMost(0.1f)
                offsetPx += speedDpPerSec * density * dt
            }
            lastFrameNanos = now
            postInvalidateOnAnimation()
        } else {
            lastFrameNanos = 0L
        }
    }

    private fun drawGrid(canvas: Canvas, list: List<Bitmap>, w: Float, h: Float) {
        val tile = h / 2.3f
        val gap = 6f * density
        val step = tile + gap
        val diagonal = hypot(w, h)
        val cells = ceil(diagonal / step).toInt() + 2
        val startX = w / 2f - diagonal / 2f
        val startY = h / 2f - diagonal / 2f

        val saved = canvas.save()
        canvas.rotate(-angleDeg, w / 2f, h / 2f)
        for (row in 0 until cells) {
            val direction = if (row % 2 == 0) 1f else -1f
            val total = offsetPx * direction
            val shift = floor(total / step).toInt()
            val frac = total - shift * step
            val y = startY + row * step
            for (col in 0 until cells) {
                val x = startX + (col - 1) * step + frac
                val bitmap = list[Math.floorMod(col - shift + row * 3, list.size)]
                src.set(0, 0, bitmap.width, bitmap.height)
                dst.set(x, y, x + tile, y + tile)
                canvas.drawBitmap(bitmap, src, dst, tilePaint)
            }
        }
        canvas.restoreToCount(saved)
    }

    private fun shouldAnimate(list: List<Bitmap>): Boolean {
        if (speedDpPerSec <= 0f || list.isEmpty() || !isShown) return false
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ValueAnimator.areAnimatorsEnabled()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        resumeFrames()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        resumeFrames()
    }

    private fun resumeFrames() {
        lastFrameNanos = 0L
        if (isShown) invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lastFrameNanos = 0L
        clearTargets()
        if (loading) {
            // The pending loads were cancelled; let the next setCovers call load them again.
            loading = false
            loadedKey = emptyList()
        }
    }
}
