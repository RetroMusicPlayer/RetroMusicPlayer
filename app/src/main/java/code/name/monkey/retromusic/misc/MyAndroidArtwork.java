package code.name.monkey.retromusic.misc;

import android.graphics.Bitmap;

import org.jaudiotagger.tag.images.AndroidArtwork;

import java.io.File;
import java.io.IOException;

/**
 * This is a fork from jaudiotagger's AndroidArtwork, which doesn't implement
 * some Artwork methods and throws UnsupportedOperationException.
 *
 */
public class MyAndroidArtwork extends AndroidArtwork
{
    private Bitmap          bitmap;

    public MyAndroidArtwork()
    {

    }

    /**
     * Should be called when you wish to prime the artwork for saving
     *
     * This implementation tries to copy jaudiotagger's StandardArwork using
     * android's Bitmap class instead of java.awt's Image class.
     * https://bitbucket.org/ijabz/jaudiotagger/src/ddd521dba64331be36f3ee8c3e3f408cdd7eee15/src/org/jaudiotagger/tag/images/StandardArtwork.java?at=master&fileviewer=file-view-default#StandardArtwork.java-78
     *
     * @return true if this instance has a valid bitmap and was able to retrieve
     * the bitmap's dimensions.
     */
    public boolean setImageFromData()
    {
        if (bitmap != null) {
            setWidth(bitmap.getWidth());
            setHeight(bitmap.getHeight());
            return true;
        }
        return false;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static MyAndroidArtwork create(File file, Bitmap bitmap)  throws IOException
    {
        MyAndroidArtwork artwork = new MyAndroidArtwork();
        artwork.setFromFile(file);
        artwork.setBitmap(bitmap);
        return artwork;
    }

}
