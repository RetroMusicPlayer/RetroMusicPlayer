package code.name.monkey.retromusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import code.name.monkey.retromusic.R;


public class CategoryInfo implements Parcelable {
    public static final Parcelable.Creator<CategoryInfo> CREATOR = new Parcelable.Creator<CategoryInfo>() {
        public CategoryInfo createFromParcel(Parcel source) {
            return new CategoryInfo(source);
        }

        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };
    public Category category;
    public boolean visible;

    public CategoryInfo(Category category, boolean visible) {
        this.category = category;
        this.visible = visible;
    }


    private CategoryInfo(Parcel source) {
        category = (Category) source.readSerializable();
        visible = source.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(category);
        dest.writeInt(visible ? 1 : 0);
    }

    public enum Category {
        SONGS(R.string.songs),
        ALBUMS(R.string.albums),
        ARTISTS(R.string.artists),
        GENRES(R.string.genres),
        PLAYLISTS(R.string.playlists);

        public final int stringRes;

        Category(int stringRes) {
            this.stringRes = stringRes;
        }
    }
}
