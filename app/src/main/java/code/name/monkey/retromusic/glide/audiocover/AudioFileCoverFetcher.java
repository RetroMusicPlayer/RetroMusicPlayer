package code.name.monkey.retromusic.glide.audiocover;

import android.media.MediaMetadataRetriever;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class AudioFileCoverFetcher implements DataFetcher<InputStream> {
    private static final String[] FALLBACKS = {"cover.jpg", "album.jpg", "folder.jpg", "cover.png", "album.png", "folder.png"};
    private final AudioFileCover model;
    private FileInputStream stream;

    public AudioFileCoverFetcher(AudioFileCover model) {
        this.model = model;
    }

    @Override
    public String getId() {
        // makes sure we never ever return null here
        return String.valueOf(model.filePath);
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(model.filePath);
            byte[] picture = retriever.getEmbeddedPicture();
            if (picture != null) {
                return new ByteArrayInputStream(picture);
            } else {
                return fallback(model.filePath);
            }
        } finally {
            retriever.release();
        }
    }

    private InputStream fallback(String path) throws FileNotFoundException {
        // Method 1: use embedded high resolution album art if there is any
        try {
            MP3File mp3File = new MP3File(path);
            if (mp3File.hasID3v2Tag()) {
                Artwork art = mp3File.getTag().getFirstArtwork();
                if (art != null) {
                    byte[] imageData = art.getBinaryData();
                    return new ByteArrayInputStream(imageData);
                }
            }
            // If there are any exceptions, we ignore them and continue to the other fallback method
        } catch (ReadOnlyFileException | InvalidAudioFrameException | TagException | IOException ignored) {
        }

        // Method 2: look for album art in external files
        File parent = new File(path).getParentFile();
        for (String fallback : FALLBACKS) {
            File cover = new File(parent, fallback);
            if (cover.exists()) {
                return stream = new FileInputStream(cover);
            }
        }
        return null;
    }

    @Override
    public void cleanup() {
        // already cleaned up in loadData and ByteArrayInputStream will be GC'd
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ignore) {
                // can't do much about it
            }
        }
    }

    @Override
    public void cancel() {
        // cannot cancel
    }
}