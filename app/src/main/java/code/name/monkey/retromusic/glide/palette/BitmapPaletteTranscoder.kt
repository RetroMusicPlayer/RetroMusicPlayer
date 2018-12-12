package code.name.monkey.retromusic.glide.palette


import android.graphics.Bitmap
import code.name.monkey.retromusic.util.RetroColorUtil
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder

class BitmapPaletteTranscoder : ResourceTranscoder<Bitmap, BitmapPaletteWrapper> {
    override fun transcode(bitmapResource: Resource<Bitmap>, options: Options): Resource<BitmapPaletteWrapper>? {
        val bitmap = bitmapResource.get()
        val bitmapPaletteWrapper = BitmapPaletteWrapper(bitmap, RetroColorUtil.generatePalette(bitmap)!!)
        return BitmapPaletteResource(bitmapPaletteWrapper)
    }
}