package code.name.monkey.retromusic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Hemanth S (h4h13).
 */

public class Genre implements Parcelable {

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
    public final int id;
    public final String name;
    public final int songCount;

    public Genre(final int id, final String name, int songCount) {
        this.id = id;
        this.name = name;
        this.songCount = songCount;
    }


    // For unknown genre
    public Genre(final String name, final int songCount) {
        this.id = -1;
        this.name = name;
        this.songCount = songCount;
    }

    protected Genre(Parcel in) {
        id = in.readInt();
        name = in.readString();
        songCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(songCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        if (id != genre.id) return false;
        return name != null ? name.equals(genre.name) : genre.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}