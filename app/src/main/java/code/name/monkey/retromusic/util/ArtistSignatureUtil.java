package code.name.monkey.retromusic.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.bumptech.glide.signature.StringSignature;


public class ArtistSignatureUtil {
    private static final String ARTIST_SIGNATURE_PREFS = "artist_signatures";

    private static ArtistSignatureUtil sInstance;

    private final SharedPreferences mPreferences;

    private ArtistSignatureUtil(@NonNull final Context context) {
        mPreferences = context.getSharedPreferences(ARTIST_SIGNATURE_PREFS, Context.MODE_PRIVATE);
    }

    public static ArtistSignatureUtil getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new ArtistSignatureUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    @SuppressLint("CommitPrefEdits")
    public void updateArtistSignature(String artistName) {
        mPreferences.edit().putLong(artistName, System.currentTimeMillis()).apply();
    }

    public long getArtistSignatureRaw(String artistName) {
        return mPreferences.getLong(artistName, 0);
    }

    public StringSignature getArtistSignature(String artistName) {
        return new StringSignature(String.valueOf(getArtistSignatureRaw(artistName)));
    }
}
