package code.name.monkey.retromusic.glide.palette

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.palette.graphics.Palette
import code.name.monkey.retromusic.util.RetroColorUtil
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.util.Util

class BitmapPaletteTranscoder : ResourceTranscoder<Bitmap, BitmapPaletteWrapper> {
    override fun transcode(bitmapResource: Resource<Bitmap>, options: Options): Resource<BitmapPaletteWrapper>? {
        val bitmap = bitmapResource.get()
        val bitmapPaletteWrapper = BitmapPaletteWrapper(bitmap, RetroColorUtil.generatePalette(bitmap)!!)
        return BitmapPaletteResource(bitmapPaletteWrapper)
    }
}

class BitmapPaletteWrapper(val bitmap: Bitmap, val palette: Palette)

open class BitmapPaletteTarget(view: ImageView) : ImageViewTarget<BitmapPaletteWrapper>(view) {

    override fun setResource(bitmapPaletteWrapper: BitmapPaletteWrapper?) {
        if (bitmapPaletteWrapper != null) {
            view.setImageBitmap(bitmapPaletteWrapper.bitmap)
        }
    }
}

class BitmapPaletteResource(private val bitmapPaletteWrapper: BitmapPaletteWrapper) : Resource<BitmapPaletteWrapper> {

    override fun get(): BitmapPaletteWrapper {
        return bitmapPaletteWrapper
    }

    override fun getResourceClass(): Class<BitmapPaletteWrapper> {
        return BitmapPaletteWrapper::class.java
    }

    override fun getSize(): Int {
        return Util.getBitmapByteSize(bitmapPaletteWrapper.bitmap)
    }

    override fun recycle() {
        bitmapPaletteWrapper.bitmap.recycle()
    }
}