package code.name.monkey.retromusic.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView

import com.bumptech.glide.request.animation.GlideAnimation

import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.RetroApplication
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTarget
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.util.PreferenceUtil

import code.name.monkey.retromusic.util.RetroColorUtil.getColor
import code.name.monkey.retromusic.util.RetroColorUtil.getDominantColor


abstract class RetroMusicColoredTarget(view: ImageView) : BitmapPaletteTarget(view) {

    protected val defaultFooterColor: Int
        get() = ATHUtil.resolveColor(getView().context, R.attr.defaultFooterColor)

    protected val albumArtistFooterColor: Int
        get() = ATHUtil.resolveColor(getView().context, R.attr.cardBackgroundColor)

    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
        super.onLoadFailed(e, errorDrawable)
        onColorReady(defaultFooterColor)
    }

    override fun onResourceReady(resource: BitmapPaletteWrapper,
                                 glideAnimation: GlideAnimation<in BitmapPaletteWrapper>?) {
        super.onResourceReady(resource, glideAnimation)
        val defaultColor = defaultFooterColor

        val primaryColor = getColor(resource.palette, defaultColor)
        val dominantColor = getDominantColor(resource.bitmap, defaultColor)

        onColorReady(if (PreferenceUtil.getInstance().isDominantColor)
            dominantColor
        else
            primaryColor)
    }

    abstract fun onColorReady(color: Int)
}
