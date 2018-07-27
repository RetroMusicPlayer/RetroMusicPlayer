package code.name.monkey.retromusic.glide.artistimage;


public class ArtistImage {
    public final String artistName;
    public final boolean skipOkHttpCache;

    public ArtistImage(String artistName, boolean skipOkHttpCache) {
        this.artistName = artistName;
        this.skipOkHttpCache = skipOkHttpCache;
    }
}
