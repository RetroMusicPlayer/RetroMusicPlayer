package code.name.monkey.retromusic.glide.palette

import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.util.Util


class BitmapPaletteResource(private val bitmapPaletteWrapper: BitmapPaletteWrapper, private val bitmapPool: BitmapPool) : Resource<BitmapPaletteWrapper> {

    override fun get(): BitmapPaletteWrapper {
        return bitmapPaletteWrapper
    }

    override fun getSize(): Int {
        return Util.getBitmapByteSize(bitmapPaletteWrapper.bitmap)
    }

    override fun recycle() {
        if (!bitmapPool.put(bitmapPaletteWrapper.bitmap)) {
            bitmapPaletteWrapper.bitmap.recycle()
        }
    }
}
