package code.name.monkey.retromusic.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import code.name.monkey.retromusic.glide.artistimage.ArtistImage;
import code.name.monkey.retromusic.glide.artistimage.ArtistImageLoader;
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover;
import code.name.monkey.retromusic.glide.audiocover.AudioFileCoverLoader;

import java.io.InputStream;


public class RetroMusicGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(AudioFileCover.class, InputStream.class, new AudioFileCoverLoader.Factory());
        glide.register(ArtistImage.class, InputStream.class, new ArtistImageLoader.Factory(context));
    }
}
