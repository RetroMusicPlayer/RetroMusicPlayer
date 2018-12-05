package code.name.monkey.retromusic.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import androidx.annotation.NonNull;
import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.model.Artist;


public class CustomArtistImageUtil {
    private static final String CUSTOM_ARTIST_IMAGE_PREFS = "custom_artist_image";
    private static final String FOLDER_NAME = "/custom_artist_images/";

    private static CustomArtistImageUtil sInstance;

    private final SharedPreferences mPreferences;

    private CustomArtistImageUtil(@NonNull final Context context) {
        mPreferences = context.getApplicationContext().getSharedPreferences(CUSTOM_ARTIST_IMAGE_PREFS, Context.MODE_PRIVATE);
    }

    public static CustomArtistImageUtil getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new CustomArtistImageUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    private static String getFileName(Artist artist) {
        String artistName = artist.getName();
        if (artistName == null)
            artistName = "";
        // replace everything that is not a letter or a number with _
        artistName = artistName.replaceAll("[^a-zA-Z0-9]", "_");
        return String.format(Locale.US, "#%d#%s.jpeg", artist.getId(), artistName);
    }

    public static File getFile(Artist artist) {
        File dir = new File(App.Companion.getInstance().getFilesDir(), FOLDER_NAME);
        return new File(dir, getFileName(artist));
    }

    public void setCustomArtistImage(final Artist artist, Uri uri) {
        Glide.with(App.Companion.getInstance())
                .load(uri)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        e.printStackTrace();
                        Toast.makeText(App.Companion.getInstance(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        new AsyncTask<Void, Void, Void>() {
                            @SuppressLint("ApplySharedPref")
                            @Override
                            protected Void doInBackground(Void... params) {
                                File dir = new File(App.Companion.getInstance().getFilesDir(), FOLDER_NAME);
                                if (!dir.exists()) {
                                    if (!dir.mkdirs()) { // create the folder
                                        return null;
                                    }
                                }
                                File file = new File(dir, getFileName(artist));

                                boolean succesful = false;
                                try {
                                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                                    succesful = ImageUtil.resizeBitmap(resource, 2048).compress(Bitmap.CompressFormat.JPEG, 100, os);
                                    os.close();
                                } catch (IOException e) {
                                    Toast.makeText(App.Companion.getInstance(), e.toString(), Toast.LENGTH_LONG).show();
                                }

                                if (succesful) {
                                    mPreferences.edit().putBoolean(getFileName(artist), true).commit();
                                    ArtistSignatureUtil.getInstance(App.Companion.getInstance()).updateArtistSignature(artist.getName());
                                    App.Companion.getInstance().getContentResolver().notifyChange(Uri.parse("content://media"), null); // trigger media store changed to force artist image reload
                                }
                                return null;
                            }
                        }.execute();
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    public void resetCustomArtistImage(final Artist artist) {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("ApplySharedPref")
            @Override
            protected Void doInBackground(Void... params) {
                mPreferences.edit().putBoolean(getFileName(artist), false).commit();
                ArtistSignatureUtil.getInstance(App.Companion.getInstance()).updateArtistSignature(artist.getName());
                App.Companion.getInstance().getContentResolver().notifyChange(Uri.parse("content://media"), null); // trigger media store changed to force artist image reload

                File file = getFile(artist);
                if (!file.exists()) {
                    return null;
                } else {
                    file.delete();
                }
                return null;
            }
        }.execute();
    }

    // shared prefs saves us many IO operations
    public boolean hasCustomArtistImage(Artist artist) {
        return mPreferences.getBoolean(getFileName(artist), false);
    }
}