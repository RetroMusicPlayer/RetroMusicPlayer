package code.name.monkey.retromusic.glide.palette;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;


public class BitmapPaletteResource implements Resource<BitmapPaletteWrapper> {

    private final BitmapPaletteWrapper bitmapPaletteWrapper;
    private final BitmapPool bitmapPool;

    public BitmapPaletteResource(BitmapPaletteWrapper bitmapPaletteWrapper, BitmapPool bitmapPool) {
        this.bitmapPaletteWrapper = bitmapPaletteWrapper;
        this.bitmapPool = bitmapPool;
    }

    @Override
    public BitmapPaletteWrapper get() {
        return bitmapPaletteWrapper;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(bitmapPaletteWrapper.getBitmap());
    }

    @Override
    public void recycle() {
        if (!bitmapPool.put(bitmapPaletteWrapper.getBitmap())) {
            bitmapPaletteWrapper.getBitmap().recycle();
        }
    }
}
