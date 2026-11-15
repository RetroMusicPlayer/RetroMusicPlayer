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
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.model.Song
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.util.concurrent.Executors
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.cos
import kotlin.math.sin

/**
 * A wall of album covers drawn at an angle, darkened, slowly scrolling.
 * The covers sit in tilted columns that drift downwards, with rounded corners and a vignette
 * that is darker on the left. Everything is drawn in [onDraw] from a few small bitmaps, so it
 * stays cheap; the animation stops when the view is hidden, detached, or when the speed is 0
 * or the system has animations turned off.
 */
class TiltedCoverGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var covers: List<Bitmap> = emptyList()
    private var coverSongs: List<Song> = emptyList()

    /** Called with the songs on the wall and the index of the one that was tapped. */
    var onSongClick: ((songs: List<Song>, index: Int) -> Unit)? = null

    /** Called with the songs on the wall and the index of the one that was long-pressed. */
    var onSongLongClick: ((songs: List<Song>, hit: WallHit) -> Unit)? = null

    /** A cover under the finger: its index and where it is drawn, in this view's coordinates. */
    class WallHit(val index: Int, val centerX: Float, val centerY: Float, val half: Float)

    private var frozen = false

    /** Stops the drift while a menu is open, so the cover stays under the menu. */
    fun setFrozen(value: Boolean) {
        if (frozen == value) return
        frozen = value
        lastFrameNanos = 0L
        invalidate()
    }

    private val tapDetector = GestureDetector(
        context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = covers.isNotEmpty()

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val index = hitTest(e.x, e.y)
                if (index < 0) return false
                playSoundEffect(android.view.SoundEffectConstants.CLICK)
                onSongClick?.invoke(coverSongs, index)
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val hit = findHit(e.x, e.y) ?: return
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                onSongLongClick?.invoke(coverSongs, hit)
            }
        }
    )
    private val targets = ArrayList<CustomTarget<Bitmap>>()
    private var loadedKey: List<Long> = emptyList()
    private var loading = false
    private var generation = 0

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
        applyShade(width.toFloat())
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
            coverSongs = emptyList()
            invalidate()
            return
        }
        loading = true
        val size = (120 * density).toInt()
        val myGeneration = ++generation
        val loaded = arrayOfNulls<Bitmap>(songs.size)
        var finished = 0
        fun onOneFinished() {
            finished++
            if (finished == songs.size) {
                loading = false
                val keep = loaded.indices.filter { loaded[it] != null }
                covers = keep.map { loaded[it]!! }
                coverSongs = keep.map { songs[it] }
                invalidate()
            }
        }
        songs.forEachIndexed { index, song ->
            val cached = roundedCache.get(song.id)
            if (cached != null) {
                loaded[index] = cached
                onOneFinished()
                return@forEachIndexed
            }
            val target = object : CustomTarget<Bitmap>(size, size) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Glide may reuse its own bitmap, so take a quick copy here and do the
                    // slower rounding on a background thread, not on the main thread.
                    val copy = resource.copy(Bitmap.Config.ARGB_8888, false)
                    if (copy == null) {
                        onOneFinished()
                        return
                    }
                    roundExecutor.execute {
                        val rounded = roundedCopy(copy)
                        copy.recycle()
                        roundedCache.put(song.id, rounded)
                        post {
                            if (myGeneration == generation) {
                                loaded[index] = rounded
                                onOneFinished()
                            }
                        }
                    }
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

    private fun roundedCopy(source: Bitmap): Bitmap {
        val out = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val radius = min(source.width, source.height) * 0.1f
        canvas.drawRoundRect(
            RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), radius, radius, paint
        )
        return out
    }

    private fun clearTargets() {
        val glide = Glide.with(context.applicationContext)
        targets.forEach { glide.clear(it) }
        targets.clear()
    }

    /** Darker on the left, lighter on the right, like a soft vignette. */
    private fun applyShade(w: Float) {
        if (w <= 0f) return
        val left = Color.argb((darkness.coerceAtMost(1f) * 255).toInt(), 0, 0, 0)
        val right = Color.argb((darkness * 0.45f * 255).toInt(), 0, 0, 0)
        shadePaint.shader = LinearGradient(0f, 0f, w, 0f, left, right, Shader.TileMode.CLAMP)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val tv = TypedValue()
        val surface = if (context.theme.resolveAttribute(android.R.attr.colorBackground, tv, true)) {
            tv.data
        } else {
            Color.BLACK
        }
        applyShade(w.toFloat())
        fadePaint.shader = LinearGradient(
            0f, h * 0.7f, 0f, h.toFloat(),
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
        canvas.drawRect(0f, h * 0.7f, w, h, fadePaint)

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

    /** Layout numbers shared by drawing and tap detection. */
    private class Grid(
        val tile: Float,
        val step: Float,
        val cells: Int,
        val startX: Float,
        val startY: Float
    )

    private fun gridFor(w: Float, h: Float): Grid {
        // Tiles are sized from the short side so the wall looks the same in portrait and landscape.
        val tile = min(w, h) / 3.2f
        val step = tile + 6f * density
        val diagonal = hypot(w, h)
        return Grid(
            tile = tile,
            step = step,
            cells = ceil(diagonal / step).toInt() + 2,
            startX = w / 2f - diagonal / 2f,
            startY = h / 2f - diagonal / 2f
        )
    }

    /** Every column drifts down; [col] only changes where its covers start. */
    private fun columnShiftAndFrac(step: Float): Pair<Int, Float> {
        val shift = floor(offsetPx / step).toInt()
        return shift to (offsetPx - shift * step)
    }

    private fun drawGrid(canvas: Canvas, list: List<Bitmap>, w: Float, h: Float) {
        val g = gridFor(w, h)
        val (shift, frac) = columnShiftAndFrac(g.step)
        val saved = canvas.save()
        canvas.rotate(-angleDeg, w / 2f, h / 2f)
        for (col in 0 until g.cells) {
            val x = g.startX + col * g.step
            for (row in 0 until g.cells) {
                val y = g.startY + (row - 1) * g.step + frac
                val bitmap = list[Math.floorMod(row - shift + col * 3, list.size)]
                src.set(0, 0, bitmap.width, bitmap.height)
                dst.set(x, y, x + g.tile, y + g.tile)
                canvas.drawBitmap(bitmap, src, dst, tilePaint)
            }
        }
        canvas.restoreToCount(saved)
    }

    /** Index in [coverSongs] of the cover under the touch point, or -1 for a gap / nothing. */
    private fun hitTest(touchX: Float, touchY: Float): Int = findHit(touchX, touchY)?.index ?: -1

    private fun findHit(touchX: Float, touchY: Float): WallHit? {
        val list = covers
        if (list.isEmpty()) return null
        val w = width.toFloat()
        val h = height.toFloat()
        val g = gridFor(w, h)
        // Undo the canvas rotation: rotate the touch point back around the centre.
        val rad = Math.toRadians(angleDeg.toDouble())
        val dx = touchX - w / 2f
        val dy = touchY - h / 2f
        val px = (w / 2f + dx * cos(rad) - dy * sin(rad)).toFloat()
        val py = (h / 2f + dx * sin(rad) + dy * cos(rad)).toFloat()

        val colF = (px - g.startX) / g.step
        val col = floor(colF).toInt()
        if (col < 0 || col >= g.cells) return null
        if ((colF - col) * g.step > g.tile) return null
        val (shift, frac) = columnShiftAndFrac(g.step)
        val rowF = (py - g.startY - frac) / g.step
        val rowMinus1 = floor(rowF).toInt()
        if ((rowF - rowMinus1) * g.step > g.tile) return null
        val row = rowMinus1 + 1
        if (row < 0 || row >= g.cells) return null
        val index = Math.floorMod(row - shift + col * 3, list.size)

        // Centre of that tile, rotated forward again so it matches where it is drawn.
        val tileCx = g.startX + col * g.step + g.tile / 2f
        val tileCy = g.startY + rowMinus1 * g.step + frac + g.tile / 2f
        val ox = tileCx - w / 2f
        val oy = tileCy - h / 2f
        val cx = (w / 2f + ox * cos(rad) + oy * sin(rad)).toFloat()
        val cy = (h / 2f - ox * sin(rad) + oy * cos(rad)).toFloat()
        return WallHit(index, cx, cy, g.tile / 2f)
    }

    /** For taps that land on the empty hero area above this view's scrolling content. */
    fun tapAtScreen(rawX: Float, rawY: Float): Boolean {
        val loc = IntArray(2)
        getLocationOnScreen(loc)
        val index = hitTest(rawX - loc[0], rawY - loc[1])
        if (index < 0) return false
        onSongClick?.invoke(coverSongs, index)
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return tapDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun shouldAnimate(list: List<Bitmap>): Boolean {
        if (frozen || speedDpPerSec <= 0f || list.isEmpty() || !isShown) return false
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

    private companion object {
        /** Rounded covers survive leaving and re-entering the home screen (about 24 MB at most). */
        val roundedCache = object : LruCache<Long, Bitmap>(24 * 1024 * 1024) {
            override fun sizeOf(key: Long, value: Bitmap): Int = value.byteCount
        }
        val roundExecutor = Executors.newSingleThreadExecutor()
    }
}
