/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.renderscript.*
import androidx.annotation.FloatRange
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.helper.StackBlur
import code.name.monkey.retromusic.util.ImageUtil
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

class BlurTransformation : BitmapTransformation {

    private var context: Context? = null
    private var blurRadius: Float = 0.toFloat()
    private var sampling: Int = 0

    private fun init(builder: Builder) {
        this.context = builder.context
        this.blurRadius = builder.blurRadius
        this.sampling = builder.sampling
    }

    private constructor(builder: Builder) : super(builder.context) {
        init(builder)
    }

    private constructor(builder: Builder, bitmapPool: BitmapPool) : super(bitmapPool) {
        init(builder)
    }

    class Builder(val context: Context) {
        private var bitmapPool: BitmapPool? = null
        var blurRadius = DEFAULT_BLUR_RADIUS
        var sampling: Int = 0

        /**
         * @param blurRadius The radius to use. Must be between 0 and 25. Default is 5.
         * @return the same Builder
         */
        fun blurRadius(@FloatRange(from = 0.0, to = 25.0) blurRadius: Float): Builder {
            this.blurRadius = blurRadius
            return this
        }

        /**
         * @param sampling The inSampleSize to use. Must be a power of 2, or 1 for no down sampling or 0 for auto detect sampling. Default is 0.
         * @return the same Builder
         */
        fun sampling(sampling: Int): Builder {
            this.sampling = sampling
            return this
        }

        /**
         * @param bitmapPool The BitmapPool to use.
         * @return the same Builder
         */
        fun bitmapPool(bitmapPool: BitmapPool): Builder {
            this.bitmapPool = bitmapPool
            return this
        }

        fun build(): BlurTransformation {
            return if (bitmapPool != null) {
                BlurTransformation(this, bitmapPool!!)
            } else BlurTransformation(this)
        }
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap? {
        val sampling: Int
        if (this.sampling == 0) {
            sampling = ImageUtil.calculateInSampleSize(toTransform.width, toTransform.height, 100)
        } else {
            sampling = this.sampling
        }

        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling

        var out: Bitmap? = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        if (out == null) {
            out = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(out!!)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        if (Build.VERSION.SDK_INT >= 17) {
            try {
                val rs = RenderScript.create(context!!.applicationContext)
                val input = Allocation.createFromBitmap(
                    rs,
                    out,
                    Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT
                )
                val output = Allocation.createTyped(rs, input.type)
                val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

                script.setRadius(blurRadius)
                script.setInput(input)
                script.forEach(output)

                output.copyTo(out)

                rs.destroy()

                return out
            } catch (e: RSRuntimeException) {
                // on some devices RenderScript.create() throws: android.support.v8.renderscript.RSRuntimeException: Error loading libRSSupport library
                if (BuildConfig.DEBUG) e.printStackTrace()
            }
        }

        return StackBlur.blur(out, blurRadius)
    }

    override fun getId(): String {
        return "BlurTransformation(radius=$blurRadius, sampling=$sampling)"
    }

    companion object {
        val DEFAULT_BLUR_RADIUS = 5f
    }
}
