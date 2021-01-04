package io.github.muntashirakon.music.activities.tageditor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.github.muntashirakon.music.R;
import io.github.muntashirakon.music.misc.DialogAsyncTask;
import io.github.muntashirakon.music.misc.UpdateToastMediaScannerCompletionListener;
import io.github.muntashirakon.music.model.LoadingInfo;
import io.github.muntashirakon.music.util.MusicUtil;

public class WriteTagsAsyncTask extends DialogAsyncTask<LoadingInfo, Integer, List<String>> {

    public WriteTagsAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected List<String> doInBackground(LoadingInfo... params) {
        try {
            LoadingInfo info = params[0];

            Artwork artwork = null;
            File albumArtFile = null;
            if (info.getArtworkInfo() != null && info.getArtworkInfo().getArtwork() != null) {
                try {
                    albumArtFile = MusicUtil.INSTANCE.createAlbumArtFile().getCanonicalFile();
                    info.getArtworkInfo().getArtwork().compress(Bitmap.CompressFormat.PNG, 0, new FileOutputStream(albumArtFile));
                    artwork = ArtworkFactory.createArtworkFromFile(albumArtFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            int counter = 0;
            boolean wroteArtwork = false;
            boolean deletedArtwork = false;
            for (String filePath : info.getFilePaths()) {
                publishProgress(++counter, info.getFilePaths().size());
                try {
                    AudioFile audioFile = AudioFileIO.read(new File(filePath));
                    Tag tag = audioFile.getTagOrCreateAndSetDefault();

                    if (info.getFieldKeyValueMap() != null) {
                        for (Map.Entry<FieldKey, String> entry : info.getFieldKeyValueMap().entrySet()) {
                            try {
                                tag.setField(entry.getKey(), entry.getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (info.getArtworkInfo() != null) {
                        if (info.getArtworkInfo().getArtwork() == null) {
                            tag.deleteArtworkField();
                            deletedArtwork = true;
                        } else if (artwork != null) {
                            tag.deleteArtworkField();
                            tag.setField(artwork);
                            wroteArtwork = true;
                        }
                    }

                    audioFile.commit();
                } catch (@NonNull CannotReadException | IOException | CannotWriteException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
                    e.printStackTrace();
                }
            }

            Context context = getContext();
            if (context != null) {
                if (wroteArtwork) {
                    MusicUtil.INSTANCE.
                            insertAlbumArt(context, info.getArtworkInfo().getAlbumId(), albumArtFile.getPath());
                } else if (deletedArtwork) {
                    MusicUtil.INSTANCE.deleteAlbumArt(context, info.getArtworkInfo().getAlbumId());
                }
            }

            return info.getFilePaths();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<String> toBeScanned) {
        super.onPostExecute(toBeScanned);
        scan(toBeScanned);
    }

    @Override
    protected void onCancelled(List<String> toBeScanned) {
        super.onCancelled(toBeScanned);
        scan(toBeScanned);
    }

    private void scan(List<String> toBeScanned) {
        Context context = getContext();
        if (toBeScanned == null || toBeScanned.isEmpty()) {
            Log.i("scan", "scan: Empty");
            Toast.makeText(context, "Scan file from folder", Toast.LENGTH_SHORT).show();
            return;
        }
        MediaScannerConnection.scanFile(context, toBeScanned.toArray(new String[0]), null, context instanceof Activity ? new UpdateToastMediaScannerCompletionListener((Activity) context, toBeScanned) : null);
    }

    @Override
    protected Dialog createDialog(@NonNull Context context) {
        return new MaterialDialog.Builder(context)
                .title(R.string.saving_changes)
                .cancelable(false)
                .progress(false, 0)
                .build();
    }

    @Override
    protected void onProgressUpdate(@NonNull Dialog dialog, Integer... values) {
        super.onProgressUpdate(dialog, values);
        ((MaterialDialog) dialog).setMaxProgress(values[1]);
        ((MaterialDialog) dialog).setProgress(values[0]);
    }
}
