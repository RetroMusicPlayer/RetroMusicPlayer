package code.name.monkey.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.*
import androidx.annotation.FloatRange
import code.name.monkey.retromusic.BuildConfig
import code.name.monkey.retromusic.helper.StackBlur
import code.name.monkey.retromusic.util.ImageUtil
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


@Suppress("Deprecation")
class BlurTransformation private constructor(builder: Builder) : BitmapTransformation() {

    private var context: Context? = null
    private var blurRadius = 0f
    private var sampling = 0

    init {
        this.context = builder.context
        this.blurRadius = builder.blurRadius
        this.sampling = builder.sampling
    }

    class Builder(val context: Context) {
        private var bitmapPool: BitmapPool? = null
        var blurRadius = DEFAULT_BLUR_RADIUS
        var sampling = 0

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
        fun bitmapPool(bitmapPool: BitmapPool?): Builder {
            this.bitmapPool = bitmapPool
            return this
        }

        fun build(): BlurTransformation {
            return if (bitmapPool != null) {
                BlurTransformation(this)
            } else BlurTransformation(this)
        }
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int,
    ): Bitmap {
        val sampling = if (this.sampling == 0) {
            ImageUtil.calculateInSampleSize(toTransform.width, toTransform.height, 100)
        } else {
            this.sampling
        }
        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling
        val out = pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(out)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

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
            output.destroy()
            script.destroy()
            input.destroy()
            rs.destroy()

            return out
        } catch (e: RSRuntimeException) {
            // on some devices RenderScript.create() throws: android.support.v8.renderscript.RSRuntimeException: Error loading libRSSupport library
            if (BuildConfig.DEBUG) e.printStackTrace()
        }

        return StackBlur.blur(out, blurRadius)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(
            "BlurTransformation(radius=$blurRadius, sampling=$sampling)".toByteArray(
                CHARSET
            )
        )
    }

    companion object {
        const val DEFAULT_BLUR_RADIUS = 5f
    }
}
