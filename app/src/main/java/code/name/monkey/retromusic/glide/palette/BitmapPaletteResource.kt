package code.name.monkey.retromusic.glide.palette

import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.util.Util


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