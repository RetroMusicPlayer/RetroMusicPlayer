package code.name.monkey.retromusic.cast;

import android.net.Uri;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadOptions;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import java.net.MalformedURLException;
import java.net.URL;

import code.name.monkey.retromusic.Constants;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.util.RetroUtil;

import static code.name.monkey.retromusic.Constants.CAST_SERVER_PORT;

public class CastHelper {

    public static void startCasting(CastSession castSession, Song song) {

        String ipAddress = RetroUtil.getIPAddress(true);
        URL baseUrl;
        try {
            baseUrl = new URL("https", ipAddress,CAST_SERVER_PORT, "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        String songUrl = baseUrl.toString() + "/song?id=" + song.getId();
        String albumArtUrl = baseUrl.toString() + "/albumart?id=" + song.getAlbumId();


        MediaMetadata musicMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK);

        musicMetadata.putString(MediaMetadata.KEY_TITLE, song.getTitle());
        musicMetadata.putString(MediaMetadata.KEY_ARTIST, song.getArtistName());
        musicMetadata.putString(MediaMetadata.KEY_ALBUM_TITLE, song.getAlbumName());
        musicMetadata.putInt(MediaMetadata.KEY_TRACK_NUMBER, song.getTrackNumber());
        musicMetadata.addImage(new WebImage(Uri.parse(albumArtUrl)));

        try {
            MediaInfo mediaInfo = new MediaInfo.Builder(songUrl)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("audio/mpeg")
                    .setMetadata(musicMetadata)
                    .setStreamDuration(song.getDuration())
                    .build();
            RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
            remoteMediaClient.load(mediaInfo, new MediaLoadOptions.Builder()
                    .build());
            //remoteMediaClient.load(mediaInfo, true, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
