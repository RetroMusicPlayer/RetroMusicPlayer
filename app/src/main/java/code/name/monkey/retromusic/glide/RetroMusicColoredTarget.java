package code.name.monkey.retromusic.glide;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.RetroApplication;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTarget;
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper;
import code.name.monkey.retromusic.util.PreferenceUtil;

import static code.name.monkey.retromusic.util.RetroColorUtil.getColor;
import static code.name.monkey.retromusic.util.RetroColorUtil.getDominantColor;


public abstract class RetroMusicColoredTarget extends BitmapPaletteTarget {

    public RetroMusicColoredTarget(ImageView view) {
        super(view);
    }

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        super.onLoadFailed(e, errorDrawable);
        onColorReady(getDefaultFooterColor());
    }

    @Override
    public void onResourceReady(BitmapPaletteWrapper resource,
                                GlideAnimation<? super BitmapPaletteWrapper> glideAnimation) {
        super.onResourceReady(resource, glideAnimation);
        int defaultColor = getDefaultFooterColor();

        int primaryColor = getColor(resource.getPalette(), defaultColor);
        int dominantColor = getDominantColor(resource.getBitmap(), defaultColor);

        onColorReady(PreferenceUtil.getInstance().isDominantColor() ?
                dominantColor : primaryColor);
    }

    protected int getDefaultFooterColor() {
        return ATHUtil.resolveColor(getView().getContext(), R.attr.defaultFooterColor);
    }

    protected int getAlbumArtistFooterColor() {
        return ATHUtil.resolveColor(getView().getContext(), R.attr.cardBackgroundColor);
    }

    public abstract void onColorReady(int color);
}
