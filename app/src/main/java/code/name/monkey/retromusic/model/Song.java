package code.name.monkey.retromusic.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Song implements Parcelable {
    public static final Song EMPTY_SONG = new Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "");
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    public final int id;
    public final String title;
    public final int trackNumber;
    public final int year;
    public final long duration;
    public final String data;
    public final long dateModified;
    public final int albumId;
    public final String albumName;
    public final int artistId;
    public final String artistName;

    public Song(int id, String title, int trackNumber, int year, long duration, String data, long dateModified, int albumId, String albumName, int artistId, String artistName) {
        this.id = id;
        this.title = title;
        this.trackNumber = trackNumber;
        this.year = year;
        this.duration = duration;
        this.data = data;
        this.dateModified = dateModified;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    protected Song(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.trackNumber = in.readInt();
        this.year = in.readInt();
        this.duration = in.readLong();
        this.data = in.readString();
        this.dateModified = in.readLong();
        this.albumId = in.readInt();
        this.albumName = in.readString();
        this.artistId = in.readInt();
        this.artistName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.trackNumber);
        dest.writeInt(this.year);
        dest.writeLong(this.duration);
        dest.writeString(this.data);
        dest.writeLong(this.dateModified);
        dest.writeInt(this.albumId);
        dest.writeString(this.albumName);
        dest.writeInt(this.artistId);
        dest.writeString(this.artistName);
    }
}
