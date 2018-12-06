package code.name.monkey.retromusic.glide.palette

import android.content.Context
import android.graphics.Bitmap

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import code.name.monkey.retromusic.util.RetroColorUtil

class BitmapPaletteTranscoder(private val bitmapPool: BitmapPool) : ResourceTranscoder<Bitmap, BitmapPaletteWrapper> {

    constructor(context: Context) : this(Glide.get(context).bitmapPool) {}

    override fun transcode(bitmapResource: Resource<Bitmap>): Resource<BitmapPaletteWrapper> {
        val bitmap = bitmapResource.get()
        val bitmapPaletteWrapper = BitmapPaletteWrapper(bitmap, RetroColorUtil.generatePalette(bitmap)!!)
        return BitmapPaletteResource(bitmapPaletteWrapper, bitmapPool)
    }

    override fun getId(): String {
        return "BitmapPaletteTranscoder.code.name.monkey.retromusic.glide.palette"
    }
}