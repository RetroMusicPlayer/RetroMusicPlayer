package code.name.monkey.retromusic.model.smartplaylist;

import android.content.Context;
import android.os.Parcel;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;


public abstract class AbsSmartPlaylist extends AbsCustomPlaylist {
    @DrawableRes
    public final int iconRes;

    public AbsSmartPlaylist(final String name, final int iconRes) {
        super(-Math.abs(31 * name.hashCode() + (iconRes * name.hashCode() * 31 * 31)), name);
        this.iconRes = iconRes;
    }

    public AbsSmartPlaylist() {
        super();
        this.iconRes = R.drawable.ic_playlist_play_white_24dp;
    }

    protected AbsSmartPlaylist(Parcel in) {
        super(in);
        this.iconRes = in.readInt();
    }

    public abstract void clear(Context context);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + iconRes;
        return result;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (super.equals(obj)) {
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AbsSmartPlaylist other = (AbsSmartPlaylist) obj;
            return iconRes == other.iconRes;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.iconRes);
    }
}
