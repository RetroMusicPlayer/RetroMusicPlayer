package code.name.monkey.retromusic.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.ArrayList;

import code.name.monkey.retromusic.util.MusicUtil;


public class Artist implements Parcelable {
    public static final String UNKNOWN_ARTIST_DISPLAY_NAME = "Unknown Artist";
    public final ArrayList<Album> albums;

    public Artist(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public Artist() {
        this.albums = new ArrayList<>();
    }

    public int getId() {
        return safeGetFirstAlbum().getArtistId();
    }

    public String getName() {
        String name = safeGetFirstAlbum().getArtistName();
        if (MusicUtil.isArtistNameUnknown(name)) {
            return UNKNOWN_ARTIST_DISPLAY_NAME;
        }
        return name;
    }

    public int getSongCount() {
        int songCount = 0;
        for (Album album : albums) {
            songCount += album.getSongCount();
        }
        return songCount;
    }

    public int getAlbumCount() {
        return albums.size();
    }

    public ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        for (Album album : albums) {
            songs.addAll(album.songs);
        }
        return songs;
    }

    @NonNull
    public Album safeGetFirstAlbum() {
        return albums.isEmpty() ? new Album() : albums.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        return albums != null ? albums.equals(artist.albums) : artist.albums == null;

    }

    @Override
    public int hashCode() {
        return albums != null ? albums.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "albums=" + albums +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.albums);
    }

    protected Artist(Parcel in) {
        this.albums = in.createTypedArrayList(Album.CREATOR);
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
