package code.name.monkey.retromusic.glide.palette

import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget

open class BitmapPaletteTarget(view: ImageView) : ImageViewTarget<BitmapPaletteWrapper>(view) {

    override fun setResource(bitmapPaletteWrapper: BitmapPaletteWrapper) {
        view.setImageBitmap(bitmapPaletteWrapper.bitmap)
    }

}
